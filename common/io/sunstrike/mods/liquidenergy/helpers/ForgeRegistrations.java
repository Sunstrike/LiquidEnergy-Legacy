package io.sunstrike.mods.liquidenergy.helpers;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import io.sunstrike.mods.liquidenergy.LiquidEnergy;
import io.sunstrike.mods.liquidenergy.blocks.BlockGeneratorEU;
import io.sunstrike.mods.liquidenergy.blocks.BlockGeneratorMJ;
import io.sunstrike.mods.liquidenergy.blocks.BlockLiquifierEU;
import io.sunstrike.mods.liquidenergy.blocks.BlockLiquifierMJ;
import io.sunstrike.mods.liquidenergy.blocks.tiles.TileGeneratorEU;
import io.sunstrike.mods.liquidenergy.blocks.tiles.TileGeneratorMJ;
import io.sunstrike.mods.liquidenergy.blocks.tiles.TileLiquifierEU;
import io.sunstrike.mods.liquidenergy.blocks.tiles.TileLiquifierMJ;
import io.sunstrike.mods.liquidenergy.configuration.ModObjects;
import io.sunstrike.mods.liquidenergy.configuration.Settings;
import io.sunstrike.mods.liquidenergy.items.ItemLiquidNavitas;
import io.sunstrike.mods.liquidenergy.multiblock.blocks.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

/*
 * ForgeRegistrations
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
 * Static handler class for registering items and blocks with Minecraft Forge
 *
 * @author Sunstrike <sunstrike@azurenode.net>
 */
public class ForgeRegistrations {

    // This should never be instantiated; all static!
    private ForgeRegistrations() {}

    /**
     * Register all LE items
     */
    public static void registerItems() {
        // General (Navitas registration)
        ModObjects.itemLiquidNavitas = new ItemLiquidNavitas(Settings.itemLiquidNavitas)
                .setItemName("itemLiquidNavitas");
        GameRegistry.registerItem(ModObjects.itemLiquidNavitas, "itemLiquidNavitas");
        LanguageRegistry.addName(ModObjects.itemLiquidNavitas, "Navitas");
    }

    /**
     * Register all LE blocks
     */
    public static void registerBlocks() {
        // Register shared blocks
        registerSharedBlocks();

        // Booleans for no-mod catch
        boolean ic2 = checkIC2();
        boolean bc = checkBCEnergy();

        // Catch lack of ANY power mods
        if (!(ic2 || bc)) throw new RuntimeException("Must have Buildcraft and/or IC2 installed!");
    }

    /**
     * Private helper for registering shared blocks
     */
    private static void registerSharedBlocks() {
        //GameRegistry.registerTileEntity(TileComponentTank.class, "TileComponentTank");
        ModObjects.blockComponentTank = new BlockComponentTank(Settings.blockComponentTank, 4, Material.glass)
                .setStepSound(Block.soundGlassFootstep)
                .setBlockName("blockComponentTank")
                .setCreativeTab(CreativeTabs.tabRedstone);
        registerBlock(ModObjects.blockComponentTank, "blockComponentTank", "Transformer Tank", 1, ModRecipes.blockComponentTank);

        //GameRegistry.registerTileEntity(TileInputFluid.class, "TileInputFluid");
        ModObjects.blockInputFluid = new BlockInputFluid(Settings.blockInputFluid, 5, Material.iron)
                .setStepSound(Block.soundMetalFootstep)
                .setBlockName("blockInputFluid")
                .setCreativeTab(CreativeTabs.tabRedstone);
        registerBlock(ModObjects.blockInputFluid, "blockInputFluid", "Fluid Input", 1, ModRecipes.blockInputFluid);

        //GameRegistry.registerTileEntity(TileOutputFluid.class, "TileOutputFluid");
        ModObjects.blockOutputFluid = new BlockOutputFluid(Settings.blockOutputFluid, 6, Material.iron)
                .setStepSound(Block.soundMetalFootstep)
                .setBlockName("blockOutputFluid")
                .setCreativeTab(CreativeTabs.tabRedstone);
        registerBlock(ModObjects.blockOutputFluid, "blockOutputFluid", "Fluid Output", 1, ModRecipes.blockOutputFluid);

        ModObjects.blockStructure = new BlockStructure(Settings.blockStructure, 4, Material.rock)
                .setStepSound(Block.soundStoneFootstep)
                .setBlockName("blockStructure")
                .setCreativeTab(CreativeTabs.tabRedstone);
        registerBlock(ModObjects.blockStructure, "blockStructure", "Transformer Structure", 7, ModRecipes.blockStructure);
    }

