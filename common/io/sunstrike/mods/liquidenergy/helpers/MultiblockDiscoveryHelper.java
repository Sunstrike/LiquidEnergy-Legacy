package io.sunstrike.mods.liquidenergy.helpers;

import io.sunstrike.mods.liquidenergy.multiblock.blocks.*;
import io.sunstrike.mods.liquidenergy.multiblock.MultiblockDescriptor;
import net.minecraft.block.Block;
import net.minecraft.world.World;

/*
 * MultiblockDiscoveryHelper
 * io.sunstrike.mods.liquidenergy.helpers
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
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * @author Sunstrike <sunstrike@azurenode.net>
 */

/**
 * Helper for finding and constructing multiblock instances
 */
public class MultiblockDiscoveryHelper {

    public static MultiblockDescriptor discoverTransformerStructure(int x, int y, int z, World world, Block caller) {
        if (caller instanceof BlockComponentTank) {
            return __discoverTransformerFromTank(x, y, z, world);
        } else if (caller instanceof BlockInputEU || caller instanceof BlockInputMJ || caller instanceof BlockInputFluid) {
            return __discoverTransformerFromInput(x, y, z, world);
        } else if (caller instanceof BlockOutputEU || caller instanceof BlockOutputMJ || caller instanceof BlockOutputFluid) {
            return __discoverTransformerFromOutput(x, y, z, world);
        } else {
            return null;
        }
    }

    private static MultiblockDescriptor __discoverTransformerFromOutput(int x, int y, int z, World world) {
        // TODO: Stub method
        return null;
    }

    private static MultiblockDescriptor __discoverTransformerFromInput(int x, int y, int z, World world) {
        // TODO: Stub method
        return null;
    }

    private static MultiblockDescriptor __discoverTransformerFromTank(int x, int y, int z, World world) {
        // TODO: Stub method
        return null;
    }

}
