package com.trongthang.bettercampfires;

public class CooldownTick {
    public float time = 0;
    public int checkInterval = 20; // Add a checkInterval field
    public int cooldownTime;

    // Constructor to initialize with a cooldownTime
    public CooldownTick(int cooldownTime){
        this.cooldownTime = cooldownTime;
        this.time = cooldownTime; // Initialize with the default cooldown time
    }

    // Check if cooldown is active (time > 0)
    public boolean isCooldown(){
        return this.time > 0;
    }

    // Set cooldown time in seconds (converted to ticks)
    public void setCooldownBySecond(float second){
        this.time = second * 20; // Convert seconds to ticks
    }

    // Update method that decrements time based on the provided tick
    public void update(int tick){
        this.time -= tick;
    }

    // Default update method that decrements time by one tick (for a single tick)
    public void update(){
        this.time -= 1;
    }

    // Reset the cooldown to the original cooldown time
    public void reset(){
        time = cooldownTime;
    }
}
