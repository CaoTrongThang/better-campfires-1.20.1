package com.trongthang.bettercampfires;

import com.google.gson.*;
import java.lang.reflect.Type;

public class CampfireInfoAdapter implements JsonSerializer<CampfireInfo>, JsonDeserializer<CampfireInfo> {

    @Override
    public JsonElement serialize(CampfireInfo campfireInfo, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("timeLeft", campfireInfo.timeLeft);
        jsonObject.addProperty("inRainTimeLeft", campfireInfo.inRainTimeLeft);
        jsonObject.addProperty("inSnowTimeLeft", campfireInfo.inSnowTimeLeft);
        return jsonObject;
    }

    @Override
    public CampfireInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        CampfireInfo campfireInfo = new CampfireInfo();

        // Safely handle "timeLeft"
        if (jsonObject.has("timeLeft") && !jsonObject.get("timeLeft").isJsonNull()) {
            campfireInfo.timeLeft = jsonObject.get("timeLeft").getAsInt();
        } else {
            campfireInfo.timeLeft = ModConfig.getInstance().campfiresBurnOutTime; // Default or fallback value
        }

        // Safely handle "inRainTimeLeft"
        if (jsonObject.has("inRainTimeLeft") && !jsonObject.get("inRainTimeLeft").isJsonNull()) {
            campfireInfo.inRainTimeLeft = jsonObject.get("inRainTimeLeft").getAsInt();
        } else {
            campfireInfo.inRainTimeLeft = ModConfig.getInstance().campfiresExtinguishByRainTime; // Default or fallback value
        }

        // Safely handle "inSnowTimeLeft"
        if (jsonObject.has("inSnowTimeLeft") && !jsonObject.get("inSnowTimeLeft").isJsonNull()) {
            campfireInfo.inSnowTimeLeft = jsonObject.get("inSnowTimeLeft").getAsInt();
        } else {
            campfireInfo.inSnowTimeLeft = ModConfig.getInstance().campfiresExtinguishBySnowTime; // Default or fallback value
        }

        return campfireInfo;
    }
}
