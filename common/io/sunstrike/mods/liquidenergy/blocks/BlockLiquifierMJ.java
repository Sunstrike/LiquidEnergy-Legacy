package io.sunstrike.mods.liquidenergy.blocks;

import io.sunstrike.mods.liquidenergy.CommonProxy;
import io.sunstrike.mods.liquidenergy.blocks.tiles.TileLiquifierMJ;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * BlockLiquifierEU
 * io.sunstrike.mods.liquidenergy.blocks
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

public class BlockLiquifierMJ extends Block {

	public BlockLiquifierMJ(int id, int tex, Material mat) {
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
		return new TileLiquifierMJ();
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
		if (!(te instanceof TileLiquifierMJ) || !(player instanceof EntityPlayerMP)) return;
		if (player.getCurrentEquippedItem().getItem() == Item.stick) {
			((TileLiquifierMJ) te).sendDebugToPlayer(player);
		}
	}

}