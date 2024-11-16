package com.trongthang.features;

import com.trongthang.bettercampfires.ModConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CampfireCookHandler {
    private int cookCheckCounter = 0;
    private static final Map<ItemEntity, Long> dropTimes = new HashMap<>();

    public void onServerTick(MinecraftServer server) {
        cookCheckCounter++;
        if (cookCheckCounter < ModConfig.getInstance().cookCheckInterval) return;

        for (World world : server.getWorlds()) {
            checkNearbyItemsForCooking(world);
        }
        cookCheckCounter = 0;
    }

    private void checkNearbyItemsForCooking(World world) {
        ModConfig config = ModConfig.getInstance();
        for (var player : world.getPlayers()) {
            Vec3d playerPos = player.getPos();
            Box scanBox = createScanBoxAroundPlayer(playerPos);
            List<ItemEntity> nearbyItems = world.getEntitiesByClass(ItemEntity.class, scanBox, itemEntity -> true);

            for (ItemEntity itemEntity : nearbyItems) {
                processItemForCooking(itemEntity, config);
            }
        }
    }

    private Box createScanBoxAroundPlayer(Vec3d playerPos) {
        return new Box(playerPos.x - 32, playerPos.y - 32, playerPos.z - 32,
                playerPos.x + 32, playerPos.y + 32, playerPos.z + 32);
    }

    private void processItemForCooking(ItemEntity itemEntity, ModConfig config) {
        ItemStack itemStack = itemEntity.getStack();
        ModConfig.CookableItem cookableItem = findCookableItem(itemStack.getItem());

        if (cookableItem != null) {
            dropTimes.putIfAbsent(itemEntity, System.currentTimeMillis());

            if (isItemNearCampfire(itemEntity.getWorld(), itemEntity.getBlockPos(), config.cookRadius)) {
                long elapsedCookingTime = System.currentTimeMillis() - dropTimes.get(itemEntity);
                if (elapsedCookingTime >= cookableItem.cookTime * 50L) {
                    cookItem(itemEntity, cookableItem);
                }
            } else {
                dropTimes.remove(itemEntity); // Remove if item is out of range
            }
        }
    }

    private void cookItem(ItemEntity itemEntity, ModConfig.CookableItem cookableItem) {


        itemEntity.setStack(new ItemStack(cookableItem.cookedItem, itemEntity.getStack().getCount()));
        dropTimes.remove(itemEntity);
    }

    private boolean isItemNearCampfire(World world, BlockPos pos, int radius) {
        BlockPos.Mutable mutablePos = new BlockPos.Mutable();
        ModConfig config = ModConfig.getInstance();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -radius; z <= radius; z++) {
                    mutablePos.set(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                    BlockState state = world.getBlockState(mutablePos);

                    if (state.isOf(Blocks.CAMPFIRE) && state.get(CampfireBlock.LIT)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private ModConfig.CookableItem findCookableItem(Item item) {
        return ModConfig.getInstance().cookableItems.stream()
                .filter(cookable -> cookable.rawItem == item) // Use '==' for direct instance comparison
                .findFirst()
                .orElse(null);
    }
}
