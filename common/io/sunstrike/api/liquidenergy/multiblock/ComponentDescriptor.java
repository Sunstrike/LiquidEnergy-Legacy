package io.sunstrike.api.liquidenergy.multiblock;

import io.sunstrike.mods.liquidenergy.configuration.ModObjects;
import io.sunstrike.mods.liquidenergy.configuration.Settings;
import net.minecraft.block.Block;

/*
 * ComponentDescriptor
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
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * @author Sunstrike <sunstrike@azurenode.net>
 */

/**
 * Possible parts of a multiblock structure
 *
 * @author Sunstrike <sunstrike@azurenode.net>
 */
public enum ComponentDescriptor {

    INTERNAL_TANK   (ModObjects.blockComponentTank, Settings.blockComponentTank),
    INPUT_POWER_EU  (ModObjects.blockInputEU, Settings.blockComponentTank),
    INPUT_POWER_MJ  (ModObjects.blockInputMJ, Settings.blockComponentTank),
    INPUT_FLUID     (ModObjects.blockInputFluid, Settings.blockComponentTank),
    OUTPUT_POWER_EU (ModObjects.blockOutputEU, Settings.blockComponentTank),
    OUTPUT_POWER_MJ (ModObjects.blockOutputMJ, Settings.blockComponentTank),
    OUTPUT_FLUID    (ModObjects.blockOutputFluid, Settings.blockComponentTank),
    STRUCTURE       (ModObjects.blockStructure, Settings.blockComponentTank);

    private final Block block;
    private final int id;

    ComponentDescriptor(Block bl, int id) {
        this.block = bl;
        this.id = id;
    }

    /**
     * Return the appropriate descriptor for a given block
     *
     * @param bl The block to get a descriptor for
     * @return The blocks descriptor or null if not valid
     */
    public static ComponentDescriptor getDescriptorForBlock(Block bl) {
        for (ComponentDescriptor c : ComponentDescriptor.values()) {
            if (c.getBlock() == bl) return c;
        }

        return null;
    }

    public static ComponentDescriptor getDescriptorForBlockID(int id) {
        for (ComponentDescriptor c : ComponentDescriptor.values()) {
            if (c.getId() == id) return c;
        }

        return null;
    }

    private Block getBlock() {
        return block;
    }

    private int getId() {
        return id;
    }
}
