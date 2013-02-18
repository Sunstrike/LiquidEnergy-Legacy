package io.sunstrike.mods.liquidenergy.configuration;

import net.minecraftforge.common.Configuration;

/**
 * Settings
 *
 * This class should only hold user-adjustable settings.
 * Block and item instances belong in ModObjects!
 *
 * @author Sunstrike <sunstrike@azurenode.net>
 */

public class Settings {
	
	// Blocks
	public static int blockLiquifyEU = 1460;
	public static int blockLiquifyMJ = 1461;
	public static int blockGeneratorEU = 1462;
	public static int blockGeneratorMJ = 1463;
	
	// Items
	public static int itemLiquidNavitas = 1464;
	
	// Conversions
	public static int euPerNv = 8;
	public static int mjPerNv = 1;

    public static void doConfig(Configuration config) {
        config.load();

        // Blocks
        blockLiquifyEU = config.getBlock("BlockLiquifyEU", 1460).getInt();
        blockLiquifyMJ = config.getBlock("BlockLiquifyMJ", 1461).getInt();
        blockGeneratorEU = config.getBlock("BlockGeneratorEU", 1462).getInt();
        blockGeneratorMJ = config.getBlock("BlockGeneratorMJ", 1463).getInt();

        // Items
        itemLiquidNavitas = config.getItem("LiquidNavitas", 1464).getInt();

        // Conversion values
        euPerNv = config.get("conversions", "euPerNv", 8).getInt();
        mjPerNv = config.get("conversions", "mjPerNv", 1).getInt();

        config.save();
    }

}
