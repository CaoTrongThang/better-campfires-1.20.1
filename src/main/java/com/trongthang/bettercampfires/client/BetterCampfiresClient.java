package com.trongthang.bettercampfires.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

import static com.trongthang.bettercampfires.BetterCampfires.PLAY_BLOCK_LAVA_EXTINGUISH;

public class BetterCampfiresClient implements ClientModInitializer  {

    private static final Map<BlockPos, Integer> burnOutTimes = new HashMap<>();

    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(PLAY_BLOCK_LAVA_EXTINGUISH, (client, handler, buf, responseSender) -> {
            client.execute(() -> {
                // Play sound locally
                client.player.playSound(SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.PLAYERS, 0.7f, 1f);
            });
        });
    }
}
