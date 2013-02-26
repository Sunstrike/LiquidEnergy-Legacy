package io.sunstrike.mods.liquidenergy.multiblock.blocks;

import io.sunstrike.api.liquidenergy.Position;
import io.sunstrike.api.liquidenergy.multiblock.ComponentDescriptor;
import io.sunstrike.mods.liquidenergy.blocks.LEBlock;
import io.sunstrike.mods.liquidenergy.helpers.MultiblockDiscoveryHelper;
import io.sunstrike.mods.liquidenergy.multiblock.MultiblockDescriptor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * BlockOutput
 * io.sunstrike.mods.liquidenergy.multiblock.blocks
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
 * Root class for multiblock output blocks
 *
 * @author Sunstrike <sunstrike@azurenode.net>
 */
public class BlockOutput extends LEBlock {

    public BlockOutput(int id, int tex, Material mat) {
        super(id, tex, mat);
        setHardness(5F);
    }

    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
        super.onBlockClicked(world, x, y, z, player);
        if (world.isRemote) return;
        ItemStack held = player.getCurrentEquippedItem();
        if (held == null || held.getItem() != Item.stick) return;

        MultiblockDescriptor desc = MultiblockDiscoveryHelper.discoverTransformerStructure(new Position(x, y, z, world), ComponentDescriptor.OUTPUT_GENERIC);
        if (desc == null) {
            player.addChatMessage("Got NULL from disc. helper.");
            return;
        }
        player.addChatMessage("Got object from disc. helper: " + desc);
    }

}
