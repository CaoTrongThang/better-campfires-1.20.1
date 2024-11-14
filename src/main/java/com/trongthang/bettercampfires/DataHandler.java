package com.trongthang.bettercampfires;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.BlockPos;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static com.mojang.text2speech.Narrator.LOGGER;
import static com.trongthang.bettercampfires.BetterCampfires.campfiresList;

public class DataHandler {

    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(CooldownTick.class, new CooldownTickAdapter()) // Register CooldownTick adapter
            .create();

    // Type for the campfire list (now using String for BlockPos as the key)
    public static final Type CAMPFIRES_LIST_TYPE = new TypeToken<Map<String, CooldownTick>>() {}.getType();

    public Path saveFilePath;

    // Initialize the world data and set up file path for saving/loading
    public void initializeWorldData(MinecraftServer server) {
        saveFilePath = server.getSavePath(WorldSavePath.PLAYERDATA).resolve("campfiresList.json");
        loadCampfiresData();
    }

    // Load the campfire data
    public void loadCampfiresData() {
        LOGGER.info("Loading campfire data from: {}", saveFilePath);

        if (Files.exists(saveFilePath)) {
            try (Reader reader = Files.newBufferedReader(saveFilePath)) {
                // Deserialize the JSON into the campfires list (now using CooldownTick for values)
                Map<String, CooldownTick> loadedData = GSON.fromJson(reader, CAMPFIRES_LIST_TYPE);
                if (loadedData != null) {
                    for (Map.Entry<String, CooldownTick> entry : loadedData.entrySet()) {
                        String posStr = entry.getKey();
                        String[] posParts = posStr.split(",");
                        if (posParts.length == 3) {
                            int x = Integer.parseInt(posParts[0]);
                            int y = Integer.parseInt(posParts[1]);
                            int z = Integer.parseInt(posParts[2]);
                            BlockPos pos = new BlockPos(x, y, z);
                            campfiresList.put(pos, entry.getValue());
                        }
                    }
                }
                LOGGER.info("Loaded {} campfire positions from data file.", campfiresList.size());
            } catch (IOException e) {
                LOGGER.error("Failed to load campfire data", e);
            }
        }
    }

    // Save the campfire data
    public void saveCampfiresData() {
        LOGGER.info("Saving campfire data to: {}", saveFilePath);

        try {
            Files.createDirectories(saveFilePath.getParent());
            try (Writer writer = Files.newBufferedWriter(saveFilePath)) {
                // Serialize the campfire list into the JSON file
                Map<String, CooldownTick> campfiresToSave = new HashMap<>();
                for (Map.Entry<BlockPos, CooldownTick> entry : campfiresList.entrySet()) {
                    String posStr = entry.getKey().getX() + "," + entry.getKey().getY() + "," + entry.getKey().getZ();
                    campfiresToSave.put(posStr, entry.getValue()); // Save the entire CooldownTick object
                }
                GSON.toJson(campfiresToSave, CAMPFIRES_LIST_TYPE, writer);
            }
            LOGGER.info("Saved {} campfire positions to data file.", campfiresList.size());
        } catch (IOException e) {
            LOGGER.error("Failed to save campfire data", e);
        }
    }
}
