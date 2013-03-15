package io.sunstrike.api.liquidenergy.multiblock;

import io.sunstrike.api.liquidenergy.Position;
import io.sunstrike.mods.liquidenergy.configuration.ModObjects;
import io.sunstrike.mods.liquidenergy.configuration.Settings;
import io.sunstrike.mods.liquidenergy.multiblock.MultiblockDescriptor;
import io.sunstrike.mods.liquidenergy.multiblock.tiles.TileOutputEU;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;

import java.util.Random;

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
    private MultiblockDescriptor structure;
    private IControlTile controller;
    private Phase currentPhase = Phase.FILLING;
    private int nvPowerBuffer = 0;
    private int ticks = 0;
    private Random random = new Random();

    public StructureHandler(IControlTile controller, MultiblockDescriptor structure) {
        this.controller = controller;
        this.structure = structure;
    }

    @Override
    public ILiquidTank getTank(LiquidStack type, ComponentDescriptor part) {
        if (structure.type == StructureType.TRANSFORMER_LIQUIFIER) {
            if (type.containsLiquid(new LiquidStack(Block.waterStill, 1)) && part == ComponentDescriptor.INPUT_FLUID) return internalTank;
            if (type.containsLiquid(new LiquidStack(ModObjects.itemLiquidNavitas, 1)) && part == ComponentDescriptor.OUTPUT_FLUID) return internalTank;
        } else {
            if (type.containsLiquid(new LiquidStack(ModObjects.itemLiquidNavitas, 1)) && part == ComponentDescriptor.OUTPUT_FLUID) return internalTank;
        }
        return null;
    }

    @Override
    public ILiquidTank[] getTanks(ComponentDescriptor part) {
        return new ILiquidTank[]{internalTank};
    }

    @Override
    public void debugInfo(EntityPlayer player) {
        player.addChatMessage("[StructureHandler] Phase: " + currentPhase);
        player.addChatMessage("[StructureHandler] Nv pBuffer: " + nvPowerBuffer);
        if (internalTank.getLiquid() != null) {
            player.addChatMessage("[StructureHandler] Tank liquid: " + internalTank.getLiquid().asItemStack());
            player.addChatMessage("[StructureHandler] Tank level: " + internalTank.getLiquid().amount);
        } else player.addChatMessage("[StructureHandler] No liquid in tank.");
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
                LiquidStack drainable = internalTank.drain(10, false);
                if (drainable != null) {
                    ((FluidTile) te).dump(internalTank.drain(drainable.amount, true), true);
                }
            }
        } else if (structure.type == StructureType.TRANSFORMER_GENERATOR && currentPhase == Phase.DRAINING) {
            Position tePos = structure.getComponent(ComponentDescriptor.OUTPUT_POWER_EU);
            TileEntity te = controller.getWorld().getBlockTileEntity(tePos.x, tePos.y, tePos.z);
            if (te instanceof TileOutputEU) {
                nvPowerBuffer = ((TileOutputEU)te).emitEnergy(nvPowerBuffer);
            }
        }

        if (currentPhase == Phase.DRAINING && internalTank.getLiquid() == null) currentPhase = Phase.FILLING;
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
        if (structure == null) return false;
        return structure.isValid();
    }

    private boolean verifyState() {
        if (!(checkStructure())) {
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
        // TANK
        NBTTagCompound tankNbt = nbt.getCompoundTag("tankLiquid");
        LiquidStack li = LiquidStack.loadLiquidStackFromNBT(tankNbt);
        if (li != null) internalTank.fill(li, true);
        // MISC. VARS
        nvPowerBuffer = nbt.getInteger("nvPowerBuffer");
        if (nbt.getString("phase") != "" && nbt.getString("phase") != null) currentPhase = Phase.valueOf(nbt.getString("phase"));

        verifyState();
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        // TANK
        NBTTagCompound tankNbt = new NBTTagCompound();
        LiquidStack tankLi = internalTank.getLiquid();
        if (tankLi != null) tankNbt = tankLi.writeToNBT(tankNbt);
        nbt.setCompoundTag("tankLiquid", tankNbt);
        // MISC. VARS
        nbt.setInteger("nvPowerBuffer", nvPowerBuffer);
        nbt.setString("phase", currentPhase.toString());
    }

    @Override
    public int demandsEU() {
        if (currentPhase != Phase.CHARGING) return 0;
        return (8000 - nvPowerBuffer)*Settings.euPerNv;
    }

    @Override
    public int receiveEU(int input) {
        if (currentPhase != Phase.CHARGING) return input;
        int spare = 0;
        if (input > 32) {
            spare = input - 32;
            input = 32;
        }
        // TODO: make this configurable.
        if (random.nextInt(50) == 1) { // 1-in-50
            input -= random.nextInt(input/8); // Up to an eighth loss
        }
        return Settings.euPerNv*chargeNv(input/Settings.euPerNv) + spare;
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
            int leftovers = nvPowerBuffer - 8000;
            nvPowerBuffer = 0;
            return leftovers;
        }
        return 0;
    }

}
