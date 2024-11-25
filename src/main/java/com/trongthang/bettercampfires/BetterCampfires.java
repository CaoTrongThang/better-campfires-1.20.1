package com.trongthang.bettercampfires;

import com.trongthang.features.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class BetterCampfires implements ModInitializer {
	public static final String MOD_ID = "bettercampfires";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Identifier PLAY_BLOCK_LAVA_EXTINGUISH = new Identifier(MOD_ID, "play_block_lava_extinguish");

	public static ConcurrentHashMap<BlockPos, CampfireInfo> campfiresList = new ConcurrentHashMap<>();

	private DataHandler dataHandler = new DataHandler();

	private final CampfireBuffHandler campfireBuffHandler = new CampfireBuffHandler();
	private final CampfireCookHandler campfireCookHandler = new CampfireCookHandler();
	private final CampfireBurnOutHandler campfireBurnOutHandler = new CampfireBurnOutHandler();
	private final RainExtinguishCampfireHandler rainExtinguishCampfireHandler = new RainExtinguishCampfireHandler();
	private final GetCampfireBurnTimeLeft getCampfireBurnTimeLeft = new GetCampfireBurnTimeLeft();

	@Override
	public void onInitialize() {

		ModConfig.loadConfig();
		LOGGER.info(MOD_ID + " has been initialized!");

		ServerLifecycleEvents.SERVER_STARTING.register((t) -> {
			dataHandler.initializeCampfiresData(t);
		});

		ServerLifecycleEvents.SERVER_STOPPING.register((t) -> {
			dataHandler.saveCampfiresData();
		});

		ModConfig.getInstance().initializeCookableItems();
		campfireBuffHandler.cacheEffects();
		ServerTickEvents.START_SERVER_TICK.register(this::onServerTick);
		getCampfireBurnTimeLeft.handleSendingCampfiresBurnTime();
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
