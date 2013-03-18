package io.sunstrike.api.liquidenergy.multiblock;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import ic2.api.IWrenchable;
import io.sunstrike.api.liquidenergy.Position;
import io.sunstrike.mods.liquidenergy.helpers.Packet250Helper;
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
import net.minecraft.world.World;
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

    protected ForgeDirection orientation = ForgeDirection.UP;
    protected Position position;

    protected boolean initialRenderDone = false;
    protected boolean failsafeTrip = false; // Used for 0, 0, 0 tiles (talk about edge cases...)

    protected int tex = 0;

    @Override
    public void setWorldObj(World par1World) {
        super.setWorldObj(par1World);
        if (worldObj.isRemote) requestRenderUpdate();
    }

    public Tile() {
        this.position = new Position(this.xCoord, this.yCoord, this.zCoord, this.worldObj);
    }

    public void updatePosition() {
        position = new Position(xCoord, yCoord, zCoord, worldObj);
    }

    /**
     * Setter method for the direction this output faces (points out from output side)
     *
     * @param direction Direction to make the new orientation
     */
    public void setOrientation(ForgeDirection direction) {
        this.orientation = direction;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlockId(xCoord, yCoord, zCoord));
        sendRenderPacket();
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
        setOrientation(ForgeDirection.getOrientation((int) facing));
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
    }

    /**
     * TEs use this to synchronise orientation to the client.
     *
     * @param net The NetworkManager the packet originated from
     * @param pkt The data packet
     */
    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
        if (!worldObj.isRemote) handleClientPacket(pkt); // Client
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
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlockId(xCoord, yCoord, zCoord));
        }
    }

    /**
     * Sends a rendering update packet to an area. Use to update orientation visually on all clients.
     */
    public void sendRenderPacket() {
        PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 100, worldObj.getWorldInfo().getDimension(), constructServerPacket132());
    }

    /**
     * Sends a rendering update packet to a specific player. Use to update orientation visually on a specific client.
     *
     * @param player Player to send packet to
     */
    public void sendRenderPacket(EntityPlayerMP player) {
        PacketDispatcher.sendPacketToPlayer(constructServerPacket132(), (Player)player);
    }

    /**
     * Helper method - constructs a Packet 132 for orientation updates from the server to the client.
     *
     * @return The packet to be sent
     */
    private Packet132TileEntityData constructServerPacket132() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("orientation", orientation.ordinal());
        return new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, nbt);
    }

    /**
     * Helper method - constructs a Packet 250 for requesting a render update packet.
     *
     * @return The packet to be sent
     */
    private Packet250CustomPayload constructClientUpdateRequestPacket() {
        String pName = Minecraft.getMinecraft().session.username;

        Packet250CustomPayload pkt = Packet250Helper.constructLocationWithString(xCoord, yCoord, zCoord, worldObj.getWorldInfo().getDimension(), pName);
        pkt.channel = "LE-Render";
        return pkt;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT && !initialRenderDone) {
            if (xCoord == 0 && yCoord == 0 && zCoord == 0) {
                if (failsafeTrip) {
                    requestRenderUpdate();
                    initialRenderDone = true;
                } else failsafeTrip = true; // Delay a bit.
            } else {
                requestRenderUpdate();
                initialRenderDone = true;
            }
        }
    }

    /**
     * Client helper - constructs and sends a packet asking for new render data.
     */
    private void requestRenderUpdate() {
        if (!worldObj.isRemote) return; // Client-only call
        PacketDispatcher.sendPacketToServer(constructClientUpdateRequestPacket());
    }

    /**
     * Reloading state from NBT, needs to update Position variable.
     *
     * @param nbt The NBT compound
     */
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        orientation = ForgeDirection.getOrientation(nbt.getShort("orientationOrdinal"));
        super.readFromNBT(nbt);
        updatePosition();
    }

    /**
     * Archiving state to NBT
     *
     * @param nbt The NBT compound
     */
    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setShort("orientationOrdinal", (short)orientation.ordinal());
        super.writeToNBT(nbt);
    }

}
