package com.trongthang.bettercampfires;

import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static com.trongthang.bettercampfires.BetterCampfires.LOGGER;

public class Utils {

    public static void log(Object s){
        LOGGER.info(String.valueOf(s));
    }

    static ConcurrentHashMap<UUID, RunAfter> runnableList = new ConcurrentHashMap();

    public static void addRunAfter(Runnable runFunction, int afterTicks) {
        UUID taskId = UUID.randomUUID();
        runnableList.put(taskId, new RunAfter(runFunction, afterTicks));
    }

    public static void onServerTick(MinecraftServer server) {
        for (UUID key : runnableList.keySet()) {
            RunAfter runTask = runnableList.get(key);

            runTask.runAfterInTick--;
            if (runTask.runAfterInTick <= 0) {
                runTask.functionToRun.run();
                runnableList.remove(key);
            }
        }
    }
}
