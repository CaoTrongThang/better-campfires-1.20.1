package com.trongthang.bettercampfires.mixin;

import com.trongthang.bettercampfires.CampfireBlockEntityAccess;
import com.trongthang.bettercampfires.ModConfig;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.trongthang.bettercampfires.BetterCampfires.*;

@Mixin(CampfireBlock.class)
public class CampfireBlockFuelAndLitFlint {
    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    public void onPlayerInteract(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (!world.isClient && !ModConfig.getInstance().campfireFuels.isEmpty() && ModConfig.getInstance().campfiresCanBurnOut) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            CampfireBlockEntityAccess campfireBlockEntityAccess = null;
            ItemStack heldItem = player.getStackInHand(hand);

            if (blockEntity instanceof CampfireBlockEntity) {
                campfireBlockEntityAccess = (CampfireBlockEntityAccess) blockEntity;
            }

            if (campfireBlockEntityAccess == null) return;

            if (!state.get(CampfireBlock.LIT)) {
                if (heldItem.isOf(Items.FLINT_AND_STEEL)) {
                    world.setBlockState(pos, state.with(CampfireBlock.LIT, true));
                    campfireBlockEntityAccess.setBurnTime(ModConfig.getInstance().campfiresBurnOutTime);
                    heldItem.damage(1, player, p -> p.sendToolBreakStatus(hand));
                    cir.setReturnValue(ActionResult.SUCCESS);
                }
            } else {
                Item item = heldItem.getItem();
                Identifier itemIdentifier = Registries.ITEM.getId(item);
                String itemIdString = itemIdentifier.toString();

                for (ModConfig.CampfireFuels fuel : ModConfig.getInstance().campfireFuels) {
                    String fuelId = fuel.fuelId;
                    if (fuelId.startsWith("#")) {
                        String tagName = fuelId.substring(1);
                        Identifier tagIdentifier = new Identifier(tagName);
                        TagKey<Item> tagKey = TagKey.of(RegistryKeys.ITEM, tagIdentifier);
                        if (heldItem.isIn(tagKey)) {
                            handleFuelConsumption(player, fuel, heldItem, campfireBlockEntityAccess, cir);
                            break;
                        }
                    } else {
                        if (itemIdString.equals(fuelId)) {
                            handleFuelConsumption(player, fuel, heldItem, campfireBlockEntityAccess, cir);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void handleFuelConsumption(PlayerEntity player, ModConfig.CampfireFuels fuel, ItemStack heldItem, CampfireBlockEntityAccess campfireBlockEntityAccess, CallbackInfoReturnable<ActionResult> cir) {
        if (heldItem.getCount() > 0) {
            heldItem.decrement(1);
            int fuelTime = fuel.addBurnTime;
            int currentBurnTime = campfireBlockEntityAccess.getBurnTime();
            campfireBlockEntityAccess.setBurnTime(currentBurnTime + fuelTime);
            ServerPlayNetworking.send(player.getServer().getPlayerManager().getPlayer(player.getUuid()), PLAY_BLOCK_LAVA_EXTINGUISH, PacketByteBufs.empty());
            cir.setReturnValue(ActionResult.SUCCESS);
            cir.cancel();
        }
    }
}

