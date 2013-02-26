package io.sunstrike.mods.liquidenergy.configuration;

import net.minecraftforge.common.Configuration;

/*
 * Settings
 * io.sunstrike.mods.liquidenergy.configuration
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

/**
 * Container for user settings, along with handling config loading
 *
 * @author Sunstrike <sunstrike@azurenode.net>
 */
public class Settings {

	// To change defaults, change these variables before compile

	// Blocks
	public static int blockLiquifyEU = 1460;
	public static int blockLiquifyMJ = 1461;
	public static int blockGeneratorEU = 1462;
	public static int blockGeneratorMJ = 1463;

    // Multiblock blocks
    public static int blockComponentTank = 1464;
    public static int blockInputEU = 1465;
    public static int blockInputMJ = 1466;
    public static int BlockInputFluid = 1467;
    public static int blockOutputEU = 1468;
    public static int blockOutputMJ = 1469;
    public static int blockOutputFluid = 1470;
    public static int blockStructure = 1471;
	
	// Items
	public static int itemLiquidNavitas = 1464;
	
	// Conversions
	public static int euPerNv = 8;
	public static int mjPerNv = 2;

    public static void doConfig(Configuration config) {
        config.load();

        // Blocks
        blockLiquifyEU = config.getBlock("BlockLiquifyEU", blockLiquifyEU).getInt();
        blockLiquifyMJ = config.getBlock("BlockLiquifyMJ", blockLiquifyMJ).getInt();
        blockGeneratorEU = config.getBlock("BlockGeneratorEU", blockGeneratorEU).getInt();
        blockGeneratorMJ = config.getBlock("BlockGeneratorMJ", blockGeneratorMJ).getInt();

        // Multiblock blocks
        blockComponentTank = config.getBlock("blockComponentTank", blockComponentTank).getInt();
        blockInputEU = config.getBlock("blockInputEU", blockInputEU).getInt();
        blockInputMJ = config.getBlock("blockInputMJ", blockInputMJ).getInt();
        BlockInputFluid = config.getBlock("BlockInputFluid", BlockInputFluid).getInt();
        blockOutputEU = config.getBlock("blockOutputEU", blockOutputEU).getInt();
        blockOutputMJ = config.getBlock("blockOutputMJ", blockOutputMJ).getInt();
        blockOutputFluid = config.getBlock("blockOutputFluid", blockOutputFluid).getInt();
        blockStructure = config.getBlock("blockStructure", blockStructure).getInt();

        // Items
        itemLiquidNavitas = config.getItem("LiquidNavitas", itemLiquidNavitas).getInt();

        // Conversion values
        euPerNv = config.get("conversions", "euPerNv", euPerNv).getInt();
        mjPerNv = config.get("conversions", "mjPerNv", mjPerNv).getInt();

        config.save();
    }

}
