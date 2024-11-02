package com.trongthang.campfirebuffs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ModConfig {
    private static final String CONFIG_FILE_NAME = "campfire_buffs.json";
    private static ModConfig INSTANCE;

    @Expose
    @SerializedName("buff_radius")
    public int buffRadius = 6;

    @Expose
    @SerializedName("check_interval")
    public int checkInterval = 30;

    @Expose
    @SerializedName("require_lit_campfire")
    public boolean requireLitCampfire = true;

    @Expose
    @SerializedName("buffs")
    public List<BuffConfig> buffs = List.of(
            new BuffConfig("minecraft:regeneration", 200, 0),
            new BuffConfig("minecraft:resistance", 200, 0)
    ); // Default regeneration and resistance effects

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
                JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

                // Check if "require_lit_campfire" is missing in the JSON file
                boolean requireLitCampfireMissing = !jsonObject.has("require_lit_campfire");

                // Load the full config as an instance of ModConfig
                INSTANCE = gson.fromJson(jsonObject, ModConfig.class);

                // If requireLitCampfire was missing, set the default value and save the config
                if (requireLitCampfireMissing) {
                    INSTANCE.requireLitCampfire = true;  // Set default value for requireLitCampfire
                    saveConfig(gson, configFile);  // Save updated config with new field
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Create a default config file if it doesn't exist
            INSTANCE = new ModConfig();
            saveConfig(gson, configFile);
        }
    }

    private static void saveConfig(Gson gson, File configFile) {
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write(gson.toJson(INSTANCE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ModConfig getInstance() {
        return INSTANCE;
    }
}
