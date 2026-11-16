package dev.tommy.client.module;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ModuleManager {
    private static final List<Module> MODULES = new ArrayList<>();
    private static final Set<Integer> DOWN_KEYS = new HashSet<>();
    private ModuleManager() {}

    public static void register(Module module) { MODULES.add(module); }
    public static List<Module> all() { return Collections.unmodifiableList(MODULES); }

    public static Module get(String name) {
        return MODULES.stream().filter(m -> m.name().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static void tick(MinecraftClient client) {
        if (client.getWindow() != null && client.currentScreen == null) {
            long handle = client.getWindow().getHandle();
            for (Module module : MODULES) {
                int key = module.keybind();
                if (key < 0) continue;
                boolean down = GLFW.glfwGetKey(handle, key) == GLFW.GLFW_PRESS;
                if (down && DOWN_KEYS.add(key)) module.toggle();
                if (!down) DOWN_KEYS.remove(key);
            }
        }

        for (Module module : MODULES) {
            if (!module.isEnabled()) continue;
            try {
                module.onTick(client);
            } catch (RuntimeException exception) {
                module.setEnabled(false);
                System.err.println("[TommyClient] Disabled '" + module.name() + "' after tick error: " + exception);
            }
        }
    }
}
