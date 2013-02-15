package io.sunstrike.mods.liquidenergy.items;

import net.minecraft.item.Item;
import net.minecraftforge.liquids.ILiquid;
import io.sunstrike.mods.liquidenergy.LiquidEnergy;

public class LiquidNavitas extends Item implements ILiquid {
	
	public LiquidNavitas(int id) {
		super(id);
		setMaxStackSize(64);
		setIconIndex(0);
	}
	
	public String getTextureFile() {
		return LiquidEnergy.proxy.ITEMS_PNG;
	}

	@Override
	public int stillLiquidId() {
		return this.itemID;
	}

	@Override
	public boolean isMetaSensitive() {
		return false;
	}

	@Override
	public int stillLiquidMeta() {
		return 0;
	}

}
