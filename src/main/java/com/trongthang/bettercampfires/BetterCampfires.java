package com.trongthang.bettercampfires;

import com.trongthang.features.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerBlockEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BetterCampfires implements ModInitializer {
    public static final String MOD_ID = "bettercampfires";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private int campfiresListCleanInterval = 60;
    private int counter = 0;

    public static final Identifier PLAY_BLOCK_LAVA_EXTINGUISH = new Identifier(MOD_ID, "play_block_lava_extinguish");

    private final GetCampfireBurnTimeLeft getCampfireBurnTimeLeft = new GetCampfireBurnTimeLeft();

    public static final Map<String, StatusEffect> cachedEffects = new HashMap<>();

    public void cacheEffects() {
        ModConfig.getInstance().buffs.forEach(buff -> {
            StatusEffect effect = Registries.STATUS_EFFECT.get(new Identifier(buff.effect));
            if (effect != null) cachedEffects.put(buff.effect, effect);
        });

        ModConfig.getInstance().hostileMobBuffs.forEach(buff -> {
            StatusEffect effect = Registries.STATUS_EFFECT.get(new Identifier(buff.effect));
            if (effect != null) cachedEffects.put(buff.effect, effect);
        });
    }

    @Override
    public void onInitialize() {


        ModConfig.loadConfig();
        LOGGER.info(MOD_ID + " has been initialized!");

        ServerTickEvents.START_SERVER_TICK.register(this::onServerTick);
        getCampfireBurnTimeLeft.handleSendingCampfiresBurnTime();
        cacheEffects();
    }

    private void onServerTick(MinecraftServer server) {
        Utils.onServerTick(server);
    }

}
