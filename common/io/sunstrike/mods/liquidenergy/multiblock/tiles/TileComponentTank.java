package io.sunstrike.mods.liquidenergy.multiblock.tiles;

import io.sunstrike.api.liquidenergy.Position;
import io.sunstrike.api.liquidenergy.multiblock.ComponentDescriptor;
import io.sunstrike.api.liquidenergy.multiblock.Tile;
import io.sunstrike.mods.liquidenergy.LiquidEnergy;
import io.sunstrike.mods.liquidenergy.configuration.ModObjects;
import io.sunstrike.mods.liquidenergy.helpers.MultiblockDiscoveryHelper;
import io.sunstrike.mods.liquidenergy.multiblock.MultiblockDescriptor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

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
 * Acts as root controller for a structure
 *
 * @author Sunstrike <sunstrike@azurenode.net>
 */
public class TileComponentTank extends Tile {

    private MultiblockDescriptor structure;
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
        if (structure != null) structureUpdate(); // Update structure
    }

    private boolean checkStructure() {
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
        // TODO: Implement
        super.debugInfo(player);
    }

    private boolean assembleStructure() {
        structure = MultiblockDiscoveryHelper.discoverTransformerStructure(getPosition(), ComponentDescriptor.INTERNAL_TANK);
        if (structure != null) return true;
        return false;
    }

    private void invalidateStructure() {
        LiquidEnergy.logger.info("[TileComponentTank] Destroying structure " + structure);
        Collection<Position> parts = structure.getAllComponents();
        for (Position p : parts) {
            TileEntity te = worldObj.getBlockTileEntity(p.x, p.y, p.z);
            if (te instanceof Tile) ((Tile) te).setController(null);
        }
    }

    /**
     * Entity update handler for the whole structure (called in updateEntity())
     */
    private void structureUpdate() {
        // TODO: Update whole structure
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
