package com.trongthang.features;

import com.trongthang.bettercampfires.ModConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.registry.Registries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.trongthang.bettercampfires.BetterCampfires.campfiresList;

public class CampfireBuffHandler {
    private int buffCheckCounter = 0;
    private final Map<String, StatusEffect> cachedEffects = new HashMap<>();

    public void cacheEffects() {
        ModConfig.getInstance().buffs.forEach(buff -> {
            StatusEffect effect = Registries.STATUS_EFFECT.get(new Identifier(buff.effect));
            if (effect != null) cachedEffects.put(buff.effect, effect);
        });

        ModConfig.getInstance().hostileMobBuffs.forEach(buff -> {
            StatusEffect effect = Registries.STATUS_EFFECT.get(new Identifier(buff.effect));
            if (effect != null) cachedEffects.put(buff.effect, effect);
        });
    }

    public void onServerTick(MinecraftServer server) {

        buffCheckCounter++;
        if (buffCheckCounter < ModConfig.getInstance().buffCheckInterval) return;
        buffCheckCounter = 0;

        if (ModConfig.getInstance().campfiresCanBuff) {
            ServerWorld world = server.getOverworld();
            for (BlockPos key : campfiresList.keySet()) {
                BlockState state = world.getBlockState(key);
                if(state != null){
                    if(!state.isAir()){
                        if(state.getBlock() instanceof CampfireBlock){
                            if(state.get(CampfireBlock.LIT)){
                                checkAllCampfiresAndBuffEntities(key, world);
                            } else {
                                campfiresList.remove(key);
                            }
                        }
                    } else {
                        campfiresList.remove(key);
                    }
                }
            }
        }

        buffCheckCounter = 0;
    }

    private void checkAllCampfiresAndBuffEntities(BlockPos center, ServerWorld world) {
        int buffRadius = ModConfig.getInstance().buffRadius;
        ModConfig config = ModConfig.getInstance();
        Box boundingBox = new Box(center.add(-buffRadius, -buffRadius, -buffRadius), center.add(buffRadius, buffRadius, buffRadius));

        // Get all entities within the bounding box
        List<Entity> entities = world.getEntitiesByClass(Entity.class, boundingBox, e -> true);

        for (Entity entity : entities) {
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entity;

                if(config.campfiresCanBuffForNonHostileMobs && !(entity instanceof HostileEntity)){
                    applyBuffsToNonHostileMobs(livingEntity);
                }

                if (config.campfiresCanBuffForHostileMobs && entity instanceof HostileEntity) {
                    applyBuffsToHostileMobs(livingEntity);
                    if(config.campfiresCanBurnHostileMobsBasedOnBuffRadius){
                        if(!livingEntity.isOnFire()){
                            livingEntity.setOnFireFor(6);
                        }
                    }

                }
            }
        }
    }

    private void applyBuffsToNonHostileMobs(LivingEntity entity) {
        // Apply the buffs to the living entity (mob or player)
        ModConfig.getInstance().buffs.forEach(buff -> {
            StatusEffect effect = cachedEffects.get(buff.effect);
            if (effect != null && !entity.hasStatusEffect(effect)) {
                // Apply the buff
                entity.addStatusEffect(new StatusEffectInstance(effect, buff.duration, buff.amplifier, false, true));
            }
        });
    }

    private void applyBuffsToHostileMobs(LivingEntity entity) {
        // Apply the buffs to the living entity (mob or player)
        ModConfig.getInstance().hostileMobBuffs.forEach(buff -> {
            StatusEffect effect = cachedEffects.get(buff.effect);
            if (effect != null && !entity.hasStatusEffect(effect)) {
                // Apply the buff
                entity.addStatusEffect(new StatusEffectInstance(effect, buff.duration, buff.amplifier, false, true));
            }
        });
    }
}
