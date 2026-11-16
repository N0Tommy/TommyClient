package dev.tommy.client.gui;

import dev.tommy.client.config.ConfigManager;
import dev.tommy.client.module.Category;
import dev.tommy.client.module.Module;
import dev.tommy.client.module.ModuleManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class ClickGuiScreen extends Screen {
    private static final int PANEL_W = 590;
    private static final int PANEL_H = 360;
    private static final int ACCENT = 0xFF9B7BFF;

    private Category selectedCategory;
    private Module selectedModule;
    private TextFieldWidget search;
    private final List<Row> rows = new ArrayList<>();
    private int scroll;
    private boolean waitingForBind;

    public ClickGuiScreen() { super(Text.literal("TommyClient")); }

    @Override
    protected void init() {
        int left = (width - PANEL_W) / 2;
        int top = (height - PANEL_H) / 2;
        search = new TextFieldWidget(textRenderer, left + 150, top + 48, 190, 20, Text.literal("Search"));
        search.setPlaceholder(Text.literal("Search modules..."));
        addDrawableChild(search);
        rebuildRows();
    }

    private void rebuildRows() {
        rows.clear();
        String query = search == null ? "" : search.getText().toLowerCase(Locale.ROOT);
        int left = (width - PANEL_W) / 2;
        int top = (height - PANEL_H) / 2;
        int y = top + 82 - scroll;
        for (Module module : ModuleManager.all()) {
            if (selectedCategory != null && module.category() != selectedCategory) continue;
            if (!query.isBlank() && !module.name().toLowerCase(Locale.ROOT).contains(query)
                    && !module.description().toLowerCase(Locale.ROOT).contains(query)) continue;
            rows.add(new Row(module, left + 142, y));
            y += 35;
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        int l = (width - PANEL_W) / 2, t = (height - PANEL_H) / 2;
        context.fill(l, t, l + PANEL_W, t + PANEL_H, 0xF014171F);
        context.fill(l, t, l + PANEL_W, t + 3, ACCENT);
        context.drawTextWithShadow(textRenderer, "TOMMYCLIENT", l + 16, t + 14, 0xFFFFFFFF);
        context.drawTextWithShadow(textRenderer, "CLIENT UTILITY  /  1.21.5", l + 16, t + 29, 0xFF9299A8);

        context.fill(l + 10, t + 75, l + 130, t + PANEL_H - 12, 0xFF1B1F29);
        Category[] categories = Category.values();
        for (int i = 0; i < categories.length; i++) {
            int y = t + 86 + i * 34;
            boolean active = selectedCategory == categories[i];
            context.fill(l + 16, y - 5, l + 124, y + 20, active ? 0xFF38304F : 0xFF252A35);
            context.drawText(textRenderer, title(categories[i].name()), l + 25, y + 3,
                    active ? 0xFFD8C9FF : 0xFFB9C0CC, false);
        }
        context.drawText(textRenderer, "ALL MODULES", l + 145, t + 76, 0xFF9299A8, false);
        context.fill(l + 140, t + 75, l + 370, t + PANEL_H - 12, 0xAA0D1016);
        context.fill(l + 380, t + 75, l + PANEL_W - 10, t + PANEL_H - 12, 0xFF1B1F29);

        rebuildRows();
        for (Row row : rows) {
            if (row.y < t + 80 || row.y > t + PANEL_H - 43) continue;
            boolean hover = mouseX >= row.x && mouseX <= l + 370 && mouseY >= row.y && mouseY <= row.y + 29;
            boolean chosen = selectedModule == row.module;
            context.fill(row.x, row.y, l + 365, row.y + 29,
                    chosen ? 0xFF38304F : hover ? 0xFF303644 : 0xFF222833);
            context.drawText(textRenderer, row.module.name(), row.x + 9, row.y + 5,
                    row.module.isEnabled() ? 0xFFD6C6FF : 0xFFE5E7EB, false);
            context.drawText(textRenderer, row.module.isEnabled() ? "ON" : "OFF",
                    l + 330, row.y + 5, row.module.isEnabled() ? 0xFF86EFAC : 0xFF8E96A5, false);
            context.drawText(textRenderer, row.module.description(), row.x + 9, row.y + 17, 0xFF8E96A5, false);
        }

        if (selectedModule == null) {
            context.drawTextWithShadow(textRenderer, "Select a module", l + 395, t + 95, 0xFFFFFFFF);
            context.drawText(textRenderer, "Choose a module to view its", l + 395, t + 116, 0xFF9CA3AF, false);
            context.drawText(textRenderer, "description and keybind.", l + 395, t + 128, 0xFF9CA3AF, false);
        } else {
            context.drawTextWithShadow(textRenderer, selectedModule.name(), l + 395, t + 94, 0xFFFFFFFF);
            context.drawText(textRenderer, title(selectedModule.category().name()), l + 395, t + 111, ACCENT, false);
            drawWrapped(context, selectedModule.description(), l + 395, t + 139, 175, 12, 0xFFB9C0CC);
            int bx = l + 395, by = t + 205;
            context.fill(bx, by, bx + 165, by + 28, selectedModule.isEnabled() ? 0xFF3D3159 : 0xFF303744);
            context.drawText(textRenderer, selectedModule.isEnabled() ? "Disable module" : "Enable module",
                    bx + 10, by + 9, 0xFFFFFFFF, false);
            context.fill(bx, by + 38, bx + 165, by + 66, waitingForBind ? 0xFF57456F : 0xFF303744);
            String bind = selectedModule.keybind() < 0 ? "Not set" : GLFW.glfwGetKeyName(selectedModule.keybind(), 0);
            if (bind == null && selectedModule.keybind() >= 0) bind = "Key " + selectedModule.keybind();
            context.drawText(textRenderer, waitingForBind ? "Press a key..." : "Keybind: " + bind,
                    bx + 10, by + 48, 0xFFFFFFFF, false);
            context.drawText(textRenderer, "Click keybind to change", bx, by + 77, 0xFF9299A8, false);
            context.drawText(textRenderer, "Press Backspace to clear", bx, by + 90, 0xFF9299A8, false);
            context.fill(bx, by + 116, bx + 78, by + 142, 0xFF303744);
            context.fill(bx + 86, by + 116, bx + 165, by + 142, 0xFF303744);
            context.drawText(textRenderer, "Save profile", bx + 6, by + 125, 0xFFFFFFFF, false);
            context.drawText(textRenderer, "Load profile", bx + 91, by + 125, 0xFFFFFFFF, false);
        }

        context.drawText(textRenderer, "RIGHT SHIFT  •  CLOSE", l + 16, t + PANEL_H - 22, 0xFF777F8D, false);
        super.render(context, mouseX, mouseY, delta);
    }

    private void drawWrapped(DrawContext context, String value, int x, int y, int maxWidth, int lineHeight, int color) {
        StringBuilder line = new StringBuilder();
        for (String word : value.split(" ")) {
            String next = line.isEmpty() ? word : line + " " + word;
            if (textRenderer.getWidth(next) > maxWidth && !line.isEmpty()) {
                context.drawText(textRenderer, line.toString(), x, y, color, false);
                y += lineHeight;
                line = new StringBuilder(word);
            } else line = new StringBuilder(next);
        }
        if (!line.isEmpty()) context.drawText(textRenderer, line.toString(), x, y, color, false);
    }

    private static String title(String raw) {
        String lower = raw.toLowerCase(Locale.ROOT);
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (super.mouseClicked(mx, my, button)) return true;
        if (button != 0) return false;
        int l = (width - PANEL_W) / 2, t = (height - PANEL_H) / 2;
        for (int i = 0; i < Category.values().length; i++) {
            int y = t + 81 + i * 34;
            if (mx >= l + 16 && mx <= l + 124 && my >= y && my <= y + 25) {
                selectedCategory = Category.values()[i];
                scroll = 0;
                return true;
            }
        }
        rebuildRows();
        for (Row row : rows) {
            if (mx >= row.x && mx <= l + 370 && my >= row.y && my <= row.y + 29
                    && my >= t + 80 && my <= t + PANEL_H - 43) {
                selectedModule = row.module;
                waitingForBind = false;
                return true;
            }
        }
        if (selectedModule != null) {
            int bx = l + 395, by = t + 205;
            if (mx >= bx && mx <= bx + 165 && my >= by && my <= by + 28) {
                selectedModule.toggle();
                ConfigManager.save();
                return true;
            }
            if (mx >= bx && mx <= bx + 165 && my >= by + 38 && my <= by + 66) {
                waitingForBind = true;
                return true;
            }
            if (mx >= bx && mx <= bx + 78 && my >= by + 116 && my <= by + 142) {
                ConfigManager.saveProfile("quick");
                return true;
            }
            if (mx >= bx + 86 && mx <= bx + 165 && my >= by + 116 && my <= by + 142) {
                ConfigManager.loadProfile("quick");
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mx, double my, double horizontal, double vertical) {
        scroll = Math.max(0, scroll - (int) (vertical * 35));
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (waitingForBind && selectedModule != null) {
            if (keyCode == GLFW.GLFW_KEY_BACKSPACE || keyCode == GLFW.GLFW_KEY_DELETE) {
                selectedModule.setKeybind(-1);
                waitingForBind = false;
                ConfigManager.save();
                return true;
            }
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                waitingForBind = false;
                return true;
            }
            if (keyCode != GLFW.GLFW_KEY_RIGHT_SHIFT) {
                selectedModule.setKeybind(keyCode);
                waitingForBind = false;
                ConfigManager.save();
                return true;
            }
        }
        if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void close() {
        ConfigManager.save();
        super.close();
    }

    @Override
    public boolean shouldPause() { return false; }
    private record Row(Module module, int x, int y) {}
}
