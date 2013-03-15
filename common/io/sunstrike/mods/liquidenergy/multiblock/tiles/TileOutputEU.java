package io.sunstrike.mods.liquidenergy.multiblock.tiles;

import ic2.api.Direction;
import ic2.api.energy.event.EnergyTileSourceEvent;
import ic2.api.energy.tile.IEnergySource;
import io.sunstrike.api.liquidenergy.multiblock.IC2Tile;
import io.sunstrike.mods.liquidenergy.configuration.ModObjects;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;

/*
 * TileOutputEU
 * io.sunstrike.mods.liquidenergy.multiblock.tiles
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
 * Output tile for Multiblocks - EU
 *
 * @author Sunstrike <sunstrike@azurenode.net>
 */
public class TileOutputEU extends IC2Tile implements IEnergySource {

    @Override
    public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
        return new ItemStack(ModObjects.blockInputEU);
    }

    @Override
    public int getMaxEnergyOutput() {
        return 32;
    }

    @Override
    public boolean emitsEnergyTo(TileEntity receiver, Direction direction) {
        if (direction.toForgeDirection() == orientation.getOpposite()) return true;
        return false;
    }

    /**
     * Emits power to the IC2 ENet
     *
     * @param power Power to try and dispatch
     * @return Power left over
     */
    public int emitEnergy(int power) {
        int excess = power - 32;
        if (excess <= 0) excess = 0;
        if (power >= 32) { // LV machine
            // We need to keep a handle to the event so we can get how much is actually consumed
            EnergyTileSourceEvent evt = new EnergyTileSourceEvent(this, 32);
            MinecraftForge.EVENT_BUS.post(evt);
            // Adjust buffer depending on amount actually emitted (evt.amount = 0 where all energy consumed by ENet)
            power -= 32 - evt.amount;
        }
        return excess + power;
    }

}
