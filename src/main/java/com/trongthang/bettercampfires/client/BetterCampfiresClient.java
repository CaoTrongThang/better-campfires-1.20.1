package com.trongthang.bettercampfires.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import static com.trongthang.bettercampfires.BetterCampfires.PLAY_BLOCK_LAVA_EXTINGUISH;

public class BetterCampfiresClient implements ClientModInitializer  {
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(PLAY_BLOCK_LAVA_EXTINGUISH, (client, handler, buf, responseSender) -> {
            client.execute(() -> {
                // Play sound locally
                client.player.playSound(SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.PLAYERS, 0.7f, 1f);
            });

        });
    }
}
