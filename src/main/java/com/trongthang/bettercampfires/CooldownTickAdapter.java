package com.trongthang.bettercampfires;

import com.google.gson.*;
import java.lang.reflect.Type;

public class CooldownTickAdapter implements JsonSerializer<CooldownTick>, JsonDeserializer<CooldownTick> {

    @Override
    public JsonElement serialize(CooldownTick cooldown, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("time", cooldown.time);
        jsonObject.addProperty("checkInterval", cooldown.checkInterval);
        jsonObject.addProperty("cooldownTime", cooldown.cooldownTime); // Add cooldownTime
        return jsonObject;
    }

    @Override
    public CooldownTick deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        float time = jsonObject.get("time").getAsFloat();
        int checkInterval = jsonObject.get("checkInterval").getAsInt();
        int cooldownTime = jsonObject.get("cooldownTime").getAsInt(); // Deserialize cooldownTime
        CooldownTick cooldown = new CooldownTick(cooldownTime);
        cooldown.time = time;
        cooldown.checkInterval = checkInterval;
        return cooldown;
    }
}
