package io.sunstrike.api.liquidenergy.multiblock;

import io.sunstrike.mods.liquidenergy.multiblock.MultiblockDescriptor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.LiquidStack;

/*
 * IStructure
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
 * Interface for valid structure objects (Liquifiers and Generators)
 * </p>
 * All methods must be implemented for structures, and all structures must store a reference to the MultiblockDescriptor
 * that constructs the structure for later verification. How this is done may depend on the implementation.
 *
 * @author Sunstrike <sunstrike@azurenode.net>
 */
public interface IStructure {

    /**
     * Called on the root TEs entity update ticks
     * </p>
     * Use this to perform any continuous actions such as requesting pipe dumps
     */
    public void update();

    /**
     * Gets this structures descriptor
     *
     * @return A MultiblockDescriptor representing the physical structure
     */
    public MultiblockDescriptor getStructureDescriptor();

    /**
     * Set this structures descriptor
     *
     * @param descriptor Descriptor to set
     */
    public void setStructureDescriptor(MultiblockDescriptor descriptor);

    /**
     * Verify the structure is still valid using the internal descriptor
     * </p>
     * This should not be called often to avoid any lag associated with world access.
     * Call MultiblockDescriptor.isValid() to check. If invalid, break the structure and clean up.
     * </p>
     * Call deregisterStructure() on each component to remove references and delete the structure.
     */
    public boolean checkStructure();

    /**
     * ITankContainer wrapper; see net.minecraftforge.liquids.ITankContainer (ForgeDirection from is handled in the
     * input/output TEs)
     *
     * @param resource LiquidStack to fill with
     * @param doFill Whether or not to commit the operation
     * @return Amount filled into internal tanks
     */
    public int fill(LiquidStack resource, boolean doFill);

    /**
     * Restore state from NBT from save data
     *
     * @param nbt NBT tag compound to write from
     */
    public void readFromNBT(NBTTagCompound nbt);

    /**
     * Store state in NBT for saving
     *
     * @param nbt NBT tag compound to write to
     */
    public void writeToNBT(NBTTagCompound nbt);

}
