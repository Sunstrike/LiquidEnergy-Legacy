package io.sunstrike.mods.liquidenergy.multiblock.tiles;

import ic2.api.Direction;
import ic2.api.energy.tile.IEnergySink;
import io.sunstrike.api.liquidenergy.multiblock.IC2Tile;
import io.sunstrike.mods.liquidenergy.LiquidEnergy;
import io.sunstrike.mods.liquidenergy.configuration.ModObjects;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

/*
 * TileInputEU
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
 * Input tile for Multiblocks - EU
 *
 * @author Sunstrike <sunstrike@azurenode.net>
 */
public class TileInputEU extends IC2Tile implements IEnergySink {

    @Override
    public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
        return new ItemStack(ModObjects.blockInputEU);
    }

    @Override
    public int getTexture(ForgeDirection side) {
        if (side == orientation) return 80; // Input
        return 81; // Side
    }

    @Override
    public int demandsEnergy() {
        // TODO: Implement
        return 0;
    }

    @Override
    public int injectEnergy(Direction directionFrom, int amount) {
        // TODO; Implement
        return amount;
    }

    @Override
    public int getMaxSafeInput() {
        return 32; // TODO: Make configurable
    }

    @Override
    public boolean acceptsEnergyFrom(TileEntity emitter, Direction direction) {
        return (direction.toForgeDirection() == orientation.getOpposite());
    }
}
