package io.sunstrike.mods.liquidenergy.multiblock.tiles;

import io.sunstrike.api.liquidenergy.Position;
import io.sunstrike.api.liquidenergy.multiblock.*;
import io.sunstrike.mods.liquidenergy.LiquidEnergy;
import io.sunstrike.mods.liquidenergy.configuration.ModObjects;
import io.sunstrike.mods.liquidenergy.helpers.MultiblockDiscoveryHelper;
import io.sunstrike.mods.liquidenergy.multiblock.MultiblockDescriptor;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;

import java.util.Collection;

/*
 * TileComponentTank
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
 * Multiblock tank TE
 * </p>
 * Acts as root controller for a structure (IControlTile)
 *
 * @author Sunstrike <sunstrike@azurenode.net>
 */
public class TileComponentTank extends Tile implements IControlTile {

    private enum Phase {FILLING, CHARGING, DRAINING}

    private MultiblockDescriptor structure;
    private Phase phase = Phase.FILLING;

    @Override
    public int receiveLiquid(LiquidStack li, boolean doFill) {
        if (phase != Phase.FILLING) return li.amount; // Wrong phase?
        if (structure.type == StructureType.TRANSFORMER_LIQUIFIER) {
            // Liquifier - Only accept Water
            if (li.containsLiquid(new LiquidStack(Block.waterStill, 1))) {
                return tank.fill(li, doFill);
            }
        } else {
            // Generator - Only accept Navitas
            if (li.containsLiquid(new LiquidStack(ModObjects.itemLiquidNavitas, 1))) {
                return tank.fill(li, doFill);
            }
        }
        return li.amount;
    }

    @Override
    public int receivePower(int nvCharged) {
        // TODO: Stub method
        return nvCharged;
    }

    private ILiquidTank tank = new LiquidTank(4000); // 4 buckets at a time
    private int nvPowerBuffer = 0;
    private int ticks = 0;

    @Override
    public void updateEntity() {
        super.updateEntity();
        ticks++;
        if (ticks >= 200) {
            if (structure == null)
                assembleStructure();
            else
                checkStructure();
            ticks = 0;
        }
        structureUpdate(); // Update structure
    }

    /**
     * Check if the structure is still valid.
     *
     * @return True if still valid, false otherwise.
     */
    private boolean checkStructure() {
        if (structure == null) return false;
        boolean valid = structure.isValid();
        if (!valid) {
            invalidateStructure();
            return false;
        }
        return true;
    }

    @Override
    public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
        return new ItemStack(ModObjects.blockComponentTank);
    }

    @Override
    public void debugInfo(EntityPlayer player) {
        player.addChatMessage("[TileComponentTank] Structure: " + structure);
        player.addChatMessage("[TileComponentTank] Phase: " + phase);
        LiquidStack li = tank.getLiquid();
        if (li != null) {
            player.addChatMessage("[TileComponentTank] Liquid item: " + li.asItemStack().getItem());
            player.addChatMessage("[TileComponentTank] Liquid level: " + li.amount + "/" + tank.getCapacity());
        }
        super.debugInfo(player);
    }

    /**
     * Attempt to construct a structure
     *
     * @return True if found and saved, false otherwise.
     */
    private boolean assembleStructure() {
        structure = MultiblockDiscoveryHelper.discoverTransformerStructure(getPosition(), ComponentDescriptor.INTERNAL_TANK);
        if (structure != null) return true;
        return false;
    }

    /**
     * Disable a structure (set all components controller to null)
     */
    private void invalidateStructure() {
        LiquidEnergy.logger.info("[TileComponentTank] Destroying structure " + structure);
        Collection<Position> parts = structure.getAllComponents();
        for (Position p : parts) {
            TileEntity te = worldObj.getBlockTileEntity(p.x, p.y, p.z);
            if (te instanceof Tile) ((Tile) te).setController(null);
        }
        structure = null;
    }

    /**
     * Entity update handler for the whole structure (called in updateEntity())
     */
    private void structureUpdate() {
        if (structure == null) return;
        // Hand off to phase-specific handlers
        if (structure.type == StructureType.TRANSFORMER_LIQUIFIER) {
            switch(phase) {
                case FILLING:
                    phaseFillingLiquifier();
                    break;
                case CHARGING:
                    phaseChargingLiquifier();
                    break;
                case DRAINING:
                    phaseDrainingLiquifier();
                    break;
            }
        } else { // TRANSFORMER_GENERATOR
            switch(phase) {
                case FILLING:
                    phaseFillingGenerator();
                    break;
                case CHARGING:
                    phaseChargingGenerator();
                    break;
                case DRAINING:
                    phaseDrainingGenerator();
                    break;
            }
        }
    }

    /**
     * Phase - Filling handler (Liquifier)
     */
    private void phaseFillingLiquifier() {
        LiquidStack li = tank.getLiquid();
        if (li != null && li.amount >= tank.getCapacity()) phase = Phase.CHARGING;
    }

    /**
     * Phase - Charging handler (Liquifier)
     */
    private void phaseChargingLiquifier() {
        if (nvPowerBuffer >= tank.getCapacity()) {
            phase = Phase.DRAINING;
            nvPowerBuffer = 0;
            // Convert tank
            tank = new LiquidTank(4000);
            tank.fill(new LiquidStack(ModObjects.itemLiquidNavitas, 4000), true);
        }
    }

    /**
     * Phase - Draining handler (Liquifier)
     */
    private void phaseDrainingLiquifier() {
        Position outPos = structure.getComponent(ComponentDescriptor.OUTPUT_FLUID);
        TileEntity ft = worldObj.getBlockTileEntity(outPos.x, outPos.y, outPos.z);
        if (ft instanceof FluidTile && ((FluidTile) ft).canDumpOut()) {
            int dumped = ((FluidTile) ft).dump(tank.drain(4, true), true);
            int ret = 4 - dumped;
            if (ret != 0) tank.fill(new LiquidStack(ModObjects.itemLiquidNavitas, ret), true);
        }

        if (tank.getLiquid() == null) {
            phase = Phase.FILLING;
            // Reset tank
            tank = new LiquidTank(4000);
        }
    }

    /**
     * Phase - Filling handler (Generator)
     */
    private void phaseFillingGenerator() {
        // TODO: Implement
        LiquidEnergy.logger.severe("[TileComponentTank] Generator phase FILLING not implemented!");
    }

    /**
     * Phase - Charging handler (Generator)
     */
    private void phaseChargingGenerator() {
        // TODO: Implement
        LiquidEnergy.logger.severe("[TileComponentTank] Generator phase CHARGING not implemented!");
    }

    /**
     * Phase - Draining handler (Generator)
     */
    private void phaseDrainingGenerator() {
        // TODO: Implement
        LiquidEnergy.logger.severe("[TileComponentTank] Generator phase DRAINING not implemented!");
    }

    @Override
    public int getTexture(ForgeDirection side) {
        if (side == orientation) return 64;
        return 65;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        // TODO: State restoration
        super.readFromNBT(nbt);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        // TODO: State archival
        super.writeToNBT(nbt);
    }

}
