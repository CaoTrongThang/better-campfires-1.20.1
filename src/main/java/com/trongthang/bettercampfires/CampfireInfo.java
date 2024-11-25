package com.trongthang.bettercampfires;

public class CampfireInfo {
    public int timeLeft = 0;

    // Constructor to initialize with a cooldownTime
    public CampfireInfo(){
        this.timeLeft = ModConfig.getInstance().campfiresBurnOutTime;
    }

    // Check if cooldown is active (time > 0)
    public boolean isCooldown(){
        return this.timeLeft > 0;
    }

    // Update method that decrements time based on the provided tick
    public void update(int tick){
        this.timeLeft -= tick;
    }
}
