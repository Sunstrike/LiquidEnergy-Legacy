package io.sunstrike.mods.liquidenergy.client;

import io.sunstrike.mods.liquidenergy.CommonProxy;
import net.minecraftforge.client.MinecraftForgeClient;

/**
 * ClientProxy
 *
 * @author Sunstrike <sunstrike@azurenode.net>
 */

public class ClientProxy extends CommonProxy {
	
	@Override
    public void registerRenderers() {
            MinecraftForgeClient.preloadTexture(ITEMS_PNG);
            MinecraftForgeClient.preloadTexture(BLOCK_PNG);
    }

}
