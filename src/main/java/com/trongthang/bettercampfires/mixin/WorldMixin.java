package com.trongthang.bettercampfires.mixin;

import com.trongthang.bettercampfires.CampfireInfo;
import com.trongthang.bettercampfires.ModConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.trongthang.bettercampfires.BetterCampfires.campfiresList;

@Mixin(World.class)
public class WorldMixin {
    // Intercept the setBlockState to detect block changes
    @Inject(method = "setBlockState", at = @At("HEAD"))
    private void onBlockChange(BlockPos pos, BlockState newState, int flags, CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.getInstance().campfiresCanBurnOut || ModConfig.getInstance().campfiresExtinguishByRain || ModConfig.getInstance().campfiresExtinguishBySnow) {
            World world = (World) (Object) this;
            BlockState oldState = world.getBlockState(pos);

            if (oldState.getBlock() instanceof CampfireBlock || newState.getBlock() instanceof CampfireBlock) {
                boolean wasLit = oldState.contains(CampfireBlock.LIT) && oldState.get(CampfireBlock.LIT);
                boolean isNowLit = newState.contains(CampfireBlock.LIT) && newState.get(CampfireBlock.LIT);

                // Detect if the campfire was just lit
                if (!wasLit && isNowLit) {
                    campfiresList.put(pos, new CampfireInfo());
                }

                // Detect if the campfire was just extinguished
                else if (wasLit && !isNowLit) {
                    campfiresList.remove(pos);
                }
            }

//            // Detect block break (new block state is air)
//            if (newState.isAir() && !oldState.isAir()) {
//                if (oldState.getBlock() == Blocks.CAMPFIRE) {
//                    campfiresList.remove(pos);
//                }
//            }
//            // Detect block placement (old block state is air or if the old block was snow)
//            else if (!newState.isAir() && (oldState.isAir() || oldState.getBlock() == Blocks.SNOW || oldState.getBlock() == Blocks.GRASS)) {
//                if (newState.getBlock() == Blocks.CAMPFIRE) {
//                    boolean isNewCampfireLit = newState.contains(CampfireBlock.LIT) && newState.get(CampfireBlock.LIT);
//                    if (isNewCampfireLit) {
//                        campfiresList.put(pos, new CampfireInfo());
//                    }
//                }
//            }
        }
    }
}
