package dev.tommy.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.tommy.client.module.Module;
import dev.tommy.client.module.ModuleManager;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path DIRECTORY = FabricLoader.getInstance().getConfigDir().resolve("tommyclient");
    private static final Path FILE = DIRECTORY.resolve("default.json");
    private static JsonObject loaded = new JsonObject();

    private ConfigManager() {}

    public static void load() {
        try {
            if (Files.exists(FILE)) {
                loaded = GSON.fromJson(Files.readString(FILE, StandardCharsets.UTF_8), JsonObject.class);
                if (loaded == null) loaded = new JsonObject();
            }
        } catch (Exception exception) {
            loaded = new JsonObject();
            System.err.println("[TommyClient] Could not read config: " + exception);
        }
    }

    public static void applyLoadedState() {
        JsonObject modules = object(loaded, "modules");
        for (Module module : ModuleManager.all()) {
            JsonObject state = object(modules, module.name());
            if (state.has("enabled")) {
                try { module.setEnabled(state.get("enabled").getAsBoolean()); }
                catch (RuntimeException ignored) {}
            }
            if (state.has("keybind")) {
                try { module.setKeybind(state.get("keybind").getAsInt()); }
                catch (RuntimeException ignored) {}
            }
        }
    }

    public static void save() {
        try {
            JsonObject root = new JsonObject();
            JsonObject modules = new JsonObject();
            for (Module module : ModuleManager.all()) {
                JsonObject state = new JsonObject();
                state.addProperty("enabled", module.isEnabled());
                state.addProperty("keybind", module.keybind());
                modules.add(module.name(), state);
            }
            root.add("modules", modules);
            Files.createDirectories(DIRECTORY);
            Files.writeString(FILE, GSON.toJson(root), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            System.err.println("[TommyClient] Could not save config: " + exception);
        }
    }

    public static boolean saveProfile(String name) {
        if (!validProfileName(name)) return false;
        try {
            Files.createDirectories(DIRECTORY);
            Files.writeString(DIRECTORY.resolve(name + ".json"), GSON.toJson(snapshot()), StandardCharsets.UTF_8);
            return true;
        } catch (IOException exception) {
            System.err.println("[TommyClient] Could not save profile: " + exception);
            return false;
        }
    }

    public static boolean loadProfile(String name) {
        if (!validProfileName(name)) return false;
        Path profile = DIRECTORY.resolve(name + ".json");
        if (!Files.isRegularFile(profile)) return false;
        try {
            JsonObject data = GSON.fromJson(Files.readString(profile, StandardCharsets.UTF_8), JsonObject.class);
            if (data == null) return false;
            loaded = data;
            applyLoadedState();
            save();
            return true;
        } catch (Exception exception) {
            System.err.println("[TommyClient] Could not load profile: " + exception);
            return false;
        }
    }

    private static JsonObject snapshot() {
        JsonObject root = new JsonObject();
        JsonObject modules = new JsonObject();
        for (Module module : ModuleManager.all()) {
            JsonObject state = new JsonObject();
            state.addProperty("enabled", module.isEnabled());
            state.addProperty("keybind", module.keybind());
            modules.add(module.name(), state);
        }
        root.add("modules", modules);
        return root;
    }

    private static JsonObject object(JsonObject parent, String key) {
        return parent.has(key) && parent.get(key).isJsonObject()
                ? parent.getAsJsonObject(key) : new JsonObject();
    }

    private static boolean validProfileName(String name) {
        return name != null && name.matches("[A-Za-z0-9_-]{1,24}");
    }
}
