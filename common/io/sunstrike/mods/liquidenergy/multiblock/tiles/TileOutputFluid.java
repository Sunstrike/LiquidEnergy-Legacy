package io.sunstrike.mods.liquidenergy.multiblock.tiles;

import io.sunstrike.api.liquidenergy.multiblock.FluidTile;
import io.sunstrike.mods.liquidenergy.configuration.ModObjects;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.LiquidStack;

/*
 * TileOutputFluid
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
 * Fluid output TE for BlockOutputFluid
 *
 * @author Sunstrike <sunstrike@azurenode.net>
 */
public class TileOutputFluid extends FluidTile {

    @Override
    public boolean canDumpOut() {
        return true;
    }

    @Override
    public int dump(LiquidStack resource, boolean doFill) {
        return 0;
    }

    @Override
    public int fill(ForgeDirection from, LiquidStack resource, boolean doFill) {
        return 0;
    }

    @Override
    public int fill(int tankIndex, LiquidStack resource, boolean doFill) {
        return 0;
    }

    @Override
    public LiquidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        if (from == this.orientation.getOpposite()) return structure.drain(maxDrain, doDrain);
        return null;
    }

    @Override
    public LiquidStack drain(int tankIndex, int maxDrain, boolean doDrain) {
        return null;
    }

    @Override
    public ILiquidTank[] getTanks(ForgeDirection direction) {
        return new ILiquidTank[0];
    }

    @Override
    public ILiquidTank getTank(ForgeDirection direction, LiquidStack type) {
        return null;
    }

    @Override
    public void debugInfo(EntityPlayer player) {
        player.addChatMessage("[TileOutputFluid] Orientation: " + orientation);
        if (structure == null) return;
        player.addChatMessage("[TileOutputFluid] IStructure: " + structure);
    }

    @Override
    public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
        return new ItemStack(ModObjects.blockOutputFluid);
    }

    @Override
    public int getTexture(ForgeDirection side) {
        if (side == orientation) return 32; // Output
        if (side == orientation.getOpposite()) return 34; // Input
        return 33; // Side
    }

}
