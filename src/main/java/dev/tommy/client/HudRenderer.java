
package dev.tommy.client;

import dev.tommy.client.module.ModuleManager;
import dev.tommy.client.module.modules.HudModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public final class HudRenderer {

    private HudRenderer() {}

    public static void render(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player == null || client.options.hudHidden) {
            return;
        }

        int y = 8;

        // WATERMARK
        if (enabled(HudModule.Element.WATERMARK)) {
            context.drawTextWithShadow(
                    client.textRenderer,
                    "TOMMYCLIENT",
                    8,
                    y,
                    0xFFB69CFF
            );
            y += 12;
        }

        // FPS
        if (enabled(HudModule.Element.FPS)) {
            context.drawTextWithShadow(
                    client.textRenderer,
                    "FPS  " + client.getCurrentFps(),
                    8,
                    y,
                    0xFFFFFFFF
            );
            y += 12;
        }

        // COORDINATES
        if (enabled(HudModule.Element.COORDINATES)) {
            var position = client.player.getBlockPos();

            context.drawTextWithShadow(
                    client.textRenderer,
                    "XYZ  " + position.getX()
                            + "  " + position.getY()
                            + "  " + position.getZ(),
                    8,
                    y,
                    0xFFFFFFFF
            );
            y += 12;
        }

        // DIRECTION
        if (enabled(HudModule.Element.DIRECTION)) {
            float yaw = client.player.getYaw(1.0f);

            String[] directions = {
                    "South",
                    "West",
                    "North",
                    "East"
            };

            int index = Math.floorMod(
                    Math.round(yaw / 90.0f),
                    4
            );

            context.drawTextWithShadow(
                    client.textRenderer,
                    "Facing  " + directions[index],
                    8,
                    y,
                    0xFFFFFFFF
            );
        }

        // ARMOR HUD
        if (enabled(HudModule.Element.ARMOR)) {
            int x = 8;
            int bottom = client.getWindow().getScaledHeight() - 28;

            EquipmentSlot[] armorSlots = {
                    EquipmentSlot.HEAD,
                    EquipmentSlot.CHEST,
                    EquipmentSlot.LEGS,
                    EquipmentSlot.FEET
            };

            for (EquipmentSlot slot : armorSlots) {
                ItemStack stack = client.player.getEquippedStack(slot);

                if (!stack.isEmpty()) {
                    context.drawItem(stack, x, bottom);
                    x += 20;
                }
            }
        }

        // KEYSTROKES HUD
        if (enabled(HudModule.Element.KEYSTROKES)) {
            int x = 8;
            int bottom = client.getWindow().getScaledHeight() - 78;

            drawKey(
                    context, client, "W",
                    client.options.forwardKey.isPressed(),
                    x + 20, bottom
            );

            drawKey(
                    context, client, "A",
                    client.options.leftKey.isPressed(),
                    x, bottom + 20
            );

            drawKey(
                    context, client, "S",
                    client.options.backKey.isPressed(),
                    x + 20, bottom + 20
            );

            drawKey(
                    context, client, "D",
                    client.options.rightKey.isPressed(),
                    x + 40, bottom + 20
            );

            drawKey(
                    context, client, "SPACE",
                    client.options.jumpKey.isPressed(),
                    x, bottom + 40, 60
            );
        }

        // MODULE LIST
        if (enabled(HudModule.Element.MODULE_LIST)) {
            int right = client.getWindow().getScaledWidth() - 8;
            int line = 8;

            for (var module : ModuleManager.all()) {
                if (module.isEnabled()
                        && !(module instanceof HudModule)) {

                    String label = module.name();

                    context.drawTextWithShadow(
                            client.textRenderer,
                            label,
                            right - client.textRenderer.getWidth(label),
                            line,
                            0xFFB69CFF
                    );

                    line += 11;
                }
            }
        }
    }

    // KEYSTROKE DRAWING
    private static void drawKey(
            DrawContext context,
            MinecraftClient client,
            String label,
            boolean down,
            int x,
            int y
    ) {
        drawKey(context, client, label, down, x, y, 18);
    }

    private static void drawKey(
            DrawContext context,
            MinecraftClient client,
            String label,
            boolean down,
            int x,
            int y,
            int width
    ) {
        context.fill(
                x,
                y,
                x + width,
                y + 18,
                down ? 0xFF7256A8 : 0xAA171B24
        );

        int textX = x + (
                width - client.textRenderer.getWidth(label)
        ) / 2;

        context.drawText(
                client.textRenderer,
                label,
                textX,
                y + 5,
                0xFFFFFFFF,
                false
        );
    }

    // CHECK IF HUD ELEMENT IS ENABLED
    private static boolean enabled(HudModule.Element element) {
        return ModuleManager.all().stream()
                .filter(module -> module instanceof HudModule)
                .map(module -> (HudModule) module)
                .anyMatch(module ->
                        module.element() == element
                                && module.isEnabled()
                );
    }
}
