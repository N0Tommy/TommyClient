package dev.tommy.client.module.modules;

import dev.tommy.client.module.Category;
import dev.tommy.client.module.Module;
import net.minecraft.client.MinecraftClient;

public final class TogglePerspectiveModule extends Module {
    private net.minecraft.client.option.Perspective previous;

    public TogglePerspectiveModule() {
        super("Perspective", "Switches to third-person while enabled, then restores your view.", Category.PLAYER);
    }

    @Override
    public void onEnable() {
        MinecraftClient client = MinecraftClient.getInstance();
        previous = client.options.getPerspective();
        client.options.setPerspective(net.minecraft.client.option.Perspective.THIRD_PERSON_BACK);
    }

    @Override
    public void onDisable() {
        if (previous != null) MinecraftClient.getInstance().options.setPerspective(previous);
    }
}
