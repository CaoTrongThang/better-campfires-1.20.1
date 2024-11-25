package com.trongthang.bettercampfires;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import static com.mojang.text2speech.Narrator.LOGGER;

public class Utils {
    public static void log(Object s){
        LOGGER.info(String.valueOf(s));
    }
}
