package io.sunstrike.api.liquidenergy.multiblock;

import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidStack;

/*
 * FluidTile
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
 * Abstract to be implemented in all fluid blocks for IStructure structures.
 * </p>
 * Fluid blocks must also implement ITankContainer but redirect these calls to the IStructure object, else do nothing
 * if no structure is attached (central tank handles fluids instead of the input tiles).
 *
 * @author Sunstrike <sunstrike@azurenode.net>
 */
public abstract class FluidTile extends Tile implements ITankContainer {

    /**
     * Tell the caller if this can be used as an output
     *
     * @return True if can act as an output, else false.
     */
    public abstract boolean canDumpOut();

    /**
     * Attempt to dump liquids out of this output (nullable)
     *
     * @param resource LiquidStack to attempt to eject
     * @param doFill Whether or not to commit the action
     * @return Amount dumped to an ITankContainer
     */
    public abstract int dump(LiquidStack resource, boolean doFill);

    /**
     * Add liquid to dump queue tank
     *
     * @param resource LiquidStack to attempt to eject
     * @param doFill Whether or not to commit the action
     * @return Amount dumped into internal tank
     */
    public abstract int addToDumpQueue(LiquidStack resource, boolean doFill);

}
