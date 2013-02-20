package io.sunstrike.mods.liquidenergy.blocks.tiles;

import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerFramework;
import io.sunstrike.mods.liquidenergy.LiquidEnergy;
import io.sunstrike.mods.liquidenergy.configuration.ModObjects;
import io.sunstrike.mods.liquidenergy.configuration.Settings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;

/**
 * TileLiquifierEU
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

public class TileLiquifierMJ extends TileEntity implements ITankContainer, IPowerReceptor {

	// Internal buffers
	private final LiquidTank outputBuffer = new LiquidTank(8000);
    private IPowerProvider powerProvider = PowerFramework.currentFramework.createPowerProvider();

	public TileLiquifierMJ() {
		//LiquidEnergy.logger.info("Created TE Liquifier EU.");
		outputBuffer.setTankPressure(100);
        powerProvider.configure(0, 0, Settings.mjPerNv * 10, Settings.mjPerNv, Settings.mjPerNv * 100);
	}

	@Override
	public void updateEntity() {
        // Do work (BC)
        if (powerProvider.getEnergyStored() >= Settings.mjPerNv) {
            doWork();
        }

		// Dump to pipes/tanks
		for (ForgeDirection d : new ForgeDirection[]{ForgeDirection.DOWN, ForgeDirection.UP}) {
			int y;
			if (d == ForgeDirection.DOWN) {
				y = yCoord - 1;
			} else {
				y = yCoord + 1;
			}

			TileEntity te = worldObj.getBlockTileEntity(xCoord, y, zCoord);
			if (te instanceof ITankContainer) {
				LiquidStack ours = outputBuffer.getLiquid();
				if (ours != null && ours.amount > 0) {
					int removed = ((ITankContainer) te).fill(d.getOpposite(), new LiquidStack(ModObjects.itemLiquidNavitas, 12), true);
					outputBuffer.drain(removed, true);
				}
			}
		}
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
    public void doWork() {
        if (powerProvider.useEnergy(Settings.mjPerNv, Settings.mjPerNv, true) < Settings.mjPerNv) return;
        outputBuffer.fill(new LiquidStack(ModObjects.itemLiquidNavitas, Settings.mjPerNv), true);

        // Repeat if possible (see BC machines)
        if (powerProvider.getEnergyStored() >= Settings.mjPerNv) {
            doWork();
        }
    }

    @Override
    public int powerRequest() {
        if (canDoWork()) return Settings.mjPerNv;
        return 0;
    }

    private boolean canDoWork() {
        LiquidStack stored = outputBuffer.getLiquid();
        if (stored == null) return true;
        // 2 MJ/t block so check for space
        return stored.amount <= outputBuffer.getCapacity() - (Settings.mjPerNv / 2);
    }

	@Override
	public int fill(ForgeDirection from, LiquidStack resource, boolean doFill) {
		return 0;
	}

	@Override
	public int fill(int tankIndex, LiquidStack resource, boolean doFill) {
		return 0;
	}

	@Override
	public LiquidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		if (from == ForgeDirection.DOWN || from == ForgeDirection.UP) {
			return outputBuffer.drain(maxDrain, doDrain);
		}
		return null;
	}

	@Override
	public LiquidStack drain(int tankIndex, int maxDrain, boolean doDrain) {
		return outputBuffer.drain(maxDrain, doDrain);
	}

	@Override
	public ILiquidTank[] getTanks(ForgeDirection direction) {
		if (direction == ForgeDirection.DOWN || direction == ForgeDirection.UP) {
			return new ILiquidTank[]{outputBuffer};
		}
		return null;
	}

	@Override
	public ILiquidTank getTank(ForgeDirection direction, LiquidStack type) {
		return null;
	}

	public void sendDebugToPlayer(EntityPlayer player) {
        try {
		    player.addChatMessage("[TileLiquifierMJ] powerProvider: " + powerProvider.toString());
            LiquidStack l = outputBuffer.getLiquid();
            if (l != null) {
                player.addChatMessage("[TileLiquifierMJ] outputBuffer: " + l.amount + "/8000");
            } else {
                player.addChatMessage("[TileLiquifierMJ] outputBuffer: 0/8000");
            }
        } catch (Exception e) {
            player.addChatMessage("[TileLiquifierMJ] Exception getting debug: " + e.getMessage());
        }
	}

	// Read/Write NBT overrides for buffer
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		int tankLevel = 0;

		net.minecraftforge.liquids.LiquidStack li = outputBuffer.getLiquid();
		if (li != null) tankLevel = li.amount;
		nbt.setInteger("tankLevel", tankLevel);

        powerProvider.writeToNBT(nbt);

		super.writeToNBT(nbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("tankLevel")) {
			int tankLevel = nbt.getInteger("tankLevel");
			outputBuffer.setLiquid(new LiquidStack(ModObjects.itemLiquidNavitas, tankLevel));
		}

        powerProvider.readFromNBT(nbt);

		super.readFromNBT(nbt);
		LiquidEnergy.logger.info("[MJLiq] Restored TE state for " + xCoord + ", " + yCoord + ", " + zCoord);
	}

}
