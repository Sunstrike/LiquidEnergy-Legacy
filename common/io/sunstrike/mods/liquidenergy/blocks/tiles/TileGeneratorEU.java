package io.sunstrike.mods.liquidenergy.blocks.tiles;

import ic2.api.Direction;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileSourceEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySource;
import io.sunstrike.mods.liquidenergy.LiquidEnergy;
import io.sunstrike.mods.liquidenergy.configuration.ModObjects;
import io.sunstrike.mods.liquidenergy.configuration.Settings;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;

/**
 * TileGeneratorEU
 *
 * @author Sunstrike <sunstrike@azurenode.net>
 */

public class TileGeneratorEU extends TileEntity implements IEnergySource, ITankContainer {
	
	// Internal buffers
	private final LiquidTank inputBuffer = new LiquidTank(8000);
	private int powerBuffer = 0;
	
	// Used to check we're already on the ENet
	private transient boolean hasAddedToENet = false;
	
	public TileGeneratorEU() {
		//LiquidEnergy.logger.info("Created TE Generator EU.");
		inputBuffer.setTankPressure(-100);
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
		
		// Emit energy if possible
		int maxNv = powerBufferSpace();
		LiquidStack out = inputBuffer.drain(maxNv, true);
		if (out == null || out.amount <= 0) return;
		powerBuffer += out.amount * Settings.euPerNv;
		
		if (powerBuffer >= 32) {
			// We need to keep a handle to the event so we can get how much is actually consumed
			EnergyTileSourceEvent evt = new EnergyTileSourceEvent(this, 32);
			MinecraftForge.EVENT_BUS.post(evt);
			// Adjust buffer depending on amount actually emitted (evt.amount = 0 where all energy consumed by ENet)
			powerBuffer -= 32 - evt.amount;
		}
	}
	
	// Get max amount of navitas that can be converted this tick
	private int powerBufferSpace() {
		// Buffer 'full' (Enough EU for 20 ticks)
		if (powerBuffer + Settings.euPerNv >= 640) return 0;
		
		// Buffer 'has space'
		return 32/Settings.euPerNv;
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
	public boolean emitsEnergyTo(TileEntity receiver, Direction direction) {
		return (direction != Direction.YN && direction != Direction.YP);
	}

	@Override
	public int getMaxEnergyOutput() {
		// Tier 1 (LV)
		return 32;
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
		player.addChatMessage("[TileGeneratorEU] On ENet: " + hasAddedToENet);
		LiquidStack li = inputBuffer.getLiquid();
		if (li == null) {
			player.addChatMessage("[TileGeneratorEU] Input buffer: 0/8000");
		} else {
			player.addChatMessage("[TileGeneratorEU] Input buffer: " + li.amount + "/8000");
		}
		player.addChatMessage("[TileGeneratorEU] Power buffer: " + powerBuffer + "/640");
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
		LiquidEnergy.logger.info("[EUGen] Restored TE state for " + xCoord + ", " + yCoord + ", " + zCoord);
	}

}
