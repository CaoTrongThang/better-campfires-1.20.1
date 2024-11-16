package com.trongthang.features;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import static com.trongthang.bettercampfires.BetterCampfires.campfiresList;

public class RainExtinguishCampfireHandler {
    public int checkInterval = 10;
    public int counter = 0;
    public int upDistance = 256;

    public void onServerTick(ServerWorld world) {
        counter++;
        if (counter < checkInterval) return;
        counter = 0;

        if (!world.isRaining()) return;
        var mc = MinecraftClient.getInstance();
        if(mc.player == null) return;

        for(BlockPos key : campfiresList.keySet()){
            BlockState state = world.getBlockState(key);

            if(state.isAir()) {
                campfiresList.remove(key);
            }

            if(state.getBlock() instanceof CampfireBlock) {
                var checkUp = upDistance - key.getY();
                var b = false;
                for(int x = 1; x < checkUp; x++){
                    if (!world.getBlockState(key.up(x)).isAir()) {
                        b = true;
                        break;
                    }
                }
                if(!b){
                        world.setBlockState(key, state.with(CampfireBlock.LIT, false), 3);
                        campfiresList.remove(key);
                }
            }
        }
    }
}
