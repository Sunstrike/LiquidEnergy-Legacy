package io.sunstrike.mods.liquidenergy.client;

import net.minecraftforge.client.MinecraftForgeClient;
import io.sunstrike.mods.liquidenergy.CommonProxy;

public class ClientProxy extends CommonProxy {
	
	@Override
    public void registerRenderers() {
            MinecraftForgeClient.preloadTexture(ITEMS_PNG);
            MinecraftForgeClient.preloadTexture(BLOCK_PNG);
    }

}
