package dev.tommy.client;

import dev.tommy.client.config.ConfigManager;
import dev.tommy.client.gui.ClickGuiScreen;
import dev.tommy.client.module.ModuleManager;
import dev.tommy.client.module.modules.FullbrightModule;
import dev.tommy.client.module.modules.HudModule;
import dev.tommy.client.module.modules.SprintModule;
import dev.tommy.client.module.modules.TogglePerspectiveModule;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public final class ClientMod implements ClientModInitializer {
    public static final String MOD_ID = "tommyclient";
    private static KeyBinding clickGuiKey;

    @Override
    public void onInitializeClient() {
        ConfigManager.load();

        ModuleManager.register(new SprintModule());
        ModuleManager.register(new FullbrightModule());
        ModuleManager.register(new TogglePerspectiveModule());
        ModuleManager.register(new HudModule("Watermark", "Displays TommyClient branding.", HudModule.Element.WATERMARK));
        ModuleManager.register(new HudModule("FPS", "Displays the current frame rate.", HudModule.Element.FPS));
        ModuleManager.register(new HudModule("Coordinates", "Displays your current coordinates.", HudModule.Element.COORDINATES));
        ModuleManager.register(new HudModule("Module List", "Displays enabled modules.", HudModule.Element.MODULE_LIST));
        ModuleManager.register(new HudModule("Armor HUD", "Displays equipped armor icons.", HudModule.Element.ARMOR));
        ModuleManager.register(new HudModule("Keystrokes", "Displays movement key states.", HudModule.Element.KEYSTROKES));
        ModuleManager.register(new HudModule("Direction", "Displays your compass direction.", HudModule.Element.DIRECTION));

        ConfigManager.applyLoadedState();

        clickGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.tommyclient.clickgui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.tommyclient"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (clickGuiKey.wasPressed()) {
                if (client.currentScreen == null) client.setScreen(new ClickGuiScreen());
                else if (client.currentScreen instanceof ClickGuiScreen) client.setScreen(null);
            }
            ModuleManager.tick(client);
        });

        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> HudRenderer.render(drawContext));
        Runtime.getRuntime().addShutdownHook(new Thread(ConfigManager::save, "TommyClient-config-save"));
    }
}
