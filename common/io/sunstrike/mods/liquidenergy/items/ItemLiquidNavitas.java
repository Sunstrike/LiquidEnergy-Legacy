package io.sunstrike.mods.liquidenergy.items;

import net.minecraft.item.Item;
import net.minecraftforge.liquids.ILiquid;

public class ItemLiquidNavitas extends Item implements ILiquid {
	
	public ItemLiquidNavitas(int id) {
		super(id);
		setMaxStackSize(64);
		setIconIndex(0);
	}
	
	public String getTextureFile() {
		return io.sunstrike.mods.liquidenergy.CommonProxy.ITEMS_PNG;
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
