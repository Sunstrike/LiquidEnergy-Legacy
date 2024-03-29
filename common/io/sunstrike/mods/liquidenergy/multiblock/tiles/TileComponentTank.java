package io.sunstrike.mods.liquidenergy.multiblock.tiles;

import io.sunstrike.api.liquidenergy.Position;
import io.sunstrike.api.liquidenergy.multiblock.*;
import io.sunstrike.mods.liquidenergy.LiquidEnergy;
import io.sunstrike.mods.liquidenergy.configuration.ModObjects;
import io.sunstrike.mods.liquidenergy.helpers.MultiblockDiscoveryHelper;
import io.sunstrike.mods.liquidenergy.multiblock.MultiblockDescriptor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
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
 *
 * @author Sunstrike <sunstrike@azurenode.net>
 */
public class TileComponentTank extends Tile implements IControlTile {

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (structure != null) structure.update();
    }

    @Override
    public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
        return new ItemStack(ModObjects.blockComponentTank);
    }

    @Override
    public void debugInfo(EntityPlayer player) {
        // Attempt to make a structure
        if (structure == null)
            this.assembleStructure();
        else
            structure.checkStructure();
        super.debugInfo(player);
    }

    @Override
    public boolean assembleStructure() {
        MultiblockDescriptor desc = MultiblockDiscoveryHelper.discoverTransformerStructure(new Position(xCoord, yCoord, zCoord, worldObj), ComponentDescriptor.INTERNAL_TANK);
        if (desc != null) {
            structure = new StructureHandler(this, desc);
            Collection<Position> parts = desc.getAllComponents();
            for (Position p : parts) {
                TileEntity te = worldObj.getBlockTileEntity(p.x, p.y, p.z);
                if (te instanceof Tile) ((Tile) te).setStructure(structure);
            }
            return true;
        }
        return false;
    }

    @Override
    public void invalidateStructure() {
        LiquidEnergy.logger.info("Destroying structure " + structure);
        MultiblockDescriptor desc = structure.getStructureDescriptor();
        Collection<Position> parts = desc.getAllComponents();
        for (Position p : parts) {
            TileEntity te = worldObj.getBlockTileEntity(p.x, p.y, p.z);
            if (te instanceof Tile) ((Tile) te).setStructure(null);
        }
    }

    @Override
    public World getWorld() {
        return worldObj;
    }

    @Override
    public int getTexture(ForgeDirection side) {
        if (side == orientation) return 64;
        return 65;
    }

}
