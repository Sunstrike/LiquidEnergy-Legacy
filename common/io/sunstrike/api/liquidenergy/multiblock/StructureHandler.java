package io.sunstrike.api.liquidenergy.multiblock;

import io.sunstrike.api.liquidenergy.Position;
import io.sunstrike.mods.liquidenergy.configuration.ModObjects;
import io.sunstrike.mods.liquidenergy.multiblock.MultiblockDescriptor;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;

/*
 * StructureHandler
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
 * Root class for all structure handlers (e.g. Generators/Liquifiers)
 *
 * @author Sunstrike <sunstrike@azurenode.net>
 */
public class StructureHandler implements IStructure {

    private enum Phase { FILLING, CHARGING, DRAINING; }

    private ILiquidTank internalTank = new LiquidTank(8000);
    private MultiblockDescriptor structure = null;
    private IControlTile controller;
    private Phase currentPhase = Phase.FILLING;
    private int nvPowerBuffer = 0;
    private int ticks = 0;

    public StructureHandler(IControlTile controller, MultiblockDescriptor structure) {
        this.controller = controller;
        this.structure = structure;
    }

    public void update() {
        // Verify structure
        ticks++;
        if (ticks >= 100) {
            if (verifyState() == false) return; // Short circuit to close
            ticks = 0;
        }

        // Attempt to dump
        if (structure.type == StructureType.TRANSFORMER_LIQUIFIER && currentPhase == Phase.DRAINING) {
            Position tePos = structure.getComponent(ComponentDescriptor.OUTPUT_FLUID);
            TileEntity te = controller.getWorld().getBlockTileEntity(tePos.x, tePos.y, tePos.z);
            if (te instanceof FluidTile && ((FluidTile) te).canDumpOut()) {
                int left = ((FluidTile)te).dump(internalTank.drain(10, false), false);
                int toDrain = 10 - left;
                ((FluidTile)te).dump(internalTank.drain(toDrain, true), true);
            }
        }
    }

    @Override
    public MultiblockDescriptor getStructureDescriptor() {
        return structure;
    }

    @Override
    public void setStructureDescriptor(MultiblockDescriptor descriptor) {
        structure = descriptor;
    }

    @Override
    public boolean checkStructure() {
        return structure.isValid();
    }

    private boolean verifyState() {
        if (structure == null || !(checkStructure())) {
            breakStructure();
            return false;
        }
        return true;
    }

    private void breakStructure() {
        controller.invalidateStructure();
    }

    public int fill(LiquidStack resource, boolean doFill) {
        if (currentPhase == Phase.FILLING) {
            int ret = internalTank.fill(resource, doFill);
            if (internalTank.fill(new LiquidStack(Block.waterStill, internalTank.getCapacity()), false) == 0) {
                // Tank is full
                currentPhase = Phase.CHARGING;
            }
            return ret;
        }
        return 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        // TODO: Stub method

    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        // TODO: State saving done better
        NBTTagCompound tankNbt = new NBTTagCompound();
        LiquidStack tankLi = internalTank.getLiquid();
        if (tankLi != null) tankNbt = tankLi.writeToNBT(tankNbt);
        nbt.setCompoundTag("tankLiquid", tankNbt);

    }

    /**
     * Add charge to the system
     *
     * @param nvCharged Amount of Nv the power input can charge
     * @return Amount of Nv worth of power was unused
     */
    public int chargeNv(int nvCharged) {
        if (currentPhase != Phase.CHARGING) return nvCharged; // Do nothing, accept no power
        nvPowerBuffer += nvCharged;
        if (nvPowerBuffer >= 8000) {
            internalTank.drain(8000, true); // clear tank
            internalTank.fill(new LiquidStack(ModObjects.itemLiquidNavitas, 8000), true);
            currentPhase = Phase.DRAINING;
            // Left over power
            return nvPowerBuffer - 8000;
        }
        return 0;
    }

}
