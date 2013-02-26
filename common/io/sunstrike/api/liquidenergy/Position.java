package io.sunstrike.api.liquidenergy;

import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

/*
 * Position
 * io.sunstrike.api.liquidenergy
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
 * Cleanroom Position class for storing coordinates in the world
 * </p>
 * This is a cleanroom implementation. The implementer has not seen Buildcraft's code.
 *
 * @author Sunstrike <sunstrike@azurenode.net>
 */
// Note: This is a clean-room implementation.
// I have not seen Buildcraft's Position class at time of writing.
public class Position {

    public int x, y, z;
    public World world;

    public Position(int x, int y, int z, World world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    public Position shiftInDirection(ForgeDirection dir) {
        Position p = new Position(x, y, z, world);
        switch (dir) {
            case UP:
                p.y++;
                break;
            case DOWN:
                p.y--;
                break;
            case NORTH:
                p.x++;
                break;
            case SOUTH:
                p.x--;
                break;
            case EAST:
                p.z++;
                break;
            case WEST:
                p.z--;
                break;
            default:
                break;
        }
        return p;
    }

}
