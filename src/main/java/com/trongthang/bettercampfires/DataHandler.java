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
            .registerTypeAdapter(CampfireInfo.class, new CampfireInfoAdapter()) // Register CampfireInfo adapter
            .create();

    // Type for the campfire list (now using String for BlockPos as the key)
    public static final Type CAMPFIRES_LIST_TYPE = new TypeToken<Map<String, CampfireInfo>>() {}.getType();

    public Path saveFilePath;

    // Initialize the world data and set up file path for saving/loading
    public void initializeCampfiresData(MinecraftServer server) {
        saveFilePath = server.getSavePath(WorldSavePath.ROOT).resolve("data/campfiresList.json");
        loadCampfiresData();
    }

    // Load the campfire data
    public void loadCampfiresData() {
        LOGGER.info("Loading campfire data from: {}", saveFilePath);

        if (Files.exists(saveFilePath)) {
            try (Reader reader = Files.newBufferedReader(saveFilePath)) {
                // Deserialize the JSON into the campfires list (now using CampfireInfo for values)
                Map<String, CampfireInfo> loadedData = GSON.fromJson(reader, CAMPFIRES_LIST_TYPE);
                if (loadedData != null) {
                    for (Map.Entry<String, CampfireInfo> entry : loadedData.entrySet()) {
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
                Utils.log("Loaded {} campfire positions from data file." + campfiresList.size());
            } catch (IOException e) {
                Utils.log("Failed to load campfire data " + e);
            }
        }
    }

    // Save the campfire data
    public void saveCampfiresData() {
        Utils.log("Saving campfire data to: {}" + saveFilePath);
        for(BlockPos key : campfiresList.keySet()){
            if(campfiresList.get(key).timeLeft <= 0){
                campfiresList.remove(key);
            }
        }
        try {
            Files.createDirectories(saveFilePath.getParent());
            try (Writer writer = Files.newBufferedWriter(saveFilePath)) {
                // Serialize the campfire list into the JSON file
                Map<String, CampfireInfo> campfiresToSave = new HashMap<>();
                for (Map.Entry<BlockPos, CampfireInfo> entry : campfiresList.entrySet()) {
                    String posStr = entry.getKey().getX() + "," + entry.getKey().getY() + "," + entry.getKey().getZ();
                    campfiresToSave.put(posStr, entry.getValue()); // Save the entire CampfireInfo object
                }
                GSON.toJson(campfiresToSave, CAMPFIRES_LIST_TYPE, writer);
            }
            LOGGER.info("Saved {} campfire positions to data file.", campfiresList.size());
        } catch (IOException e) {
            LOGGER.error("Failed to save campfire data", e);
        }
    }
}
