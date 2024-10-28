package com.trongthang.campfirebuff;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ModConfig {
    private static final String CONFIG_FILE_NAME = "campfire_buff.json";
    private static ModConfig INSTANCE;

    @Expose
    @SerializedName("healing_radius")
    public int buffRadius = 5;

    @Expose
    @SerializedName("check_interval")
    public int checkInterval = 20; // Check every 20 ticks

    @Expose
    @SerializedName("buffs")
    public List<BuffConfig> buffs = List.of(new BuffConfig("minecraft:regeneration", 200, 0)
    , new BuffConfig("minecraft:resistance", 200, 0)); // Default regeneration

    public static class BuffConfig {
        @Expose
        @SerializedName("effect")
        public String effect; // Effect identifier

        @Expose
        @SerializedName("duration")
        public int duration; // Duration in ticks

        @Expose
        @SerializedName("amplifier")
        public int amplifier; // Effect level

        public BuffConfig(String effect, int duration, int amplifier) {
            this.effect = effect;
            this.duration = duration;
            this.amplifier = amplifier;
        }
    }

    public static void loadConfig() {
        File configFile = new File(FabricLoader.getInstance().getConfigDirectory(), CONFIG_FILE_NAME);
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();

        // Load configuration from file if it exists
        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                INSTANCE = gson.fromJson(reader, ModConfig.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Create a default config file if it doesn't exist
            INSTANCE = new ModConfig();
            try (FileWriter writer = new FileWriter(configFile)) {
                writer.write(gson.toJson(INSTANCE));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static ModConfig getInstance() {
        return INSTANCE;
    }
}