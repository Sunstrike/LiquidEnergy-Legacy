package io.sunstrike.mods.liquidenergy.helpers;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import io.sunstrike.api.liquidenergy.multiblock.Tile;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

/*
 * PacketHandler
 * io.sunstrike.mods.liquidenergy.helpers
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
 * Master packet250 packet handler (Server-side).
 *
 * @author Sunstrike <sunstrike@azurenode.net>
 */
public class PacketHandler implements IPacketHandler {

    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload pkt, Player player) {
        if (pkt.channel == "LE-Render" && FMLCommonHandler.instance().getEffectiveSide() != Side.CLIENT) {
            // Entity render update packet
            handleRenderRequestPacket(pkt);
        }
    }

    private void handleRenderRequestPacket(Packet250CustomPayload pkt) {
        Object[] pktContents = Packet250Helper.parseLocationWithString(pkt);
        int[] location = (int[])pktContents[0];
        String pName = (String)pktContents[1];

        // Try and get world
        WorldServer world = DimensionManager.getWorld(location[3]);
        if (world == null) return;
        TileEntity te = world.getBlockTileEntity(location[0], location[1], location[2]);
        if (!(te instanceof Tile)) return; // Not one of ours/invalid packet

        // Try and get player
        EntityPlayerMP player = MinecraftServer.getServerConfigurationManager(MinecraftServer.getServer()).getPlayerForUsername(pName);
        if (player == null) {
            // Fall back to full update
            ((Tile) te).sendRenderPacket();
        } else {
            // Send player-specific update
            ((Tile) te).sendRenderPacket(player);
        }
    }

}
