package com.trongthang.bettercampfires;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterCampfires implements ModInitializer {
	public static final String MOD_ID = "bettercampfires";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private final CampfireBuffHandler buffHandler = new CampfireBuffHandler();
	private final CampfireCookHandler cookHandler = new CampfireCookHandler();

	@Override
	public void onInitialize() {
		ModConfig.loadConfig();
		LOGGER.info(MOD_ID + " has been initialized!");

		ModConfig.getInstance().initializeCookableItems();
		buffHandler.cacheEffects();
		ServerTickEvents.START_SERVER_TICK.register(this::onServerTick);
	}

	private void onServerTick(MinecraftServer server) {
		if(ModConfig.getInstance().campfiresCanCook){
			cookHandler.onServerTick(server);
		}

		if(ModConfig.getInstance().campfiresCanCook){
			buffHandler.onServerTick(server);
		}
	}
}
