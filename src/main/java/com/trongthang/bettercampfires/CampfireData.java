package com.trongthang.bettercampfires;

import net.minecraft.block.entity.CampfireBlockEntity;

import java.util.HashMap;
import java.util.Map;

public class CampfireData {
    private static final Map<CampfireBlockEntity, Integer> burnTimeMap = new HashMap<>();

    public static int getBurnTime(CampfireBlockEntity campfire) {
        return burnTimeMap.getOrDefault(campfire, 0); // Default to 0 if not found
    }

    public static void setBurnTime(CampfireBlockEntity campfire, int burnTime) {
        burnTimeMap.put(campfire, burnTime);
    }
}
