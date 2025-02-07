package com.trongthang.bettercampfires.mixin;

import com.trongthang.bettercampfires.ModConfig;
import com.trongthang.bettercampfires.CampfireBlockEntityAccess;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static com.trongthang.bettercampfires.BetterCampfires.cachedEffects;

import static com.mojang.text2speech.Narrator.LOGGER;

@Mixin(CampfireBlockEntity.class)
class CampfireBlockEntityMixin implements CampfireBlockEntityAccess {

    @Unique
    private int campfireBurntime = 0;


    private static int buffRadius = ModConfig.getInstance().buffRadius;
    private static int buffCheckCooldown = ModConfig.getInstance().buffCheckInterval;
    @Unique
    private int buffCheckCooldownCounter = 0;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onConstruct(CallbackInfo ci) {
        if (campfireBurntime == 0) {
            campfireBurntime = ModConfig.getInstance().campfiresBurnOutTime;
        }
    }

    @Inject(method = "litServerTick", at = @At("HEAD"))
    private static void litServerTick(World world, BlockPos pos, BlockState state, CampfireBlockEntity campfire, CallbackInfo ci) {
        if (campfire != null) {
            CampfireBlockEntityMixin mixin = (CampfireBlockEntityMixin) (Object) campfire;

            if (ModConfig.getInstance().campfiresCanBurnOut) {
                if (mixin.campfireBurntime > 0) {
                    mixin.campfireBurntime--;
                }

                if (mixin.campfireBurntime <= 0) {
                    world.setBlockState(pos, state.with(net.minecraft.block.CampfireBlock.LIT, false));
                }
            }

            if (ModConfig.getInstance().campfiresCanBuff) {
                mixin.buffCheckCooldownCounter++;
                if (mixin.buffCheckCooldownCounter > ModConfig.getInstance().buffCheckInterval) {
                    mixin.buffCheckCooldownCounter = 0;
                    checkAllCampfiresAndBuffEntities(pos, (ServerWorld) world);
                }
            }

            if (ModConfig.getInstance().campfiresExtinguishByRain || ModConfig.getInstance().campfiresExtinguishBySnow) {
                rainAndSnowExtinguish(world, pos, mixin);
            }
        }
    }

    // CAMPFIRE RAIN & SNOW ========================================================================
    private static void rainAndSnowExtinguish(World world, BlockPos pos, CampfireBlockEntityMixin mixin) {
        if (world.isSkyVisible(pos)) {
            boolean isInNoneRainableBiomes = world.getBiome(pos).value().getPrecipitation(pos) == Biome.Precipitation.NONE;

            if (!isInNoneRainableBiomes) {
                boolean isSnowing = world.getBiome(pos).value().getPrecipitation(pos) == Biome.Precipitation.SNOW;
                if(world.isRaining()){
                    if (isSnowing && ModConfig.getInstance().campfiresExtinguishBySnow) {
                        if (ModConfig.getInstance().snowExtinguishTimeMultiply == 0) {
                            mixin.campfireBurntime = 0;
                        }

                        mixin.campfireBurntime -= ModConfig.getInstance().snowExtinguishTimeMultiply;

                    } else if (!isSnowing && ModConfig.getInstance().campfiresExtinguishByRain) {
                        if (ModConfig.getInstance().rainExtinguishTimeMultiply == 0) {
                            mixin.campfireBurntime = 0;
                        }
                        mixin.campfireBurntime -= ModConfig.getInstance().rainExtinguishTimeMultiply;
                    }
                }
            }
        }
    }

    // Optional: Save burn time to NBT
    @Inject(method = "writeNbt", at = @At("HEAD"))
    private void writeNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt("BurnTime", this.campfireBurntime);
    }

    // Optional: Load burn time from NBT
    @Inject(method = "readNbt", at = @At("HEAD"))
    private void readNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("BurnTime")) {
            this.campfireBurntime = nbt.getInt("BurnTime");
        }
    }

    // CAMPFIRE BUFF ========================================================================
    private static void checkAllCampfiresAndBuffEntities(BlockPos center, ServerWorld world) {
        ModConfig config = ModConfig.getInstance();
        Box boundingBox = new Box(center.add(-buffRadius, -buffRadius, -buffRadius), center.add(buffRadius, buffRadius, buffRadius));

        // Get all entities within the bounding box
        List<Entity> entities = world.getEntitiesByClass(Entity.class, boundingBox, e -> true);

        for (Entity entity : entities) {
            if (entity instanceof LivingEntity livingEntity) {

                if (config.campfiresCanBuffForNonHostileMobs && !(entity instanceof HostileEntity)) {
                    applyBuffsToNonHostileMobs(livingEntity);
                }

                if (config.campfiresCanBuffForHostileMobs && entity instanceof HostileEntity) {
                    applyBuffsToHostileMobs(livingEntity);
                    if (config.campfiresCanBurnHostileMobsBasedOnBuffRadius) {
                        if (!livingEntity.isOnFire()) {
                            livingEntity.setOnFireFor(6);
                        }
                    }

                }
            }
        }
    }

    private static void applyBuffsToNonHostileMobs(LivingEntity entity) {
        // Apply the buffs to the living entity (mob or player)
        ModConfig.getInstance().buffs.forEach(buff -> {
            StatusEffect effect = cachedEffects.get(buff.effect);
            if (effect != null && !entity.hasStatusEffect(effect)) {
                // Apply the buff
                entity.addStatusEffect(new StatusEffectInstance(effect, buff.duration, buff.amplifier, false, true));
            }
        });
    }

    private static void applyBuffsToHostileMobs(LivingEntity entity) {
        // Apply the buffs to the living entity (mob or player)
        ModConfig.getInstance().hostileMobBuffs.forEach(buff -> {
            StatusEffect effect = cachedEffects.get(buff.effect);
            if (effect != null && !entity.hasStatusEffect(effect)) {
                // Apply the buff
                entity.addStatusEffect(new StatusEffectInstance(effect, buff.duration, buff.amplifier, false, true));
            }
        });
    }

    @Override
    public int getBurnTime() {
        return campfireBurntime;
    }

    @Override
    public void setBurnTime(int burnTime) {
        campfireBurntime = burnTime;
    }

}
