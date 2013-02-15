package io.sunstrike.mods.liquidenergy;

import io.sunstrike.mods.liquidenergy.blocks.BlockLiquifierEU;
import io.sunstrike.mods.liquidenergy.blocks.TileLiquifierEU;
import io.sunstrike.mods.liquidenergy.items.LiquidNavitas;

import java.util.HashMap;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
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

@Mod(modid="LiquidEnergy", name="Liquid Energy", version="0.0.1")
@NetworkMod(clientSideRequired=true, serverSideRequired=false)
public class LiquidEnergy {

	@Instance("LiquidEnergy")
	public static LiquidEnergy instance;

	@SidedProxy(clientSide="io.sunstrike.mods.liquidenergy.client.ClientProxy", serverSide="io.sunstrike.mods.liquidenergy.CommonProxy")
	public static CommonProxy proxy;

	// BLOCKS
	public static Block liquifierEU;
	public static Block liquifierMJ;
	public static Block generatorEU;
	public static Block generatorMJ;
	public static Item itemLiquidNavitas;

	// ID MAPS
	public static HashMap<String, Integer> bIDs = new HashMap();
	
	// LOGGERS
	public static Logger logger = Logger.getLogger("LiquidEnergy");

	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		// Set logger parent (See EE3 LogHelper)
		logger.setParent(FMLLog.getLogger());
		
		// Config
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		bIDs.clear();
		bIDs.put("BlockLiquifyEU", config.getBlock("BlockLiquifyEU", 1460).getInt());
		bIDs.put("BlockLiquifyMJ", config.getBlock("BlockLiquifyMJ", 1461).getInt());
		bIDs.put("BlockGeneratorEU", config.getBlock("BlockGeneratorEU", 1462).getInt());
		bIDs.put("BlockGeneratorMJ", config.getBlock("BlockGeneratorMJ", 1463).getInt());
		bIDs.put("LiquidNavitas", config.getItem("LiquidNavitas", 1464).getInt());
		config.save();
	}

	@Init
	public void load(FMLInitializationEvent event) {
		proxy.registerRenderers();
		
		// General (Navitas registration)
		itemLiquidNavitas = new LiquidNavitas(bIDs.get("LiquidNavitas").intValue())
			.setItemName("itemLiquidNavitas")
			.setCreativeTab(CreativeTabs.tabRedstone);
		GameRegistry.registerItem(itemLiquidNavitas, "itemLiquidNavitas");
		LanguageRegistry.addName(itemLiquidNavitas, "Navitas");
		
		// Boolean for no-mod catch
		boolean modsAvailable = false;
		
		// IC2
		try {
			Class ic2 = Class.forName("ic2.core.IC2");
			if (ic2 == null) { throw new Exception(); }
			// Assume we have IC2
			GameRegistry.registerTileEntity(TileLiquifierEU.class, "TileLiquifierEU");
			liquifierEU = new BlockLiquifierEU(bIDs.get("BlockLiquifyEU").intValue(), 0, Material.anvil)
				.setStepSound(Block.soundWoodFootstep)
				.setBlockName("blockLiquifyEU")
				.setCreativeTab(CreativeTabs.tabRedstone);
			GameRegistry.registerBlock(liquifierEU, "blockLiquifyEU");
			LanguageRegistry.addName(liquifierEU, "EU Liquifier");
			MinecraftForge.setBlockHarvestLevel(liquifierEU, "pickaxe", 0);
			
			// TODO: Register BlockGeneratorEU here.
			/*generatorEU = new BlockGeneratorEU(bIDs.get("BlockGeneratorEU").intValue(), 0, Material.anvil)
				.setHardness(1.0F)
				.setStepSound(Block.soundWoodFootstep)
				.setBlockName("BlockGeneratorEU")
				.setCreativeTab(CreativeTabs.tabRedstone);
			GameRegistry.registerBlock(generatorEU, "BlockGeneratorEU");
			LanguageRegistry.addName(generatorEU, "EU Generator");
			MinecraftForge.setBlockHarvestLevel(generatorEU, "pickaxe", 0);*/
			
			logger.info("Loaded IC2 integration. Enabling EU liquifier and generator.");
			modsAvailable = true;
		} catch (Exception e) {
			logger.warning("Could not find IC2! Disabling EU liquifier and generator.");
			logger.warning(e.toString());
		}
		
		// BuildCraft
		// TODO: ALL the buildcraft things
		
		// Catch lack of ANY power mods
		if (!modsAvailable) throw new RuntimeException("Must have Buildcraft and/or IC2 installed!");
	}

	@PostInit
	public void postInit(FMLPostInitializationEvent event) {
		// Stub Method
	}

}