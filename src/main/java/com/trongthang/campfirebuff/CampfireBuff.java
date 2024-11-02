package com.trongthang.campfirebuff;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.Block;
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

public class CampfireBuff implements ModInitializer {
	public static final String MOD_ID = "campfirebuff";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private int tickCounter = 0; // Counter to throttle checks

	@Override
	public void onInitialize() {

	ModConfig.loadConfig();

	LOGGER.info(MOD_ID + " has been initialized!");
		ServerTickEvents.START_SERVER_TICK.register(this::onServerTick);
	}

	private void onServerTick(MinecraftServer server) {
		tickCounter++;  // Increment the counter each tick

		// Only apply healing every CHECK_INTERVAL ticks
		if (tickCounter >= ModConfig.getInstance().checkInterval) {
			server.getPlayerManager().getPlayerList().forEach(player -> {
				applyHealingEffect(player.getWorld(), player);
			});
			tickCounter = 0;  // Reset the counter after each interval
		}
	}

	private void applyHealingToNearbyPlayers(MinecraftServer server) {
		server.getPlayerManager().getPlayerList().forEach(player -> {
			applyHealingEffect(player.getWorld(), player);
		});
	}

	private void applyHealingEffect(World world, ServerPlayerEntity player) {
		// Check if the player already has any of the buffs
		for (ModConfig.BuffConfig buff : ModConfig.getInstance().buffs) {
			Identifier effectId = new Identifier(buff.effect);
			StatusEffect effect = Registries.STATUS_EFFECT.get(effectId); // Updated for new Registry usage

			if (effect != null && player.hasStatusEffect(effect)) {
				// If player already has the effect, do not apply it again
				return;
			}
		}

		BlockPos playerPos = player.getBlockPos();
		boolean nearCampfire = false;
		Block campfireBlock = net.minecraft.block.Blocks.CAMPFIRE;

		// Check if the player is near a campfire
		for (BlockPos pos : BlockPos.iterateOutwards(playerPos, ModConfig.getInstance().buffRadius,
				ModConfig.getInstance().buffRadius, ModConfig.getInstance().buffRadius)) {
			if (world.getBlockState(pos).getBlock() == campfireBlock && world.getBlockState(pos).get(CampfireBlock.LIT) == true) {
				nearCampfire = true;
				break;
			}
		}

		// Apply buffs if near a campfire
		if (nearCampfire) {
			for (ModConfig.BuffConfig buff : ModConfig.getInstance().buffs) {
				Identifier effectId = new Identifier(buff.effect);
				StatusEffect effect = Registries.STATUS_EFFECT.get(effectId); // Updated for new Registry usage

				if (effect != null) {
					// Check again if the player has the effect before applying it
					if (!player.hasStatusEffect(effect)) {
						player.addStatusEffect(new StatusEffectInstance(effect, buff.duration, buff.amplifier, false, true));
					}
				} else {
					System.out.println("Warning: Effect " + buff.effect + " not found for player " + player.getName().getString());
				}
			}
		}
	}

}