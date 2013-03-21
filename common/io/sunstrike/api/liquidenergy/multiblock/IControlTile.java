package io.sunstrike.api.liquidenergy.multiblock;

import net.minecraftforge.liquids.LiquidStack;

/*
 * IControlTile
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
 * Multiblock control tile interface.
 *
 * @author Sunstrike <sunstrike@azurenode.net>
 */
public interface IControlTile {

    /**
     * Use to input liquid to the controller
     *
     * @param li LiquidStack to input
     * @param doFill Whether to commit the transaction
     * @return Amount used (initial - return = amount left)
     */
    public int receiveLiquid(LiquidStack li, boolean doFill);

    /**
     * Use to input power to the controller
     *
     * @param nvCharged Amount of Navitas to create with given power
     * @return Amount left over
     */
    public int receivePower(int nvCharged);

}
