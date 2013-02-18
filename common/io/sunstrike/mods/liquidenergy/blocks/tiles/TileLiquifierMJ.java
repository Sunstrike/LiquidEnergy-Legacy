package io.sunstrike.mods.liquidenergy.blocks.tiles;

import io.sunstrike.mods.liquidenergy.LiquidEnergy;
import io.sunstrike.mods.liquidenergy.configuration.ModObjects;
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
 *
 * @author Sunstrike <sunstrike@azurenode.net>
 */

public class TileLiquifierMJ extends TileEntity implements ITankContainer {

	// Internal buffers
	private final LiquidTank outputBuffer = new LiquidTank(8000);

	public TileLiquifierMJ() {
		//LiquidEnergy.logger.info("Created TE Liquifier EU.");
		outputBuffer.setTankPressure(100);
	}

	@Override
	public void updateEntity() {
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
		// TODO: Debug info
	}

	// Read/Write NBT overrides for buffer
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		int tankLevel = 0;

		net.minecraftforge.liquids.LiquidStack li = outputBuffer.getLiquid();
		if (li != null) tankLevel = li.amount;
		nbt.setInteger("tankLevel", tankLevel);

		super.writeToNBT(nbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("tankLevel")) {
			int tankLevel = nbt.getInteger("tankLevel");
			outputBuffer.setLiquid(new LiquidStack(ModObjects.itemLiquidNavitas, tankLevel));
		}

		super.readFromNBT(nbt);
		LiquidEnergy.logger.info("[MJLiq] Restored TE state for " + xCoord + ", " + yCoord + ", " + zCoord);
	}

}
