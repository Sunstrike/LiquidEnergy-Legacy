package io.sunstrike.mods.liquidenergy.client;

import io.sunstrike.mods.liquidenergy.CommonProxy;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.FMLClientHandler;
import io.sunstrike.mods.liquidenergy.external.buildcraft.core.render.TextureLiquidsFX;

/**
 * ClientProxy
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
