package com.trongthang.features;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import static com.trongthang.bettercampfires.BetterCampfires.campfiresList;

public class CampfireBurnOutHandler {

    int checkInterval = 60;
    int counter = 0;

    public void onServerTick(ServerWorld world) {

        counter++;
        if(counter < checkInterval) return;
        counter = 0;

        for(BlockPos key : campfiresList.keySet()) {
            var campfireCooldown = campfiresList.get(key);
            campfireCooldown.update(checkInterval);
        }

        for (BlockPos key : campfiresList.keySet()) {
            var campfireCooldown = campfiresList.get(key);
            BlockState state = world.getBlockState(key);

            if (!campfireCooldown.isCooldown()) {
                if (state.getBlock() instanceof CampfireBlock) {
                    world.setBlockState(key, state.with(CampfireBlock.LIT, false), 3);
                    campfiresList.remove(key);
                    return;
                }
            }

            if(state.isAir()) {
                campfiresList.remove(key);
            }
        }
    }
}