    /**
     * Private helper for checking for IC2, then registering blocks if possible.
     *
     * @return true for found, false for not found/failed
     */
    private static boolean checkIC2() {
        try {
            ModRecipes.setupIC2Recipes();

            Class ic2 = Class.forName("ic2.core.IC2");
            if (ic2 == null) throw new ClassNotFoundException();

            // Register blocks and recipes
            GameRegistry.registerTileEntity(TileLiquifierEU.class, "TileLiquifierEU");
            ModObjects.liquifierEU = new BlockLiquifierEU(Settings.blockLiquifyEU, 0, Material.anvil)
                    .setStepSound(Block.soundWoodFootstep)
                    .setBlockName("blockLiquifyEU")
                    .setCreativeTab(CreativeTabs.tabRedstone);
            registerBlock(ModObjects.liquifierEU, "blockLiquifyEU", "EU Liquifier", 1, ModRecipes.blockLiquifierEU);

            GameRegistry.registerTileEntity(TileGeneratorEU.class, "TileGeneratorEU");
            ModObjects.generatorEU = new BlockGeneratorEU(Settings.blockGeneratorEU, 1, Material.anvil)
                    .setStepSound(Block.soundWoodFootstep)
                    .setBlockName("blockGeneratorEU")
                    .setCreativeTab(CreativeTabs.tabRedstone);
            registerBlock(ModObjects.generatorEU, "blockGeneratorEU", "EU Generator", 1, ModRecipes.blockGeneratorEU);

            //GameRegistry.registerTileEntity(TileInputEU.class, "TileInputEU");
            ModObjects.blockInputEU = new BlockInputEU(Settings.blockInputEU, 8, Material.iron)
                    .setStepSound(Block.soundMetalFootstep)
                    .setBlockName("blockInputEU")
                    .setCreativeTab(CreativeTabs.tabRedstone);
            registerBlock(ModObjects.blockInputEU, "blockInputEU", "EU Input", 1, ModRecipes.blockInputEU);

            //GameRegistry.registerTileEntity(TileOutputEU.class, "TileOutputEU");
            ModObjects.blockOutputEU = new BlockOutputEU(Settings.blockOutputEU, 9, Material.iron)
                    .setStepSound(Block.soundMetalFootstep)
                    .setBlockName("blockOutputEU")
                    .setCreativeTab(CreativeTabs.tabRedstone);
            registerBlock(ModObjects.blockOutputEU, "blockOutputEU", "EU Output", 1, ModRecipes.blockOutputEU);

            LiquidEnergy.logger.info("[Integrations: IC2] Loaded integration module. Enabling EU liquifier and generator.");
            return true;
        } catch (ClassNotFoundException e) {
            LiquidEnergy.logger.warning("[Integrations: IC2] Could not find IC2 Core! Disabling EU liquifier and generator (ClassNotFoundException)");
            return false;
        }
    }

    /**
     * Private helper for checking for BC, then registering blocks if possible.
     *
     * @return true for found, false for not found/failed
     */
    private static boolean checkBCEnergy() {
        try {
            ModRecipes.setupBCRecipes();

            // Register blocks and recipes
            GameRegistry.registerTileEntity(TileLiquifierMJ.class, "TileLiquifierMJ");
            ModObjects.liquifierMJ = new BlockLiquifierMJ(Settings.blockLiquifyMJ, 2, Material.anvil)
                    .setStepSound(Block.soundWoodFootstep)
                    .setBlockName("blockLiquifyMJ")
                    .setCreativeTab(CreativeTabs.tabRedstone);
            registerBlock(ModObjects.liquifierMJ, "blockLiquifyMJ", "MJ Liquifier", 1, ModRecipes.blockLiquifierMJ);

            GameRegistry.registerTileEntity(TileGeneratorMJ.class, "TileGeneratorMJ");
            ModObjects.generatorMJ = new BlockGeneratorMJ(Settings.blockGeneratorMJ, 3, Material.anvil)
                    .setHardness(1.0F)
                    .setStepSound(Block.soundWoodFootstep)
                    .setBlockName("blockGeneratorMJ")
                    .setCreativeTab(CreativeTabs.tabRedstone);
            registerBlock(ModObjects.generatorMJ, "blockGeneratorMJ", "MJ Generator", 1, ModRecipes.blockGeneratorMJ);

            LiquidEnergy.logger.info("[Integrations: BC Energy] Loaded integration module. Enabling MJ liquifier and generator.");
            return true;
        } catch (ClassNotFoundException e) {
            LiquidEnergy.logger.warning("[Integrations: BC Energy] Could not find Buildcraft! Disabling MJ liquifier and generator (ClassNotFoundException)");
            return false;
        }
    }

    private static void registerBlock(Block bl, String internalName, String externalName, int amount, Object... recipe) {
        GameRegistry.registerBlock(bl, internalName);
        LanguageRegistry.addName(bl, externalName);
        MinecraftForge.setBlockHarvestLevel(bl, "pickaxe", 0);
        GameRegistry.addRecipe(new ItemStack(bl, amount), recipe);
    }

}
