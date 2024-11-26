package com.trongthang.features;

import com.trongthang.bettercampfires.CampfireInfo;
import com.trongthang.bettercampfires.ModConfig;
import com.trongthang.bettercampfires.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

import static com.trongthang.bettercampfires.BetterCampfires.LOGGER;
import static com.trongthang.bettercampfires.BetterCampfires.campfiresList;

public class RainAndSnowExtinguishCampfireHandler {
    public int checkInterval = 40;
    public int counter = 0;
    public int upDistance = 256;

    public void onServerTick(ServerWorld world) {
        counter++;
        if (counter < checkInterval) return;
        counter = 0;

        for (BlockPos key : campfiresList.keySet()) {
            CampfireInfo campfireInfo = campfiresList.get(key);
            
            BlockState state = world.getBlockState(key);

            boolean isRaining = world.isRaining();

            if(!isRaining){
                campfireInfo.resetRainAndSnowTime();
                continue;
            }

            if (!(state.getBlock() instanceof CampfireBlock)) {
                campfiresList.remove(key);
                continue;
            }

            //Check if there's any block above the campfire, also the current weather
            if (state.getBlock() instanceof CampfireBlock) {
                var checkUp = upDistance - key.getY();
                var b = false;

                for (int x = 1; x < checkUp; x++) {
                    if (!world.getBlockState(key.up(x)).isAir()) {
                        b = true;
                        break;
                    }
                }

                if (!b) {
                    boolean isInNoneRainableBiomes = world.getBiome(key).value().getPrecipitation(key) == Biome.Precipitation.NONE;

                    if (!isInNoneRainableBiomes) {
                        boolean isSnowing = world.getBiome(key).value().getPrecipitation(key) == Biome.Precipitation.SNOW;
                        if (isSnowing && ModConfig.getInstance().campfiresExtinguishBySnow) {
                            if(ModConfig.getInstance().campfiresExtinguishBySnowTime == 0){
                                Utils.extinguishCampfire(world, key, state);
                                continue;
                            }
                            campfireInfo.updateInSnowTimeLeft(checkInterval);
                            if (!campfireInfo.hasSnowTimeLeft()) {
                                Utils.extinguishCampfire(world, key, state);
                            }
                        } else if(!isSnowing && ModConfig.getInstance().campfiresExtinguishByRain) {
                            if(ModConfig.getInstance().campfiresExtinguishByRainTime == 0){
                                Utils.extinguishCampfire(world, key, state);
                                continue;
                            }
                            campfireInfo.updateInRainTimeLeft(checkInterval);

                            if (!campfireInfo.hasRainTimeLeft()) {
                                Utils.extinguishCampfire(world, key, state);
                            }
                        }
                    }
                } else {
                    campfireInfo.resetRainAndSnowTime();
                }
            }
        }
    }
}
