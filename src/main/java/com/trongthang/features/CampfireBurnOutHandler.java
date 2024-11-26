package com.trongthang.features;

import com.trongthang.bettercampfires.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
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
            campfireCooldown.updateTimeLeft(checkInterval);
        }

        for (BlockPos key : campfiresList.keySet()) {
            var campfireCooldown = campfiresList.get(key);
            BlockState state = world.getBlockState(key);

            if(!(state.getBlock() instanceof CampfireBlock)) {
                campfiresList.remove(key);
                continue;
            }

            if (!campfireCooldown.hasTimeLeft()) {
                if (state.getBlock() instanceof CampfireBlock) {
                    Utils.extinguishCampfire(world, key, state);
                    return;
                }
            }
        }
    }
}
