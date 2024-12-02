package com.trongthang.bettercampfires;

import com.trongthang.features.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerBlockEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class BetterCampfires implements ModInitializer {
    public static final String MOD_ID = "bettercampfires";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private int campfiresListCleanInterval = 60;
    private int counter = 0;

    public static final Identifier PLAY_BLOCK_LAVA_EXTINGUISH = new Identifier(MOD_ID, "play_block_lava_extinguish");

    public static ConcurrentHashMap<BlockPos, CampfireInfo> campfiresList = new ConcurrentHashMap<>();

    private DataHandler dataHandler = new DataHandler();

    private final CampfireBuffHandler campfireBuffHandler = new CampfireBuffHandler();
    private final CampfireCookHandler campfireCookHandler = new CampfireCookHandler();
    private final CampfireBurnOutHandler campfireBurnOutHandler = new CampfireBurnOutHandler();
    private final RainAndSnowExtinguishCampfireHandler rainAndSnowExtinguishCampfireHandler = new RainAndSnowExtinguishCampfireHandler();
    private final GetCampfireBurnTimeLeft getCampfireBurnTimeLeft = new GetCampfireBurnTimeLeft();

    @Override
    public void onInitialize() {

        ModConfig.loadConfig();
        LOGGER.info(MOD_ID + " has been initialized!");

        registerNewPlacedAndBrokenCampfiresInWorld();

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
        if (ModConfig.getInstance().campfiresExtinguishByRain || ModConfig.getInstance().campfiresExtinguishBySnow) {
            rainAndSnowExtinguishCampfireHandler.onServerTick(server.getOverworld());
        }

        Utils.onServerTick(server);

        counter++;
        if (counter < campfiresListCleanInterval) return;
        counter = 0;

        cleanUpCampfires(server);
        if(campfiresList.size() > campfiresListCleanInterval){
            campfiresListCleanInterval = campfiresList.size() * 3;
        }
    }

    public static void registerNewPlacedAndBrokenCampfiresInWorld() {

        if(!ModConfig.getInstance().applyToNewAllCampfiresInTheWorld) return;

        ServerBlockEntityEvents.BLOCK_ENTITY_LOAD.register((blockEntity, world) -> {
            if (blockEntity instanceof CampfireBlockEntity) {
                BlockPos pos = blockEntity.getPos();

                if (!campfiresList.isEmpty()) {
                    if (campfiresList.containsKey(pos)) {
                        return; // Skip if not lit or already tracked
                    }
                }

                // Create and store CampfireInfo for the newly placed campfire
                campfiresList.put(pos, new CampfireInfo());
            }
        });

        ServerBlockEntityEvents.BLOCK_ENTITY_UNLOAD.register((blockEntity, world) -> {
            if (blockEntity instanceof CampfireBlockEntity) {
                campfiresList.remove(blockEntity.getPos());
            }
        });
    }

    public static void cleanUpCampfires(MinecraftServer server) {
        ServerWorld world = server.getOverworld();

        for (BlockPos pos : campfiresList.keySet()) {
            Utils.addRunAfter(() -> {
                BlockState blockState = world.getBlockState(pos);
                if (blockState.contains(CampfireBlock.LIT) && !blockState.get(CampfireBlock.LIT)) {
                    campfiresList.remove(pos);
                }
            }, 2);
        }
    }
}
