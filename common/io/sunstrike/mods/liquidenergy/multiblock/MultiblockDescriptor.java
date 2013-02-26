package io.sunstrike.mods.liquidenergy.multiblock;

import com.google.common.collect.ArrayListMultimap;
import io.sunstrike.api.liquidenergy.Position;
import io.sunstrike.api.liquidenergy.multiblock.ComponentDescriptor;
import io.sunstrike.api.liquidenergy.multiblock.StructureType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/*
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

/**
 * A class that describes an active multiblock structure
 *
 * @author Sunstrike <sunstrike@azurenode.net>
 */
public class MultiblockDescriptor {

    public StructureType type;
    private ArrayListMultimap<ComponentDescriptor, Position> parts;

    /**
     * Constructor
     *
     * @param parts Hash set as generated by the discovery helper
     * @param type Type of structure
     */
    public MultiblockDescriptor(ArrayListMultimap<ComponentDescriptor, Position> parts, StructureType type) {
        this.parts = parts;
        this.type = type;
    }

    public Position getComponent(ComponentDescriptor descriptor) {
        try {
            List l = parts.get(descriptor);
            return (Position)l.get(0);
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Checks if the described structure is still complete and valid
     *
     * @return True if structure is valid.
     */
    public boolean isValid() {
        boolean valid = true;

        // We'll handle structure blocks seperately
        Collection<Map.Entry<ComponentDescriptor, Position>> mapCopy = parts.entries();
        ArrayList<Position> structBlocks = new ArrayList();
        for (Map.Entry e : mapCopy) {
            if (e.getKey() == ComponentDescriptor.STRUCTURE) {
                mapCopy.remove(e);
                structBlocks.add((Position)e.getValue());
            }
        }

        // Check TE'd blocks (non-structure)
        for (Position p : parts.values()) {
            if (!p.world.blockExists(p.x, p.y, p.z) || !p.world.blockHasTileEntity(p.x, p.y, p.z)) return false;
        }

        // Check structure blocks
        for (Position p : parts.values()) {
            if (!p.world.blockExists(p.x, p.y, p.z) || p.world.blockHasTileEntity(p.x, p.y, p.z)) return false;
        }

        return true;
    }

}