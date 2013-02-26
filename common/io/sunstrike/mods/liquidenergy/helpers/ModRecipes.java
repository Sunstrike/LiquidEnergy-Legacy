package io.sunstrike.mods.liquidenergy.helpers;

import ic2.api.Items;
import io.sunstrike.mods.liquidenergy.LiquidEnergy;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/*
 * ModRecipes
 * io.sunstrike.mods.liquidenergy.helpers
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
 * Static control class for mod recipes.
 * </p>
 * Recipes only containing vanilla objects will be staticly defined from launch, other mod content must have the
 * corresponding initialiser called before use, to allow reflection and/or APIs to be called for setup.
 *
 * @author Sunstrike <sunstrike@azurenode.net>
 */
public class ModRecipes {

    // IC2 BLOCKS
    public static Object[] blockLiquifierEU;
    public static Object[] blockGeneratorEU;

    // BC BLOCKS
    public static Object[] blockLiquifierMJ;
    public static Object[] blockGeneratorMJ;

    /**
     * Call before using any IC2 item-including recipes (to grab items from IC2 API)
     */
    public static void setupIC2Recipes() throws NullPointerException {
        ItemStack piston = new ItemStack(Block.pistonBase);
        ItemStack glass = new ItemStack(Block.thinGlass);
        ItemStack fiberCable = Items.getItem("glassFiberCableItem");
        ItemStack batbox = Items.getItem("batBox");
        ItemStack lvTransformer = Items.getItem("lvTransformer");

        if (fiberCable == null || batbox == null || lvTransformer == null) throw new NullPointerException();

        /*
         * EU Liquifier:
         *
         * [P][B][P]
         * [C][L][C]
         * [G][G][G]
         */
        blockLiquifierEU = new Object[]{"pbp", "clc", "ggg", 'p', piston, 'b', batbox, 'c', fiberCable, 'l', lvTransformer, 'g', glass};

        /*
         * EU Generator:
         *
         * [P][L][P]
         * [C][B][C]
         * [G][G][G]
         */
        blockGeneratorEU = new Object[]{"plp", "cbc", "ggg", 'p', piston, 'b', batbox, 'c', fiberCable, 'l', lvTransformer, 'g', glass};
    }

    /**
     * Call before using any BC item-including recipes (to reflect the items from BC)
     */
    public static void setupBCRecipes() throws ClassNotFoundException {
        Class bcEnergy = Class.forName("buildcraft.energy.PneumaticPowerProvider");
        if (bcEnergy == null) { throw new ClassNotFoundException(); }
        // Assume BC Energy is available
        // Materials for crafting recipes
        Class bcCore = Class.forName("buildcraft.BuildCraftCore");
        Class bcFactory = Class.forName("buildcraft.BuildCraftFactory");
        ItemStack goldGear = new ItemStack((Item) getStaticReflectedItem(bcCore, "goldGearItem"));
        ItemStack bcTank = new ItemStack((Block) getStaticReflectedItem(bcFactory, "tankBlock"));
        ItemStack piston = new ItemStack(Block.pistonBase);
        ItemStack diamond = new ItemStack(Item.diamond);
        ItemStack redstone = new ItemStack(Item.redstone);

        /*
         * MJ Liquifier:
         * [ ][T][ ]
         * [G][D][G]
         * [P][R][P]
         */
        blockLiquifierMJ = new Object[]{" t ", "gdg", "prp", 't', bcTank, 'g', goldGear, 'd', diamond, 'p', piston, 'r', redstone};

        /*
         * MJ Generator:
         * [ ][P][ ]
         * [G][D][G]
         * [T][R][T]
         */
        blockGeneratorMJ = new Object[]{" p ", "gdg", "trt", 't', bcTank, 'g', goldGear, 'd', diamond, 'p', piston, 'r', redstone};
    }

    /**
     * Helper method to get items/blocks through reflection
     *
     * @param cl Class to search in
     * @param fieldName Name of the field where the item/block is stored
     * @return The object if found (cast it) or null.
     */
    private static Object getStaticReflectedItem(Class cl, String fieldName) {
        try {
            return cl.getDeclaredField(fieldName).get(null);
        } catch (Exception e) {
            LiquidEnergy.logger.warning("Failed in getStaticReflectedItem: " + e.getMessage());
            return null;
        }
    }

}
