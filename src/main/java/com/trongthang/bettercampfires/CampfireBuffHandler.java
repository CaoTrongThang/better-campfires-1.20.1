package com.trongthang.bettercampfires;

import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.registry.Registries;

import java.util.HashMap;
import java.util.Map;

public class CampfireBuffHandler {
    private int buffCheckCounter = 0;
    private final Map<String, StatusEffect> cachedEffects = new HashMap<>();

    public void cacheEffects() {
        ModConfig.getInstance().buffs.forEach(buff -> {
            StatusEffect effect = Registries.STATUS_EFFECT.get(new Identifier(buff.effect));
            if (effect != null) cachedEffects.put(buff.effect, effect);
        });
    }

    public void onServerTick(MinecraftServer server) {
        buffCheckCounter++;
        if (buffCheckCounter < ModConfig.getInstance().buffCheckInterval) return;

            server.getPlayerManager().getPlayerList().forEach(player -> {
                if (player.isAlive()) applyBuffsIfNearCampfire(player);
            });

        buffCheckCounter = 0;
    }

    private void applyBuffsIfNearCampfire(ServerPlayerEntity player) {
        World world = player.getWorld();
        BlockPos playerPos = player.getBlockPos();
        ModConfig config = ModConfig.getInstance();

        for (BlockPos pos : BlockPos.iterateOutwards(playerPos, config.buffRadius, config.buffRadius, config.buffRadius)) {
            BlockState state = world.getBlockState(pos);

            if (state.getBlock() instanceof CampfireBlock && (!config.requireLitCampfire || state.get(CampfireBlock.LIT))) {
                applyBuffs(player);
                break;
            }
        }
    }

    private void applyBuffs(ServerPlayerEntity player) {
        ModConfig.getInstance().buffs.forEach(buff -> {
            StatusEffect effect = cachedEffects.get(buff.effect);
            if (effect != null && !player.hasStatusEffect(effect)) {
                player.addStatusEffect(new StatusEffectInstance(effect, buff.duration, buff.amplifier, false, true));
            }
        });
    }
}
