package io.sunstrike.mods.liquidenergy.multiblock;

import io.sunstrike.mods.liquidenergy.helpers.Position;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

/**
 * MultiblockDescriptor
 * io.sunstrike.mods.liquidenergy.multiblock
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

public class MultiblockDescriptor {

    public int x, y, z;
    public ForgeDirection orientation;
    public StructureType type;
    public World world;

    /**
     * Constructor
     *
     * @param x X coord of core tank
     * @param y Y coord of core tank
     * @param z Z coord of core tank
     * @param orientation Direction of structure (pointing from tank to output)
     * @param type The type of Multiblock structure
     * @param world The world this structure is in
     */
    public MultiblockDescriptor(int x, int y, int z, ForgeDirection orientation, StructureType type, World world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.orientation = orientation;
        this.world = world;
    }

    public Position getComponent(ComponentDescriptor descriptor) {
        switch (descriptor) {
            case INTERNAL_TANK:
                return getInternalTankPos(descriptor);
            case INPUT_POWER_EU:
            case INPUT_POWER_MJ:
                return getPowerInputPos(descriptor);
            case OUTPUT_POWER_EU:
            case OUTPUT_POWER_MJ:
                return getPowerOutputPos(descriptor);
            case INPUT_FLUID:
                return getFluidInputPos(descriptor);
            case OUTPUT_FLUID:
                return getFluidOutputPos(descriptor);
            default:
                return null; // Invalid descriptor
        }
    }

    private Position getInternalTankPos(ComponentDescriptor descriptor) {
        // INTERNAL_TANK is always the center of the structure (and where this object references)
        return new Position(x, y, z, world);
    }

    private Position getPowerInputPos(ComponentDescriptor descriptor) {
        // TODO: Stub method
        return null;
    }

    private Position getPowerOutputPos(ComponentDescriptor descriptor) {
        // TODO: Stub method
        return null;
    }

    private Position getFluidInputPos(ComponentDescriptor descriptor) {
        // TODO: Stub method
        return null;
    }

    private Position getFluidOutputPos(ComponentDescriptor descriptor) {
        // TODO: Stub method
        return null;
    }

}
