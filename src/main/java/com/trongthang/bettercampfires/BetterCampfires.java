package com.trongthang.bettercampfires;

import com.trongthang.features.CampfireBuffHandler;
import com.trongthang.features.CampfireBurnOutHandler;
import com.trongthang.features.CampfireCookHandler;
import com.trongthang.features.RainExtinguishCampfireHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class BetterCampfires implements ModInitializer {
	public static final String MOD_ID = "bettercampfires";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Identifier PLAY_BLOCK_LAVA_EXTINGUISH = new Identifier(MOD_ID, "play_block_lava_extinguish");

	public static ConcurrentHashMap<BlockPos, CooldownTick> campfiresList = new ConcurrentHashMap<>();

	private DataHandler dataHandler = new DataHandler();

	private final CampfireBuffHandler campfireBuffHandler = new CampfireBuffHandler();
	private final CampfireCookHandler campfireCookHandler = new CampfireCookHandler();
	private final CampfireBurnOutHandler campfireBurnOutHandler = new CampfireBurnOutHandler();
	private final RainExtinguishCampfireHandler rainExtinguishCampfireHandler = new RainExtinguishCampfireHandler();

	@Override
	public void onInitialize() {
		ModConfig.loadConfig();
		LOGGER.info(MOD_ID + " has been initialized!");

		ServerLifecycleEvents.SERVER_STARTING.register((t) -> {
			dataHandler.initializeWorldData(t);
		});

		ServerLifecycleEvents.SERVER_STOPPING.register((t) -> {
			dataHandler.saveCampfiresData();
		});

		ModConfig.getInstance().initializeCookableItems();
		campfireBuffHandler.cacheEffects();
		ServerTickEvents.START_SERVER_TICK.register(this::onServerTick);
	}

	private void onServerTick(MinecraftServer server) {
		if (ModConfig.getInstance().campfiresCanCook) {
			campfireCookHandler.onServerTick(server);
		}
		if (ModConfig.getInstance().campfiresCanBuff) {
			campfireBuffHandler.onServerTick(server);
		}
		if (ModConfig.getInstance().campfiresCanBurnOut) {
			campfireBurnOutHandler.onServerTick(server.getOverworld());
		}
		if (ModConfig.getInstance().campfiresExtinguishByRain) {
			rainExtinguishCampfireHandler.onServerTick(server.getOverworld());
		}
	}
}
