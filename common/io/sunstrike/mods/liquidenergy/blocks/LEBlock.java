package io.sunstrike.mods.liquidenergy.blocks;

import io.sunstrike.api.liquidenergy.multiblock.Tile;
import io.sunstrike.mods.liquidenergy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

/**
 * LEBlock
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
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * @author Sunstrike <sunstrike@azurenode.net>
 */

/**
 * Root block class for all LE blocks.
 *
 * @author Sunstrike <sunstrike@azurenode.net>
 */
public class LEBlock extends Block {

    public LEBlock(int id, int tex, Material mat) {
        super(id, tex, mat);
        setHardness(5.0f);
    }

    @Override
    public boolean isOpaqueCube() {
        return true;
    }

    @Override
    public String getTextureFile() {
        return CommonProxy.BLOCK_PNG;
    }

    @Override
    public int getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
        TileEntity te = blockAccess.getBlockTileEntity(x, y, z);
        if (!(te instanceof Tile)) return super.getBlockTexture(blockAccess, x, y, z, side);
        return ((Tile)te).getTexture(ForgeDirection.getOrientation(side));
    }

    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
        super.onBlockClicked(world, x, y, z, player);
        if (world.isRemote) return;
        ItemStack held = player.getCurrentEquippedItem();
        if (held == null || held.getItem() != Item.stick) return;

        TileEntity te = world.getBlockTileEntity(x, y, z);
        if (!(te instanceof Tile)) return;

        ((Tile)te).debugInfo(player);
    }

}
