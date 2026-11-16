package dev.tommy.client.module.modules;

import dev.tommy.client.module.Category;
import dev.tommy.client.module.Module;
import net.minecraft.client.MinecraftClient;

public final class SprintModule extends Module {
    public SprintModule() {
        super("Sprint", "Automatically sprint while moving forward in local singleplayer.", Category.MOVEMENT);
    }

    @Override
    public void onTick(MinecraftClient client) {
        // The project specification limits automated gameplay to local singleplayer.
        if (!client.isInSingleplayer() || client.player == null || client.currentScreen != null) return;
        if (client.options.forwardKey.isPressed() && !client.player.isSneaking()) {
            client.player.setSprinting(true);
        }
    }
}
