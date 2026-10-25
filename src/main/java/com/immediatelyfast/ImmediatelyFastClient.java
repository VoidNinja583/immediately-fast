package com.immediatelyfast;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

public class ImmediatelyFastClient implements ClientModInitializer {
    public static boolean safeAnchor = true;
    public static boolean anchorSwitch = false;
    public static boolean autoTotem = true;
    public static int switchDelay = 2;
    public static int totemDelay = 2;

    private static KeyBinding openConfig;
    private static int switchTicks = 0;
    private static int totemTicks = 0;
    private static int switchStep = 0;

    @Override
    public void onInitializeClient() {
        openConfig = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.immediately_fast.config",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.immediately_fast"
        ));

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (!safeAnchor || world.getRegistryKey() != World.OVERWORLD) return ActionResult.PASS;
            if (!(world.getBlockState(hitResult.getBlockPos()).getBlock() instanceof RespawnAnchorBlock)) {
                return ActionResult.PASS;
            }
            boolean hasTotem = player.getOffHandStack().isOf(Items.TOTEM_OF_UNDYING)
                    || player.getMainHandStack().isOf(Items.TOTEM_OF_UNDYING);
            return hasTotem ? ActionResult.PASS : ActionResult.FAIL;
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openConfig.wasPressed()) {
                if (client.currentScreen == null) client.setScreen(new ImmediatelyFastConfigScreen());
            }
            if (client.player == null || client.world == null || client.interactionManager == null) return;

            if (anchorSwitch) {
                if (++switchTicks >= Math.max(1, switchDelay)) {
                    switchTicks = 0;
                    runAnchorSwitch(client);
                }
            } else {
                switchTicks = 0;
                switchStep = 0;
            }

            if (autoTotem) {
                if (++totemTicks >= Math.max(1, totemDelay)) {
                    totemTicks = 0;
                    runAutoTotem(client);
                }
            } else {
                totemTicks = 0;
            }
        });
    }

    private static void runAnchorSwitch(MinecraftClient client) {
        if (client.player == null || client.interactionManager == null) return;
        // Cycle the selected hotbar slot to the next matching item in the sequence.
        net.minecraft.item.Item[] sequence = { Items.RESPAWN_ANCHOR, Items.GLOWSTONE, Items.TOTEM_OF_UNDYING };
        int selected = client.player.getInventory().getSelectedSlot();
        if (switchStep >= sequence.length) switchStep = 0;
        net.minecraft.item.Item wanted = sequence[switchStep];
        for (int i = 0; i < 9; i++) {
            if (client.player.getInventory().getStack(i).isOf(wanted)) {
                client.player.getInventory().setSelectedSlot(i);
                switchStep = (switchStep + 1) % sequence.length;
                return;
            }
        }
        // If the next item is not in the hotbar, do not silently move inventory items.
    }

    private static void runAutoTotem(MinecraftClient client) {
        if (client.player == null || client.interactionManager == null || client.currentScreen != null) return;
        var player = client.player;
        var handler = player.playerScreenHandler;

        // Put a totem into an empty offhand; preserve any item already there.
        if (player.getOffHandStack().isEmpty()) {
            for (int slot = 9; slot <= 35; slot++) {
                if (handler.getSlot(slot).getStack().isOf(Items.TOTEM_OF_UNDYING)) {
                    client.interactionManager.clickSlot(handler.syncId, slot, 40, SlotActionType.SWAP, player);
                    return;
                }
            }
            for (int slot = 0; slot <= 8; slot++) {
                if (slot != player.getInventory().getSelectedSlot()
                        && handler.getSlot(slot).getStack().isOf(Items.TOTEM_OF_UNDYING)) {
                    client.interactionManager.clickSlot(handler.syncId, slot, 40, SlotActionType.SWAP, player);
                    return;
                }
            }
        }

        // If the selected main-hand slot is empty, place a totem there from the main inventory.
        int selected = player.getInventory().getSelectedSlot();
        if (player.getInventory().getStack(selected).isEmpty()) {
            for (int slot = 9; slot <= 35; slot++) {
                if (handler.getSlot(slot).getStack().isOf(Items.TOTEM_OF_UNDYING)) {
                    client.interactionManager.clickSlot(handler.syncId, slot, selected, SlotActionType.SWAP, player);
                    return;
                }
            }
        }
    }

    public static Text status(boolean value) {
        return Text.literal(value ? "ON" : "OFF");
    }
}
