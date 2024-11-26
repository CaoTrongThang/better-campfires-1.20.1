package com.trongthang.bettercampfires;

public class CampfireInfo {
    public int timeLeft = 0;
    public int inRainTimeLeft = 0;
    public int inSnowTimeLeft = 0;

    // Constructor to initialize with a cooldownTime
    public CampfireInfo(){
        this.timeLeft = ModConfig.getInstance().campfiresBurnOutTime;
        this.inRainTimeLeft = ModConfig.getInstance().campfiresExtinguishByRainTime;
        this.inSnowTimeLeft = ModConfig.getInstance().campfiresExtinguishBySnowTime;
    }

    // Check if cooldown is active (time > 0)
    public boolean hasTimeLeft(){
        return this.timeLeft > 0;
    }

    public boolean hasRainTimeLeft(){
        return this.inRainTimeLeft > 0;
    }

    public boolean hasSnowTimeLeft(){
        return this.inSnowTimeLeft > 0;
    }

    // Update method that decrements time based on the provided tick
    public void updateTimeLeft(int tick){
        this.timeLeft -= tick;
    }

    public void updateInRainTimeLeft(int tick){
        this.inRainTimeLeft -= tick;
    }

    public void updateInSnowTimeLeft(int tick){
        this.inSnowTimeLeft -= tick;
    }

    public void resetRainAndSnowTime(){
        this.inRainTimeLeft = ModConfig.getInstance().campfiresExtinguishByRainTime;
        this.inSnowTimeLeft = ModConfig.getInstance().campfiresExtinguishBySnowTime;
    }
}
