package dev.tommy.client.module.modules;

import dev.tommy.client.module.Category;
import dev.tommy.client.module.Module;

public final class HudModule extends Module {
    public enum Element { WATERMARK, FPS, COORDINATES, MODULE_LIST, ARMOR, KEYSTROKES, DIRECTION }
    private final Element element;

    public HudModule(String name, String description, Element element) {
        super(name, description, Category.RENDER);
        this.element = element;
    }

    public Element element() { return element; }
}
