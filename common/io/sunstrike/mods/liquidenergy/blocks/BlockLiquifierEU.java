package io.sunstrike.mods.liquidenergy.blocks;

import io.sunstrike.mods.liquidenergy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

// TODO: Implement IC2 events/interfaces as stated in ic2.api.energy.usage.txt (don't skip this!)

public class BlockLiquifierEU extends Block {

	public BlockLiquifierEU(int id, int tex, Material mat) {
		super(id, tex, mat);
	}
	
	@Override
	public String getTextureFile() {
		return CommonProxy.BLOCK_PNG;
	}
	
	@Override
	public TileEntity createTileEntity(World world, int meta) {
		return new TileLiquifierEU();
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, int id, int meta) {
		// TODO: Drop liquid block on break and/or damage player for breaking in-use block
		super.breakBlock(world, x, y, z, id, meta);
	}

}
