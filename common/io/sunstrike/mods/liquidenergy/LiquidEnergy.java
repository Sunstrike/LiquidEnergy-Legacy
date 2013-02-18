package io.sunstrike.mods.liquidenergy;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
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
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;

import java.util.logging.Logger;

//import io.sunstrike.mods.liquidenergy.blocks.tiles.TileGeneratorMJ;

/**
 * LiquidEnergy
 *
 * @author Sunstrike <sunstrike@azurenode.net>
 */

@Mod(modid="LiquidEnergy", name="Liquid Energy", version="0.0.1", dependencies="after:IC2;after:BuildCraft|Energy")
@NetworkMod(clientSideRequired=true, serverSideRequired=false)
public class LiquidEnergy {

	@Instance("LiquidEnergy")
	public static LiquidEnergy instance;

	@SidedProxy(clientSide="io.sunstrike.mods.liquidenergy.client.ClientProxy", serverSide="io.sunstrike.mods.liquidenergy.CommonProxy")
	public static CommonProxy proxy;

    // LOGGERS
	public static final Logger logger = Logger.getLogger("LiquidEnergy");

	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		// Set logger parent (See EE3 LogHelper)
		logger.setParent(FMLLog.getLogger());
		
		// Config
        Settings.doConfig(new Configuration(event.getSuggestedConfigurationFile()));
	}

	@Init
	public void load(FMLInitializationEvent event) {
		proxy.registerRenderers();
		
		// General (Navitas registration)
		ModObjects.itemLiquidNavitas = new ItemLiquidNavitas(Settings.itemLiquidNavitas)
			.setItemName("itemLiquidNavitas")
			.setCreativeTab(CreativeTabs.tabRedstone);
		GameRegistry.registerItem(ModObjects.itemLiquidNavitas, "itemLiquidNavitas");
		LanguageRegistry.addName(ModObjects.itemLiquidNavitas, "Navitas");
		
		// Booleans for no-mod catch
		boolean ic2 = checkIC2();
        boolean bc = checkBCEnergy();
		
		// Catch lack of ANY power mods
		if (!(ic2 || bc)) throw new RuntimeException("Must have Buildcraft and/or IC2 installed!");
	}

    private boolean checkIC2() {
        try {
            Class ic2 = Class.forName("ic2.core.IC2");
            if (ic2 == null) { throw new Exception(); }
            // Assume we have IC2
            GameRegistry.registerTileEntity(TileLiquifierEU.class, "TileLiquifierEU");
            ModObjects.liquifierEU = new BlockLiquifierEU(Settings.blockLiquifyEU, 0, Material.anvil)
                    .setStepSound(Block.soundWoodFootstep)
                    .setBlockName("blockLiquifyEU")
                    .setCreativeTab(CreativeTabs.tabRedstone);
            GameRegistry.registerBlock(ModObjects.liquifierEU, "blockLiquifyEU");
            LanguageRegistry.addName(ModObjects.liquifierEU, "EU Liquifier");
            MinecraftForge.setBlockHarvestLevel(ModObjects.liquifierEU, "pickaxe", 0);

            GameRegistry.registerTileEntity(TileGeneratorEU.class, "TileGeneratorEU");
            ModObjects.generatorEU = new BlockGeneratorEU(Settings.blockGeneratorEU, 1, Material.anvil)
                    .setHardness(1.0F)
                    .setStepSound(Block.soundWoodFootstep)
                    .setBlockName("blockGeneratorEU")
                    .setCreativeTab(CreativeTabs.tabRedstone);
            GameRegistry.registerBlock(ModObjects.generatorEU, "blockGeneratorEU");
            LanguageRegistry.addName(ModObjects.generatorEU, "EU Generator");
            MinecraftForge.setBlockHarvestLevel(ModObjects.generatorEU, "pickaxe", 0);

            logger.info("[Integrations: IC2] Loaded integration module. Enabling EU liquifier and generator.");
            return true;
        } catch (Exception e) {
            logger.warning("[Integrations: IC2] Could not find IC2 Core! Disabling EU liquifier and generator (" + e.toString() + ")");
            return false;
        }
    }

    private boolean checkBCEnergy() {
        try {
            Class bcEnergy = Class.forName("buildcraft.energy.PneumaticPowerProvider");
            if (bcEnergy == null) { throw new Exception(); }
            // Assume BC Energy is available
            GameRegistry.registerTileEntity(TileLiquifierMJ.class, "TileLiquifierMJ");
            ModObjects.liquifierMJ = new BlockLiquifierMJ(Settings.blockLiquifyMJ, 2, Material.anvil)
                    .setStepSound(Block.soundWoodFootstep)
                    .setBlockName("blockLiquifyMJ")
                    .setCreativeTab(CreativeTabs.tabRedstone);
            GameRegistry.registerBlock(ModObjects.liquifierMJ, "blockLiquifyMJ");
            LanguageRegistry.addName(ModObjects.liquifierMJ, "MJ Liquifier");
            MinecraftForge.setBlockHarvestLevel(ModObjects.liquifierMJ, "pickaxe", 0);

            GameRegistry.registerTileEntity(TileGeneratorMJ.class, "TileGeneratorMJ");
            ModObjects.generatorMJ = new BlockGeneratorMJ(Settings.blockGeneratorMJ, 3, Material.anvil)
                    .setHardness(1.0F)
                    .setStepSound(Block.soundWoodFootstep)
                    .setBlockName("blockGeneratorMJ")
                    .setCreativeTab(CreativeTabs.tabRedstone);
            GameRegistry.registerBlock(ModObjects.generatorMJ, "blockGeneratorMJ");
            LanguageRegistry.addName(ModObjects.generatorMJ, "MJ Generator");
            MinecraftForge.setBlockHarvestLevel(ModObjects.generatorMJ, "pickaxe", 0);

            logger.info("[Integrations: BC Energy] Loaded integration module. Enabling MJ liquifier and generator.");
            return true;
        } catch (Exception e) {
            logger.warning("[Integrations: BC Energy] Could not find Buildcraft pneumatic power provider! Disabling MJ liquifier and generator (" + e.toString() + ")");
            return false;
        }
    }

	@PostInit
	public void postInit(FMLPostInitializationEvent event) {
		// Stub Method
	}

}