package io.sunstrike.api.liquidenergy.multiblock;

import ic2.api.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

/*
 * Tile
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
 * Base TileEntity class for multiblocks
 */
public abstract class Tile extends TileEntity implements IWrenchable {

    protected IStructure structure = null;
    protected ForgeDirection orientation = ForgeDirection.UP;

    protected int tex = 0;

    /**
     * Setter method for the structure this block is attached to
     *
     * @param structure IStructure object to attach to
     */
    public void setStructure(IStructure structure) {
        this.structure = structure;
    }

    /**
     * Getter method for the structure this block is attached to
     *
     * @return IStructure this is attached to
     */
    public IStructure getStructure() {
        return structure;
    }

    /**
     * Setter method for the direction this output faces (points out from output side)
     *
     * @param direction Direction to make the new orientation
     */
    public void setOrientation(ForgeDirection direction) {
        this.orientation = direction;
    }

    /**
     * Getter method for the current orientation
     *
     * @return The current block orientation
     */
    public ForgeDirection getOrientation() {
        return orientation;
    }

    @Override
    public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side) {
        if (side != orientation.ordinal()) return true;
        return false;
    }

    @Override
    public short getFacing() {
        return (short)orientation.ordinal();
    }

    @Override
    public void setFacing(short facing) {
        orientation = ForgeDirection.getOrientation((int)facing);
    }

    @Override
    public boolean wrenchCanRemove(EntityPlayer entityPlayer) {
        return true;
    }

    @Override
    public float getWrenchDropRate() {
        return 1.0f;
    }

    /**
     * Drops when removed with a wrench
     *
     * @param entityPlayer player using the wrench
     * @return ItemStack of what to drop
     */
    @Override
    public abstract ItemStack getWrenchDrop(EntityPlayer entityPlayer);

    public int getTexture(ForgeDirection side) {
        return tex;
    }

    public void debugInfo(EntityPlayer player) {
        // No debug info by default.
    }
}
