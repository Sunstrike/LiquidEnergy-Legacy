package io.sunstrike.mods.liquidenergy.multiblock.tiles;

import io.sunstrike.api.liquidenergy.multiblock.FluidTile;
import io.sunstrike.mods.liquidenergy.configuration.ModObjects;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;

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

    private ILiquidTank tank = new LiquidTank(1000);
    private int drainPerTick = 5; // How much should we move to the controller per tick?

    @Override
    public void updateEntity() {
        LiquidStack li = tank.getLiquid();
        if (controller != null && li != null) {
            int spare = controller.receiveLiquid(tank.drain(drainPerTick, false), false);
            controller.receiveLiquid(tank.drain(drainPerTick - spare, true), true);
        }
        super.updateEntity();
    }

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
        if (from == orientation && resource.containsLiquid(new LiquidStack(Block.waterStill, 0))) return tank.fill(resource, doFill);
        return 0;
    }

    @Override
    public int fill(int tankIndex, LiquidStack resource, boolean doFill) {
        if (resource.containsLiquid(new LiquidStack(Block.waterStill, 1))) return tank.fill(resource, doFill);
        return 0;
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
        return new ILiquidTank[]{tank};
    }

    @Override
    public ILiquidTank getTank(ForgeDirection direction, LiquidStack type) {
        if (direction == orientation && type.containsLiquid(new LiquidStack(Block.waterStill, 0))) return tank;
        return null;
    }

    @Override
    public void debugInfo(EntityPlayer player) {
        LiquidStack li = tank.getLiquid();
        if (li != null) {
            player.addChatMessage("[TileInputFluid] Liquid item: " + li.asItemStack().getItem());
            player.addChatMessage("[TileInputFluid] Liquid level: " + li.amount + "/" + tank.getCapacity());
        }
        super.debugInfo(player);
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
