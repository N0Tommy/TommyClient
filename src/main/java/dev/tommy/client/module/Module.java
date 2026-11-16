package dev.tommy.client.module;

import net.minecraft.client.MinecraftClient;

public abstract class Module {
    private final String name;
    private final String description;
    private final Category category;
    private boolean enabled;
    private int keybind = -1;

    protected Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public final String name() { return name; }
    public final String description() { return description; }
    public final Category category() { return category; }
    public final boolean isEnabled() { return enabled; }
    public final int keybind() { return keybind; }
    public final void setKeybind(int keybind) { this.keybind = keybind; }

    public final void setEnabled(boolean value) {
        if (enabled == value) return;
        enabled = value;
        try {
            if (value) onEnable();
            else onDisable();
        } catch (RuntimeException exception) {
            enabled = false;
            try { onDisable(); } catch (RuntimeException ignored) {}
            System.err.println("[TommyClient] Module '" + name + "' failed: " + exception);
        }
    }

    public final void toggle() { setEnabled(!enabled); }
    public void onEnable() {}
    public void onDisable() {}
    public void onTick(MinecraftClient client) {}
}
