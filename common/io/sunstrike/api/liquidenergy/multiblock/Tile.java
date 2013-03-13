package io.sunstrike.api.liquidenergy.multiblock;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import ic2.api.IWrenchable;
import io.sunstrike.api.liquidenergy.Position;
import io.sunstrike.mods.liquidenergy.LiquidEnergy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

/*
 * Tile
 * io.sunstrike.api.liquidenergy.multiblock
 * LiquidEnergy
 *
 * The MIT License (MIT)
 * Copyright (c) 2013 Sunstrike <sunstrike@azurenode.net>
 *  
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial 
 * portions of the Software.
 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 * @author Sunstrike <sunstrike@azurenode.net>
 */

/**
 * Base TileEntity class for multiblocks
 *
 * @author Sunstrike <sunstrike@azurenode.net>
 */
public abstract class Tile extends TileEntity implements IWrenchable {

    protected IStructure structure = null;
    protected ForgeDirection orientation = ForgeDirection.UP;
    protected Position position;

    private boolean initialRenderDone = false;

    protected int tex = 0;

    public Tile() {
        this.position = new Position(this.xCoord, this.yCoord, this.zCoord, this.worldObj);
        //if (worldObj.isRemote) requestRenderUpdate(); // TODO: Sync state ASAP
    }

    /**
     * Handle first-load render updates
     */
    @Override
    public void updateEntity() {
        if (!initialRenderDone) {
            if (worldObj.isRemote) requestRenderUpdate();
            initialRenderDone = true;
        }
        super.updateEntity();
    }

    public void updatePosition() {
        position = new Position(xCoord, yCoord, zCoord, worldObj);
    }

    /**
     * Setter method for the structure this block is attached to
     *
     * @param structure IStructure object to attach to
     */
    public void setStructure(IStructure structure) {
        this.structure = structure;
    }

    /**
     * Getter method for the structure this block is attached to
     *
     * @return IStructure this is attached to
     */
    public IStructure getStructure() {
        return structure;
    }

    /**
     * Setter method for the direction this output faces (points out from output side)
     *
     * @param direction Direction to make the new orientation
     */
    public void setOrientation(ForgeDirection direction) {
        this.orientation = direction;
    }

    /**
     * Getter method for the current orientation
     *
     * @return The current block orientation
     */
    public ForgeDirection getOrientation() {
        return orientation;
    }

    @Override
    public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side) {
        if (side != orientation.ordinal()) return true;
        return false;
    }

    @Override
    public short getFacing() {
        return (short)orientation.ordinal();
    }

    @Override
    public void setFacing(short facing) {
        orientation = ForgeDirection.getOrientation((int)facing);
        sendRenderPacket();
    }

    @Override
    public boolean wrenchCanRemove(EntityPlayer entityPlayer) {
        return true;
    }

    @Override
    public float getWrenchDropRate() {
        return 1.0f;
    }

    /**
     * Drops when removed with a wrench
     *
     * @param entityPlayer player using the wrench
     * @return ItemStack of what to drop
     */
    @Override
    public abstract ItemStack getWrenchDrop(EntityPlayer entityPlayer);

    /**
     * Called by LEBlock instances to change textures on the fly (depending on orientation usually)
     *
     * @param side Direction being rendered
     * @return Texture ID
     */
    public int getTexture(ForgeDirection side) {
        return tex;
    }

    /**
     * Called when the player smacks a TE with a stick
     *
     * @param player The player smacking the TE
     */
    public void debugInfo(EntityPlayer player) {
        if (worldObj.isRemote || player.getCurrentEquippedItem() == null) return;
        if (player.getCurrentEquippedItem().getItem() != Item.stick) return;
        player.addChatMessage("[Tile] Orientation: " + orientation);
        if (structure != null) player.addChatMessage("[Tile] IStructure: " + structure);
    }

    /**
     * TEs use this to synchronise orientation to the client.
     *
     * @param net The NetworkManager the packet originated from
     * @param pkt The data packet
     */
    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
        if (worldObj.isRemote) handleClientPacket(pkt); // Client
        handleServerPacket(pkt);
    }

    /**
     * Client-side handler for packets. Used for render-updates.
     *
     * @param pkt The data packet
     */
    private void handleClientPacket(Packet132TileEntityData pkt) {
        if (pkt.actionType == 0) {
            int i = pkt.customParam1.getInteger("orientation");
            orientation = ForgeDirection.getOrientation(i);
            Minecraft.getMinecraft().renderGlobal.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
        }
    }

    /**
     * Server-side handler for packets. Used for render-update requests.
     *
     * @param pkt The data packet
     */
    private void handleServerPacket(Packet132TileEntityData pkt) {
        if (pkt.actionType == 1) {
            String pName = pkt.customParam1.getString("player");
            EntityPlayerMP player = MinecraftServer.getServerConfigurationManager(MinecraftServer.getServer()).getPlayerForUsername(pName);
            if (player != null) {
                sendRenderPacket(player); // Send specific
            } else {
                sendRenderPacket(); // Fallback to global
            }
        }
    }

    /**
     * Sends a rendering update packet to an area. Use to update orientation visually on all clients.
     */
    private void sendRenderPacket() {
        PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 100, worldObj.getWorldInfo().getDimension(), constructServerPacket132());
    }

    /**
     * Sends a rendering update packet to a specific player. Use to update orientation visually on a specific client.
     *
     * @param player Player to send packet to
     */
    private void sendRenderPacket(EntityPlayerMP player) {
        PacketDispatcher.sendPacketToPlayer(constructServerPacket132(), (Player)player);
    }

    /**
     * Helper method - constructs a Packet 132 for orientation updates FROM the server.
     *
     * @return The packet to be sent
     */
    private Packet132TileEntityData constructServerPacket132() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("orientation", orientation.ordinal());
        return new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, nbt);
    }

    /**
     * Helper method - constructs a Packet 132 for requesting a render update packet.
     *
     * @return The packet to be sent
     */
    private Packet250CustomPayload constructClientUpdateRequestPacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("player", Minecraft.getMinecraft().session.username);
        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "LE-Render";
        // FInish according to Forge wiki
    }

    /**
     * Client helper - constructs and sends a packet asking for new render data.
     */
    private void requestRenderUpdate() {
        PacketDispatcher.sendPacketToServer(constructClientUpdateRequestPacket());
    }

    /**
     * Reloading state from NBT, needs to update Position variable.
     *
     * @param nbt The NBT compound
     */
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        orientation = ForgeDirection.getOrientation(nbt.getShort("orientationOrdinal"));
        LiquidEnergy.logger.info("[Tile] Loaded with orientation " + orientation);
        updatePosition();
    }

    /**
     * Archiving state to NBT
     * @param nbt
     */
    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setShort("orientationOrdinal", (short)orientation.ordinal());
    }

}
