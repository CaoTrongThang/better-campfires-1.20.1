package com.trongthang.bettercampfires;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModConfig {
    private static final String CONFIG_FILE_NAME = "better_campfires.json";
    private static ModConfig INSTANCE;

    @Expose
    @SerializedName("campfires_can_burn_out")
    public boolean campfiresCanBurnOut = true;

    @Expose
    @SerializedName("can_check_burn_out_time_left")
    public boolean canCheckBurnOutTimeLeft = true;

    @Expose
    @SerializedName("campfires_burn_out_time")
    public int campfiresBurnOutTime = 7200;

    @Expose
    @SerializedName("campfires_extinguish_by_rain")
    public boolean campfiresExtinguishByRain = true;

    @Expose
    @SerializedName("campfires_extinguish_by_rain_time")
    public int campfiresExtinguishByRainTime = 180;

    @Expose
    @SerializedName("campfires_extinguish_by_snow")
    public boolean campfiresExtinguishBySnow = true;

    @Expose
    @SerializedName("campfires_extinguish_by_snow_time")
    public int campfiresExtinguishBySnowTime = 1200;

    @Expose
    @SerializedName("campfires_can_buff")
    public boolean campfiresCanBuff = true;

    @Expose
    @SerializedName("campfires_can_buff_for_non_hostile_mobs")
    public boolean campfiresCanBuffForNonHostileMobs = true;

    @Expose
    @SerializedName("campfires_can_buff_for_hostile_mobs")
    public boolean campfiresCanBuffForHostileMobs = true;

    @Expose
    @SerializedName("campfires_can_burn_hostile_mobs_based_on_buff_radius")
    public boolean campfiresCanBurnHostileMobsBasedOnBuffRadius = true;

    @Expose
    @SerializedName("buff_radius")
    public int buffRadius = 7;

    @Expose
    @SerializedName("buff_check_interval")
    public int buffCheckInterval = 30;

    @Expose
    @SerializedName("campfires_can_cook")
    public boolean campfiresCanCook = true;

    @Expose
    @SerializedName("cook_radius")
    public int cookRadius = 4;

    @Expose
    @SerializedName("cook_check_interval")
    public int cookCheckInterval = 20;

    @Expose
    @SerializedName("buffs")
    public List<BuffConfig> buffs = List.of(
            new BuffConfig("minecraft:regeneration", 200, 0),
            new BuffConfig("minecraft:resistance", 200, 0)
    );

    @Expose
    @SerializedName("hostile_mob_buffs")
    public List<BuffConfig> hostileMobBuffs = List.of(
            new BuffConfig("minecraft:weakness", 100, 0),
            new BuffConfig("minecraft:slowness", 100, 0)
    );

    @Expose
    @SerializedName("cookable_items")
    private List<ItemCanCook> itemsCanCook = List.of(
            new ItemCanCook("minecraft:cod", 200, "minecraft:cooked_cod"),
            new ItemCanCook("minecraft:salmon", 150, "minecraft:cooked_salmon"),
            new ItemCanCook("minecraft:beef", 300, "minecraft:cooked_beef"),
            new ItemCanCook("minecraft:chicken", 200, "minecraft:cooked_chicken"),
            new ItemCanCook("minecraft:mutton", 200, "minecraft:cooked_mutton"),
            new ItemCanCook("minecraft:porkchop", 250, "minecraft:cooked_porkchop"),
            new ItemCanCook("minecraft:rabbit", 200, "minecraft:cooked_rabbit"),
            new ItemCanCook("minecraft:potato", 100, "minecraft:baked_potato"),
            new ItemCanCook("minecraft:grass_block", 200, "minecraft:dirt") // Not typically cooked, just for fun.
    );

    @Expose
    @SerializedName("campfire_fuels")
    public List<CampfireFuels> campfireFuels = List.of(
            new CampfireFuels("minecraft:stick", 100),
            new CampfireFuels("minecraft:oak_log", 800),
            new CampfireFuels("minecraft:birch_log", 800),
            new CampfireFuels("minecraft:spruce_log", 800),
            new CampfireFuels("minecraft:jungle_log", 800),
            new CampfireFuels("minecraft:acacia_log", 800),
            new CampfireFuels("minecraft:dark_oak_log", 800),
            new CampfireFuels("minecraft:oak_planks", 200),
            new CampfireFuels("minecraft:birch_planks", 200),
            new CampfireFuels("minecraft:spruce_planks", 200),
            new CampfireFuels("minecraft:jungle_planks", 200),
            new CampfireFuels("minecraft:acacia_planks", 200),
            new CampfireFuels("minecraft:dark_oak_planks", 200),
            new CampfireFuels("minecraft:coal", 1200),
            new CampfireFuels("minecraft:charcoal", 1200),
            new CampfireFuels("minecraft:coal_block", 9600)
    );

    public List<CookableItem> cookableItems = new ArrayList<>();

    public static class BuffConfig {
        @Expose
        public String effect;
        @Expose
        public int duration;
        @Expose
        public int amplifier;

        public BuffConfig(String effect, int duration, int amplifier) {
            this.effect = effect;
            this.duration = duration;
            this.amplifier = amplifier;
        }
    }

    public static class CookableItem {
        public final Item rawItem;
        public final Item cookedItem;
        public final int cookTime;

        public CookableItem(Item rawItem, Item cookedItem, int cookTime) {
            this.rawItem = rawItem;
            this.cookedItem = cookedItem;
            this.cookTime = cookTime;
        }
    }

    public static class ItemCanCook {
        @Expose
        public String rawItem;
        @Expose
        public int cookTime;
        @Expose
        public String cookedItem;

        public ItemCanCook(String rawItem, int cookTime, String cookedItem) {
            this.rawItem = rawItem;
            this.cookTime = cookTime;
            this.cookedItem = cookedItem;
        }
    }

    public static class CampfireFuels {
        @Expose
        public String fuelId;    // Unique ID for the fuel item
        @Expose
        public int addBurnTime;     // Burn time in seconds

        // Constructor
        public CampfireFuels(String fuelId, int addBurnTime) {
            this.fuelId = fuelId;
            this.addBurnTime = addBurnTime;
        }
    }

    public static void loadConfig() {
        File configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), CONFIG_FILE_NAME);
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();

        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                INSTANCE = gson.fromJson(reader, ModConfig.class);
                if (INSTANCE == null) {
                    INSTANCE = new ModConfig(); // Fallback to default if JSON was empty or malformed
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            INSTANCE = new ModConfig();
        }

        saveConfig(gson, configFile); // Save current config, including defaults if they were missing
    }

    public void initializeCookableItems() {
        cookableItems.clear();
        Set<Item> addedRawItems = new HashSet<>();

        for (ItemCanCook item : itemsCanCook) {
            Identifier rawItemId = new Identifier(item.rawItem);
            Identifier cookedItemId = new Identifier(item.cookedItem);

            Item rawItem = Registries.ITEM.get(rawItemId);
            Item cookedItem = Registries.ITEM.get(cookedItemId);

            // Improved logging for non-existing items
            if (rawItem == Items.AIR) {
                Utils.log(BetterCampfires.MOD_ID + " - Raw item not found in registry: " + rawItemId + " | This mod currently only works with Vanilla items, not items from other mods.");
                continue; // Skip if raw item does not exist
            }
            if (cookedItem == Items.AIR) {
                Utils.log(BetterCampfires.MOD_ID + " - Cooked item not found in registry: " + cookedItemId + " | This mod currently only works with Vanilla items, not items from other mods.");
                continue; // Skip if cooked item does not exist
            }

            // Only add the cookable item if the raw item has not been added yet
            if (addedRawItems.add(rawItem)) {
                cookableItems.add(new CookableItem(rawItem, cookedItem, item.cookTime));
                BetterCampfires.LOGGER.info(BetterCampfires.MOD_ID + " - from " + rawItem + " to " + cookedItem + " in " + item.cookTime + " ticks");
            } else {
                Utils.log(BetterCampfires.MOD_ID + " - Duplicate raw item found: " + rawItem + ". Skipping.");
            }
        }
    }

    private static void saveConfig(Gson gson, File configFile) {
        try (FileWriter writer = new FileWriter(configFile)) {
            gson.toJson(INSTANCE, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ModConfig getInstance() {
        return INSTANCE;
    }
}
