package dev.tommy.client.module.modules;

import dev.tommy.client.module.Category;
import dev.tommy.client.module.Module;
import net.minecraft.client.MinecraftClient;

public final class FullbrightModule extends Module {
    private double previousGamma;

    public FullbrightModule() {
        super("Fullbright", "Raises local brightness while enabled.", Category.RENDER);
    }

    @Override
    public void onEnable() {
        MinecraftClient client = MinecraftClient.getInstance();
        previousGamma = client.options.getGamma().getValue();
        client.options.getGamma().setValue(1.0);
    }

    @Override
    public void onDisable() {
        MinecraftClient.getInstance().options.getGamma().setValue(previousGamma);
    }
}
