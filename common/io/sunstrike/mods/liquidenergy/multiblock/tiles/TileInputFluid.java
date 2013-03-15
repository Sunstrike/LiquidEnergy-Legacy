package io.sunstrike.mods.liquidenergy.multiblock.tiles;

import io.sunstrike.api.liquidenergy.multiblock.ComponentDescriptor;
import io.sunstrike.api.liquidenergy.multiblock.FluidTile;
import io.sunstrike.mods.liquidenergy.LiquidEnergy;
import io.sunstrike.mods.liquidenergy.configuration.ModObjects;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.LiquidStack;

/*
 * TileInputFluid
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
 * Fluid Input tile entity
 *
 * @author Sunstrike <sunstrike@azurenode.net>
 */
public class TileInputFluid extends FluidTile {

    @Override
    public boolean canDumpOut() {
        return false;
    }

    @Override
    public int dump(LiquidStack resource, boolean doFill) {
        return 0;
    }

    @Override
    public int fill(ForgeDirection from, LiquidStack resource, boolean doFill) {
        if (structure == null) return 0;
        if (from == orientation) return structure.fill(resource, doFill); // Because this API is weird.
        return 0;
    }

    @Override
    public int fill(int tankIndex, LiquidStack resource, boolean doFill) {
        if (structure == null) return 0;
        return structure.fill(resource, doFill);
    }

    @Override
    public LiquidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return null;
    }

    @Override
    public LiquidStack drain(int tankIndex, int maxDrain, boolean doDrain) {
        return null;
    }

    @Override
    public ILiquidTank[] getTanks(ForgeDirection direction) {
        if (structure == null) return new ILiquidTank[0];
        return structure.getTanks(ComponentDescriptor.INPUT_FLUID);
    }

    @Override
    public ILiquidTank getTank(ForgeDirection direction, LiquidStack type) {
        if (structure == null) return null;
        return structure.getTank(type, ComponentDescriptor.INPUT_FLUID);
    }

    @Override
    public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
        return new ItemStack(ModObjects.blockInputFluid);
    }

    @Override
    public int getTexture(ForgeDirection side) {
        if (side == orientation) return 48; // Output
        return 49; // Side
    }

}
