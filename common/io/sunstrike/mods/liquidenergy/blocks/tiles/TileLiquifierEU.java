package io.sunstrike.mods.liquidenergy.blocks.tiles;

import ic2.api.Direction;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import io.sunstrike.mods.liquidenergy.LiquidEnergy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;

public class TileLiquifierEU extends TileEntity implements IEnergySink, ITankContainer {
	
	// Internal buffers
	private final LiquidTank outputBuffer = new LiquidTank(8000);
	
	// Used to check we're already on the ENet
	private boolean hasAddedToENet = false;
	
	public TileLiquifierEU() {
		//LiquidEnergy.logger.info("Created TE Liquifier EU.");
		outputBuffer.setTankPressure(100);
	}
	
	@Override
	public void updateEntity() {
		// IC2 initialisation
		if (!worldObj.isRemote) {
			if (!hasAddedToENet) {
				LiquidEnergy.logger.info("Registering entity at " + this.xCoord + ", " + yCoord + ", " + zCoord + " with IC2 ENet.");
				MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
				hasAddedToENet = true;
			}
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
					int removed = ((ITankContainer) te).fill(d.getOpposite(), new LiquidStack(LiquidEnergy.itemLiquidNavitas, 12), true);
					outputBuffer.drain(removed, true);
				}
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

	@Override
	public boolean acceptsEnergyFrom(TileEntity emitter, Direction direction) {
		return (direction != Direction.YN && direction != Direction.YP);
	}

	@Override
	public int demandsEnergy() {
		// This would be a "tier 1" converter (LV) (1Nav/8EU)
		return outputBuffer.fill(new LiquidStack(LiquidEnergy.itemLiquidNavitas, 4), false) * 8;
	}

	@Override
	public int injectEnergy(Direction directionFrom, int amount) {
		return amount/8 - outputBuffer.fill(new LiquidStack(LiquidEnergy.itemLiquidNavitas, amount/8), true);
	}

	@Override
	public int getMaxSafeInput() {
		return 32;
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
		player.addChatMessage("[TileLiquifierEU] On ENet: " + hasAddedToENet);
		LiquidStack li = outputBuffer.getLiquid();
		if (li == null) {
			player.addChatMessage("[TileLiquifierEU] Output buffer: 0/8000");
		} else {
			player.addChatMessage("[TileLiquifierEU] Output buffer: " + li.amount + "/8000");
		}
	}
	
	// Read/Write NBT overrides for buffer
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		int tankLevel = 0;
		
		LiquidStack li = outputBuffer.getLiquid();
		if (li != null) tankLevel = li.amount;
		nbt.setInteger("tankLevel", tankLevel);

		super.writeToNBT(nbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("tankLevel")) {
			int tankLevel = nbt.getInteger("tankLevel");
			outputBuffer.setLiquid(new LiquidStack(LiquidEnergy.itemLiquidNavitas, tankLevel));
		}

		super.readFromNBT(nbt);
		LiquidEnergy.logger.info("[EULiq] Restored TE state for " + xCoord + ", " + yCoord + ", " + zCoord);
	}

}
