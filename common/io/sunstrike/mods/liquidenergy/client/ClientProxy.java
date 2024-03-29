package io.sunstrike.mods.liquidenergy.client;

import cpw.mods.fml.client.FMLClientHandler;
import io.sunstrike.mods.liquidenergy.CommonProxy;
import io.sunstrike.mods.liquidenergy.external.buildcraft.core.render.TextureLiquidsFX;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraftforge.client.MinecraftForgeClient;

/*
 * ClientProxy
 * io.sunstrike.mods.liquidenergy.client
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
 * Standard Forge proxy (client-only)
 *
 * @author Sunstrike <sunstrike@azurenode.net>
 */
public class ClientProxy extends CommonProxy {
	
	@Override
    public void registerRenderers() {
        // Forge texture preload
        MinecraftForgeClient.preloadTexture(ITEMS_PNG);
        MinecraftForgeClient.preloadTexture(BLOCK_PNG);

        // Navitas renderer
        RenderEngine re = FMLClientHandler.instance().getClient().renderEngine;
        re.registerTextureFX(new TextureLiquidsFX(30, 130, 75, 105, 115, 135, 0, ITEMS_PNG));
    }

}
