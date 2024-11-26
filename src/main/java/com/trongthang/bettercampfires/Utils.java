package com.trongthang.bettercampfires;

import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static com.mojang.text2speech.Narrator.LOGGER;
import static com.trongthang.bettercampfires.BetterCampfires.campfiresList;

public class Utils {

    public static void log(Object s){
        LOGGER.info(String.valueOf(s));
    }

    public static void extinguishCampfire(ServerWorld world, BlockPos pos, BlockState state){
        world.setBlockState(pos, state.with(CampfireBlock.LIT, false), 3);
        campfiresList.remove(pos);
    }
}
