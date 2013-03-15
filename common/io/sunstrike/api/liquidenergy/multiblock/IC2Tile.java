package io.sunstrike.api.liquidenergy.multiblock;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyTile;
import io.sunstrike.mods.liquidenergy.LiquidEnergy;
import net.minecraftforge.common.MinecraftForge;

/*
 * IC2Tile
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
 * Root class for IC2 energy tiles
 *
 * @author Sunstrike <sunstrike@azurenode.net>
 */
public abstract class IC2Tile extends Tile implements IEnergyTile {
// Sorry IC2 guys, IEnergyTile was all that'd work here despite being an internal API

    private boolean hasAddedToENet = false;

    @Override
    public void updateEntity() {
        super.updateEntity();
        // IC2 initialisation
        if (!worldObj.isRemote) {
            if (!hasAddedToENet) {
                LiquidEnergy.logger.info("Registering entity at " + this.xCoord + ", " + yCoord + ", " + zCoord + " with IC2 ENet.");
                MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
                hasAddedToENet = true;
            }
        }
    }

    // Invalidation/Unload for IC2 ENet
    @Override
    public void invalidate() {
        if (!worldObj.isRemote) {
            LiquidEnergy.logger.info("Deregistering entity at " + this.xCoord + ", " + yCoord + ", " + zCoord + " with IC2 ENet.");
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        }
        super.invalidate();
    }

    @Override
    public void onChunkUnload() {
        if (!worldObj.isRemote) {
            LiquidEnergy.logger.info("Deregistering entity at " + this.xCoord + ", " + yCoord + ", " + zCoord + " with IC2 ENet.");
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        }
        super.onChunkUnload();
    }

    @Override
    public boolean isAddedToEnergyNet() {
        return hasAddedToENet;
    }

}
