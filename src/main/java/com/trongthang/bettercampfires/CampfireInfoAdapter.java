package com.trongthang.bettercampfires;

import com.google.gson.*;
import java.lang.reflect.Type;

public class CampfireInfoAdapter implements JsonSerializer<CampfireInfo>, JsonDeserializer<CampfireInfo> {

    @Override
    public JsonElement serialize(CampfireInfo cooldown, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("timeLeft", cooldown.timeLeft);
        return jsonObject;
    }

    @Override
    public CampfireInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        int time = jsonObject.get("timeLeft").getAsInt();
        CampfireInfo cooldown = new CampfireInfo();
        cooldown.timeLeft = time;
        return cooldown;
    }
}
