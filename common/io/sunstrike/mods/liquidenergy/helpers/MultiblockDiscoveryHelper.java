package io.sunstrike.mods.liquidenergy.helpers;

import com.google.common.collect.ArrayListMultimap;
import io.sunstrike.mods.liquidenergy.configuration.Settings;
import io.sunstrike.mods.liquidenergy.multiblock.ComponentDescriptor;
import io.sunstrike.mods.liquidenergy.multiblock.MultiblockDescriptor;
import io.sunstrike.mods.liquidenergy.multiblock.StructureType;
import io.sunstrike.mods.liquidenergy.multiblock.blocks.*;
import net.minecraft.block.Block;
import net.minecraftforge.common.ForgeDirection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
 *
 * @author Sunstrike <sunstrike@azurenode.net>
 */
public class MultiblockDiscoveryHelper {

    /**
     * Public method to attempt creation of a valid Multiblock structure
     *
     * @param location The position to start checking
     * @param caller The block calling for the check (instance of a multiblock component)
     * @return A MultiblockDescriptor of a complete structure or null if one could not be built
     */
    public static MultiblockDescriptor discoverTransformerStructure(Position location, Block caller) {
        if (caller instanceof BlockComponentTank) {
            return __discoverTransformerFromTank(location);
        } else if (caller instanceof BlockInputEU || caller instanceof BlockInputMJ || caller instanceof BlockInputFluid) {
            return __discoverTransformerFromInput(location);
        } else if (caller instanceof BlockOutputEU || caller instanceof BlockOutputMJ || caller instanceof BlockOutputFluid) {
            return __discoverTransformerFromOutput(location);
        } else {
            return null;
        }
    }

    /**
     * Private helper: Discover a transformer structure from a given position
     * @param location Output block to start with
     * @return A MultiblockDescriptor of a complete structure or null if one could not be built
     */
    private static MultiblockDescriptor __discoverTransformerFromOutput(Position location) {
        // Find a nearby tank and invoke that with __discoverTransformerFromTank
        ForgeDirection[] dirs = ForgeDirection.values();
        MultiblockDescriptor result = null;
        for (ForgeDirection d : dirs) {
            Position toCheck = location.shiftInDirection(d);
            int bID = toCheck.world.getBlockId(toCheck.x, toCheck.y, toCheck.z);
            if (bID != Settings.blockComponentTank) return null;
            result = __discoverTransformerFromTank(toCheck);
            if (result != null) break;
        }

        return result;
    }

    /**
     * Private helper: Discover a transformer structure from a given position
     * @param location Input block to start with
     * @return A MultiblockDescriptor of a complete structure or null if one could not be built
     */
    private static MultiblockDescriptor __discoverTransformerFromInput(Position location) {
        MultiblockDescriptor result = null;
        for (int x = -1; x > 1; x++) {
            for (int y = -1; y > 1; y++) {
                for (int z = -1; z > 1; z++) {
                    if (location.world.getBlockId(location.x + x, location.y + y, location.z + z) == Settings.blockComponentTank) {
                        result = __discoverTransformerFromTank(new Position(location.x + x, location.y + y, location.z + z, location.world));
                        if (result != null) break;
                    }
                }
            }
        }
        return result;
    }

    /**
     * Private helper: Discover a transformer structure from a given position
     * @param posi Tank block to start with
     * @return A MultiblockDescriptor of a complete structure or null if one could not be built
     */
    private static MultiblockDescriptor __discoverTransformerFromTank(Position posi) {
        ArrayListMultimap<ComponentDescriptor, Position> parts = ArrayListMultimap.create();
        parts.put(ComponentDescriptor.INTERNAL_TANK, posi);
        StructureType type = null;
        ForgeDirection direction = null;

        // Find Output block
        for (ForgeDirection dir : ForgeDirection.values()) {
            Position shifted = posi.shiftInDirection(dir);

            int bID = shifted.world.getBlockId(shifted.x, shifted.y, shifted.z);
            if (bID == Settings.blockOutputEU) {
                parts.put(ComponentDescriptor.OUTPUT_POWER_EU, shifted);
                type = StructureType.TRANSFORMER_GENERATOR;
                direction = dir;
                break;
            } else if (bID == Settings.blockOutputMJ) {
                parts.put(ComponentDescriptor.OUTPUT_POWER_MJ, shifted);
                type = StructureType.TRANSFORMER_GENERATOR;
                direction = dir;
                break;
            } else if (bID == Settings.blockOutputFluid) {
                parts.put(ComponentDescriptor.OUTPUT_FLUID, shifted);
                type = StructureType.TRANSFORMER_LIQUIFIER;
                direction = dir;
                break;
            }
        }

        // No blocks are found if we have no direction
        if (direction == null) return null;

        // Find structure blocks
        ArrayList<ForgeDirection> dirs = new ArrayList(Arrays.asList(ForgeDirection.values()));
        // Remove known-impossible locations (direction of output and the opposite)
        dirs.remove(direction);
        dirs.remove(direction.getOpposite());
        ForgeDirection struc1 = null;
        for (ForgeDirection dir : dirs) {
            Position shifted = posi.shiftInDirection(dir);

            int bID = shifted.world.getBlockId(shifted.x, shifted.y, shifted.z);
            if (bID == Settings.blockStructure) {
                parts.put(ComponentDescriptor.STRUCTURE, shifted);
                struc1 = dir;
                break;
            }
        }

        // Nothing found
        if (struc1 == null) return null;

        Position p = posi.shiftInDirection(struc1.getOpposite());
        if (p.world.getBlockId(p.x, p.y, p.z) != Settings.blockStructure) return null;
        parts.put(ComponentDescriptor.STRUCTURE, p);

        List<Position> structs = parts.get(ComponentDescriptor.STRUCTURE);
        ArrayList<ComponentDescriptor> validInputs = new ArrayList<ComponentDescriptor>();
        if (type == StructureType.TRANSFORMER_LIQUIFIER) {
            // Liquifiers need power and fluid inputs
            validInputs.add(ComponentDescriptor.INPUT_FLUID);
            validInputs.add(ComponentDescriptor.INPUT_POWER_EU);
            validInputs.add(ComponentDescriptor.INPUT_POWER_MJ);
        } else if (type == StructureType.TRANSFORMER_GENERATOR) {
            validInputs.add(ComponentDescriptor.INPUT_FLUID);
            validInputs.add(ComponentDescriptor.STRUCTURE);
        } else {
            return null; // Invalid state
        }

        for (Position structPos : structs) {
            // Check for the inputs
            Position potential = structPos.shiftInDirection(direction.getOpposite());
            int bID = potential.world.getBlockId(potential.x, potential.y, potential.z);
            ComponentDescriptor desc = ComponentDescriptor.getDescriptorForBlockID(bID);
            if (desc == null || !validInputs.contains(desc)) return null;
            parts.put(desc, potential);

            // Clean up valid targets for next iteration
            if (type == StructureType.TRANSFORMER_GENERATOR) {
                validInputs.remove(desc);
            } else {
                if (desc == ComponentDescriptor.INPUT_FLUID) {
                    // Only ONE fluid input
                    validInputs.remove(ComponentDescriptor.INPUT_FLUID);
                } else {
                    // Only ONE power input
                    validInputs.remove(ComponentDescriptor.INPUT_POWER_EU);
                    validInputs.remove(ComponentDescriptor.INPUT_POWER_MJ);
                }
            }
        }

        return new MultiblockDescriptor(parts, type);
    }

}
