package io.sunstrike.mods.liquidenergy.blocks;

import io.sunstrike.mods.liquidenergy.CommonProxy;
import io.sunstrike.mods.liquidenergy.blocks.tiles.TileGeneratorEU;
import io.sunstrike.mods.liquidenergy.blocks.tiles.TileLiquifierEU;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockGeneratorEU extends Block {

	public BlockGeneratorEU(int id, int tex, Material mat) {
		super(id, tex, mat);
		setHardness(5F);
	}
	
	@Override
	public String getTextureFile() {
		return CommonProxy.BLOCK_PNG;
	}
	
	@Override
	public boolean hasTileEntity(int meta) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, int meta) {
		return new TileGeneratorEU();
	}
	
	@Override
	public boolean isOpaqueCube() {
		return true;
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, int id, int meta) {
		// TODO: Drop liquid block on break and/or damage player for breaking in-use block
		super.breakBlock(world, x, y, z, id, meta);
	}
	
	@Override
	public void onBlockClicked(World w, int x, int y, int z, EntityPlayer player) {
		TileEntity te = w.getBlockTileEntity(x, y, z);
		if (!(te instanceof TileGeneratorEU) || !(player instanceof EntityPlayerMP)) return;
		if (player.getCurrentEquippedItem().getItem() == Item.stick) {
			((TileGeneratorEU) te).sendDebugToPlayer((EntityPlayerMP)player);
		}
	}

}
