package com.trongthang.features;

import com.trongthang.bettercampfires.CooldownTick;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import static com.trongthang.bettercampfires.BetterCampfires.campfiresList;

public class CampfireBurnOutHandler {

    CooldownTick cooldownTick = new CooldownTick(20);

    public void onServerTick(ServerWorld world) {

        cooldownTick.update();
        if(cooldownTick.isCooldown()) return;
        cooldownTick.reset();

        for(BlockPos key : campfiresList.keySet()) {
            var campfireCooldown = campfiresList.get(key);
            campfireCooldown.update(cooldownTick.checkInterval);
        }

        for(BlockPos key : campfiresList.keySet().stream().toList()){
            var campfireCooldown = campfiresList.get(key);
            if(!campfireCooldown.isCooldown()){
                BlockState state = world.getBlockState(key);
                world.setBlockState(key, state.with(CampfireBlock.LIT, false), 3);
                campfiresList.remove(key);
            }
        }
    }
}
