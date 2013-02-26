package io.sunstrike.mods.liquidenergy.blocks.tiles;

import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.transport.IPipeConnection;
import io.sunstrike.mods.liquidenergy.LiquidEnergy;
import io.sunstrike.mods.liquidenergy.configuration.ModObjects;
import io.sunstrike.mods.liquidenergy.configuration.Settings;
import io.sunstrike.mods.liquidenergy.helpers.LEPowerProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;

/*
 * TileGeneratorEU
 * io.sunstrike.mods.liquidenergy.blocks.tiles
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
 * MJ Generator tile entity
 *
 * @author Sunstrike <sunstrike@azurenode.net>
 */
public class TileGeneratorMJ extends TileEntity implements ITankContainer, IPowerReceptor, IPipeConnection {

	// Internal buffers
	private final LiquidTank inputBuffer = new LiquidTank(8000);
	private IPowerProvider powerProvider = new LEPowerProvider();
    private float powerBuffer = 0;
    private final float maxPowerInBuffer = 990;

    // Valid directions for power out
    private final ForgeDirection[] outputDirections = new ForgeDirection[]{ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.EAST, ForgeDirection.WEST};

	public TileGeneratorMJ() {
		//LiquidEnergy.logger.info("Created TE Generator EU.");
		inputBuffer.setTankPressure(-100);
        powerProvider.configure(0, 0, 0, 0, (int)maxPowerInBuffer);
	}

    @Override
    public void updateEntity() {
        // Generate power from NV
        int spaceNv = powerBufferSpaceNv();
        if (spaceNv > 0) {
            int out = 0;
            LiquidStack li = inputBuffer.drain(spaceNv, true);
            if (li != null) out = li.amount;
            powerBuffer =+ out * Settings.mjPerNv;
            if (powerBuffer > maxPowerInBuffer) powerBuffer = maxPowerInBuffer;
        }

        // Sanity check
        if (powerBuffer < 0) powerBuffer = 0;
        if (powerBuffer <= 0) return;

        // Try and dump MJ into power system (any side except UP and DOWN)
        // 4 MJ/t max.
        int maxPowerOutMJTick = 4;
        float thisRun = powerBuffer >= maxPowerOutMJTick ? maxPowerOutMJTick : (int)powerBuffer;
        float beforeWork = thisRun;
        if (thisRun == 0) return;
        for (ForgeDirection d : outputDirections) {
            // Short circuit (just in case)
            if (thisRun <= 0) continue;

            // y is always constant for sides
            int x = xCoord, z = zCoord;

            switch (d) {
                case NORTH:
                    x++;
                    break;
                case EAST:
                    z++;
                    break;
                case SOUTH:
                    x--;
                    break;
                case WEST:
                    z--;
                    break;
            }

            TileEntity te = worldObj.getBlockTileEntity(x, yCoord, z);
            if (te == null || !(te instanceof IPowerReceptor)) continue;
            IPowerProvider prov = ((IPowerReceptor) te).getPowerProvider();
            if (prov == null) continue;
            int max = prov.getMaxEnergyReceived(), min = prov.getMinEnergyReceived();
            if (thisRun < min) continue;
            if (thisRun <= max) {
                prov.receiveEnergy(thisRun, d.getOpposite());
                thisRun = 0;
                break;
            } else {
                prov.receiveEnergy(max, d.getOpposite());
                thisRun =- max;
            }
        }
        powerBuffer =- beforeWork - thisRun;
    }

    private int powerBufferSpaceNv() {
        float space = maxPowerInBuffer - powerBuffer;
        if (space <= Settings.mjPerNv) return 0;
        int capacity = (int)space / Settings.mjPerNv;
        return capacity >= 4 ? 4 : capacity;
    }

    @Override
    public boolean isPipeConnected(ForgeDirection with) {
        return true;
    }

    @Override
    public int fill(ForgeDirection from, LiquidStack resource, boolean doFill) {
        if (from == ForgeDirection.UP || from == ForgeDirection.DOWN) {
            return inputBuffer.fill(resource, doFill);
        }
        return 0;
    }

    @Override
    public int fill(int tankIndex, LiquidStack resource, boolean doFill) {
        return inputBuffer.fill(resource, doFill);
    }

    @Override
    public LiquidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return null;
    }

    @Override
    public LiquidStack drain(int tankIndex, int maxDrain, boolean doDrain) {
        return null;
    }

    @Override
    public ILiquidTank[] getTanks(ForgeDirection direction) {
        if (direction == ForgeDirection.DOWN || direction == ForgeDirection.UP) {
            return new ILiquidTank[]{inputBuffer};
        }
        return null;
    }

    @Override
    public ILiquidTank getTank(ForgeDirection direction, LiquidStack type) {
        return null;
    }

	public void sendDebugToPlayer(EntityPlayerMP player) {
		LiquidStack li = inputBuffer.getLiquid();
		if (li == null) {
			player.addChatMessage("[TileGeneratorMJ] Input buffer: 0/8000");
		} else {
			player.addChatMessage("[TileGeneratorMJ] Input buffer: " + li.amount + "/8000");
		}
        player.addChatMessage("[TileGeneratorMJ] Power buffer: " + powerBuffer + "/" + maxPowerInBuffer);
	}

    @Override
    public void setPowerProvider(IPowerProvider provider) {
        powerProvider = provider;
    }

    @Override
    public IPowerProvider getPowerProvider() {
        return powerProvider;
    }

    @Override
    public void doWork() { }

    @Override
    public int powerRequest() {
        return 0;
    }

	// Read/Write NBT overrides for buffers
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		int tankLevel = 0;

		// inputBuffer
		LiquidStack li = inputBuffer.getLiquid();
		if (li != null) tankLevel = li.amount;
		nbt.setInteger("tankLevel", tankLevel);

		super.writeToNBT(nbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		// inputBuffer
		if (nbt.hasKey("tankLevel")) {
			int tankLevel = nbt.getInteger("tankLevel");
			inputBuffer.setLiquid(new LiquidStack(ModObjects.itemLiquidNavitas, tankLevel));
		}

		super.readFromNBT(nbt);
		LiquidEnergy.logger.info("[MJGen] Restored TE state for " + xCoord + ", " + yCoord + ", " + zCoord);
	}

}
