package com.trongthang.bettercampfires.mixin;

import com.trongthang.bettercampfires.ModConfig;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.trongthang.bettercampfires.BetterCampfires.*;

@Mixin(CampfireBlock.class)
public class CampfireBlockFuel {
    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    public void onPlayerInteract(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        // Only proceed if it's not a client-side interaction and the campfire is lit
        if (!world.isClient && state.get(CampfireBlock.LIT) && !ModConfig.getInstance().campfireFuels.isEmpty() && ModConfig.getInstance().campfiresCanBurnOut) {
            ItemStack heldItem = player.getStackInHand(hand);
            String fuelId = heldItem.getItem().getTranslationKey();

            // Normalize the translation key for block or item
            // Strip "item.minecraft." or "block.minecraft." and ensure it matches "minecraft:item_id" or "minecraft:block_id"
            String itemId = fuelId.replace("item.minecraft.", "minecraft:").replace("block.minecraft.", "minecraft:");

            boolean fuelFound = false; // Flag to track if we found the fuel
            for (ModConfig.CampfireFuels fuel : ModConfig.getInstance().campfireFuels) {
                if (fuel.fuelId.equals(itemId)) {
                    fuelFound = true;

                    if (heldItem.getCount() > 0) {
                        heldItem.decrement(1);

                        int fuelTime = fuel.addBurnTime;

                        // Update the campfire's cooldown list
                        campfiresList.computeIfPresent(pos, (p, cooldown) -> {
                            cooldown.time += fuelTime;
                            return cooldown;
                        });

                        // Send network packet if needed
                        ServerPlayNetworking.send(player.getServer().getPlayerManager().getPlayer(player.getUuid()), PLAY_BLOCK_LAVA_EXTINGUISH, PacketByteBufs.empty());



                        // Set the return value to indicate success
                        cir.setReturnValue(ActionResult.SUCCESS);
                        cir.cancel();
                    }
                    break; // Exit the loop once the fuel is found
                }
            }
        }
    }
}
