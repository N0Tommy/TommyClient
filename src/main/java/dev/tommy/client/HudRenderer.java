package dev.tommy.client;

import dev.tommy.client.module.ModuleManager;
import dev.tommy.client.module.modules.HudModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;

public final class HudRenderer {
    private HudRenderer() {}

    public static void render(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.options.hudHidden) return;

        int y = 8;
        if (enabled(HudModule.Element.WATERMARK)) {
            context.drawTextWithShadow(client.textRenderer, "TOMMYCLIENT", 8, y, 0xFFB69CFF);
            y += 12;
        }
        if (enabled(HudModule.Element.FPS)) {
            context.drawTextWithShadow(client.textRenderer, "FPS  " + client.getCurrentFps(), 8, y, 0xFFFFFFFF);
            y += 12;
        }
        if (enabled(HudModule.Element.COORDINATES)) {
            var p = client.player.getBlockPos();
            context.drawTextWithShadow(client.textRenderer,
                    "XYZ  " + p.getX() + "  " + p.getY() + "  " + p.getZ(), 8, y, 0xFFFFFFFF);
            y += 12;
        }
        if (enabled(HudModule.Element.DIRECTION)) {
            float yaw = client.player.getYaw(1.0f);
            String[] directions = {"South", "West", "North", "East"};
            int index = Math.floorMod(Math.round(yaw / 90.0f), 4);
            context.drawTextWithShadow(client.textRenderer, "Facing  " + directions[index], 8, y, 0xFFFFFFFF);
        }
        if (enabled(HudModule.Element.ARMOR)) {
            int x = 8;
            int bottom = client.getWindow().getScaledHeight() - 28;
            for (int slot = 3; slot >= 0; slot--) {
                ItemStack stack = client.player.getInventory().getArmorStack(slot);
                if (!stack.isEmpty()) {
                    context.drawItem(stack, x, bottom);
                    context.drawItemInSlot(client.textRenderer, stack, x, bottom);
                    x += 20;
                }
            }
        }
        if (enabled(HudModule.Element.KEYSTROKES)) {
            int x = 8, bottom = client.getWindow().getScaledHeight() - 78;
            drawKey(context, client, "W", client.options.forwardKey.isPressed(), x + 20, bottom);
            drawKey(context, client, "A", client.options.leftKey.isPressed(), x, bottom + 20);
            drawKey(context, client, "S", client.options.backKey.isPressed(), x + 20, bottom + 20);
            drawKey(context, client, "D", client.options.rightKey.isPressed(), x + 40, bottom + 20);
            drawKey(context, client, "SPACE", client.options.jumpKey.isPressed(), x, bottom + 40, 60);
        }
        if (enabled(HudModule.Element.MODULE_LIST)) {
            int right = client.getWindow().getScaledWidth() - 8;
            int line = 8;
            for (var module : ModuleManager.all()) {
                if (module.isEnabled() && !(module instanceof HudModule)) {
                    String label = module.name();
                    context.drawTextWithShadow(client.textRenderer, label,
                            right - client.textRenderer.getWidth(label), line, 0xFFB69CFF);
                    line += 11;
                }
            }
        }
    }

    private static void drawKey(DrawContext context, MinecraftClient client, String label, boolean down, int x, int y) {
        drawKey(context, client, label, down, x, y, 18);
    }

    private static void drawKey(DrawContext context, MinecraftClient client, String label, boolean down, int x, int y, int width) {
        context.fill(x, y, x + width, y + 18, down ? 0xFF7256A8 : 0xAA171B24);
        int textX = x + (width - client.textRenderer.getWidth(label)) / 2;
        context.drawText(client.textRenderer, label, textX, y + 5, 0xFFFFFFFF, false);
    }

    private static boolean enabled(HudModule.Element element) {
        return ModuleManager.all().stream()
                .filter(m -> m instanceof HudModule)
                .map(m -> (HudModule) m)
                .anyMatch(m -> m.element() == element && m.isEnabled());
    }
}
