package io.sunstrike.mods.liquidenergy.blocks.tiles;

import io.sunstrike.mods.liquidenergy.LiquidEnergy;
import io.sunstrike.mods.liquidenergy.configuration.ModObjects;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;

/**
 * TileGeneratorEU
 *
 * @author Sunstrike <sunstrike@azurenode.net>
 */

public class TileGeneratorMJ extends TileEntity implements ITankContainer {

	// Internal buffers
	private final LiquidTank inputBuffer = new LiquidTank(8000);
	private int powerBuffer = 0;

	public TileGeneratorMJ() {
		//LiquidEnergy.logger.info("Created TE Generator EU.");
		inputBuffer.setTankPressure(-100);
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
		player.addChatMessage("[TileGeneratorMJ] Power buffer: " + powerBuffer + "/4000");
	}

	// Read/Write NBT overrides for buffers
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		int tankLevel = 0;

		// inputBuffer
		LiquidStack li = inputBuffer.getLiquid();
		if (li != null) tankLevel = li.amount;
		nbt.setInteger("tankLevel", tankLevel);

		// powerBuffer
		nbt.setInteger("powerLevel", powerBuffer);

		super.writeToNBT(nbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		// inputBuffer
		if (nbt.hasKey("tankLevel")) {
			int tankLevel = nbt.getInteger("tankLevel");
			inputBuffer.setLiquid(new LiquidStack(ModObjects.itemLiquidNavitas, tankLevel));
		}

		// powerBuffer
		if (nbt.hasKey("powerLevel")) {
			powerBuffer = nbt.getInteger("powerLevel");
		}

		super.readFromNBT(nbt);
		LiquidEnergy.logger.info("[MJGen] Restored TE state for " + xCoord + ", " + yCoord + ", " + zCoord);
	}

}
