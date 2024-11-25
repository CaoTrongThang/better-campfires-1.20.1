package com.trongthang.features;

import com.trongthang.bettercampfires.CampfireInfo;
import com.trongthang.bettercampfires.ModConfig;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

import static com.trongthang.bettercampfires.BetterCampfires.LOGGER;
import static com.trongthang.bettercampfires.BetterCampfires.campfiresList;

public class GetCampfireBurnTimeLeft {

    private static final GetCampfireBurnTimeLeft INSTANCE = new GetCampfireBurnTimeLeft();
    Random rand = new Random();

    private int counter = 0;

    // Singleton to ensure this class is only instantiated once
    public static GetCampfireBurnTimeLeft getInstance() {
        return INSTANCE;
    }

    public static final List<String> messages = List.of(
            "Looks like this fire will last for about",
            "This campfire should burn for roughly",
            "I think this fire will burn for at least",
            "It seems like the flames will last around",
            "Maybe it’ll burn for a good bit, around",
            "The campfire will probably stay lit for about",
            "Looks like it'll last a decent time, around",
            "Seems like the flames will stick around for roughly",
            "It should burn for a while, around ",
            "I’d say it'll burn for about"
    );

    // Register the event once
    public void handleSendingCampfiresBurnTime() {
        if (!ModConfig.getInstance().canCheckBurnOutTimeLeft) return;
        UseBlockCallback.EVENT.register(this::onRightClickBlock);
    }

    private ActionResult onRightClickBlock(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {

        // Check if the block the player right-clicked is a Campfire
        if (world.getBlockState(hitResult.getBlockPos()).getBlock() instanceof CampfireBlock) {
            CampfireInfo campfire = campfiresList.get(hitResult.getBlockPos());
            if (campfire != null) {
                // Only send the message from the server side
                if (!world.isClient) {
                    if(player.getPose() != EntityPose.CROUCHING && player.getMainHandStack().isEmpty()){
                        counter++;
                        if((counter % 2) == 1){
                            String randomMessage = messages.get(rand.nextInt(messages.size()));
                            String timeLeftMessage = " " + (campfire.timeLeft / 20) + "s"; // Convert to seconds

                            Text message = Text.literal(randomMessage)
                                    .append(Text.literal(timeLeftMessage).styled(style -> style.withColor(Formatting.YELLOW)));

                            // Send the message to the player
                            player.sendMessage(message, false);
                        } else {
                            counter = 0;
                        }


                    }
                }
            }
        }

        // Return PASS to allow other interactions to happen
        return ActionResult.PASS;
    }
}
