package com.trongthang.campfirebuffs;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class CampfireBuffs implements ModInitializer {
	public static final String MOD_ID = "campfirebuffs";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private int tickCounter = 0; // Counter to throttle checks
	private final Map<String, StatusEffect> cachedEffects = new HashMap<>();

	@Override
	public void onInitialize() {
		ModConfig.loadConfig();
		cacheEffects();  // Cache the effects when initializing the mod
		LOGGER.info(MOD_ID + " has been initialized!");
		ServerTickEvents.START_SERVER_TICK.register(this::onServerTick);
	}

	private void cacheEffects() {
		for (ModConfig.BuffConfig buff : ModConfig.getInstance().buffs) {
			Identifier effectId = new Identifier(buff.effect);
			StatusEffect effect = Registries.STATUS_EFFECT.get(effectId);
			if (effect != null) {
				cachedEffects.put(buff.effect, effect);
			}
		}
	}

	private void onServerTick(MinecraftServer server) {
		tickCounter++;  // Increment the counter each tick

		// Only apply healing every CHECK_INTERVAL ticks
		if (tickCounter >= ModConfig.getInstance().checkInterval) {
			applyBuffsToNearbyPlayers(server);
			tickCounter = 0;  // Reset the counter after each interval
		}
	}

	private void applyBuffsToNearbyPlayers(MinecraftServer server) {
		server.getPlayerManager().getPlayerList().forEach(player -> {
			if (player.isAlive()) {
				applyBuffsEffect(player.getWorld(), player);
			}
		});
	}

	private void applyBuffsEffect(World world, ServerPlayerEntity player) {
		BlockPos playerPos = player.getBlockPos();
		boolean nearCampfire = false;

		if(ModConfig.getInstance().requireLitCampfire){
			// Check if the player is near a lit campfire
			for (BlockPos pos : BlockPos.iterateOutwards(playerPos, ModConfig.getInstance().buffRadius,
					ModConfig.getInstance().buffRadius, ModConfig.getInstance().buffRadius)) {

				BlockState blockState = world.getBlockState(pos);

				if (blockState.getBlock() instanceof CampfireBlock && blockState.get(CampfireBlock.LIT)) {
					nearCampfire = true;
					break;
				}
			}
		} else {
			// Check if the player is near any campfire
			for (BlockPos pos : BlockPos.iterateOutwards(playerPos, ModConfig.getInstance().buffRadius,
					ModConfig.getInstance().buffRadius, ModConfig.getInstance().buffRadius)) {

				BlockState blockState = world.getBlockState(pos);

				if (blockState.getBlock() instanceof CampfireBlock) {
					nearCampfire = true;
					break;
				}
			}
		}


		// Apply buffs if near a lit campfire
		if (nearCampfire) {
			for (ModConfig.BuffConfig buff : ModConfig.getInstance().buffs) {
				StatusEffect effect = cachedEffects.get(buff.effect);  // Use cached effect
				if (effect != null && !player.hasStatusEffect(effect)) {
					player.addStatusEffect(new StatusEffectInstance(effect, buff.duration, buff.amplifier, false, true));
				} else if (effect == null) {
					LOGGER.warn("Warning: Effect " + buff.effect + " not found for player " + player.getName().getString());
				}
			}
		}
	}
}