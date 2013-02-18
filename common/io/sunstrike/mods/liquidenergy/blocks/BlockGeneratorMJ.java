package io.sunstrike.mods.liquidenergy.blocks;

import io.sunstrike.mods.liquidenergy.CommonProxy;
import io.sunstrike.mods.liquidenergy.blocks.tiles.TileGeneratorMJ;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * BlockGeneratorEU
 *
 * @author Sunstrike <sunstrike@azurenode.net>
 */

public class BlockGeneratorMJ extends Block {

	public BlockGeneratorMJ(int id, int tex, Material mat) {
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
		return new TileGeneratorMJ();
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
		if (!(te instanceof TileGeneratorMJ) || !(player instanceof EntityPlayerMP)) return;
		if (player.getCurrentEquippedItem().getItem() == Item.stick) {
			((TileGeneratorMJ) te).sendDebugToPlayer((EntityPlayerMP)player);
		}
	}

}
