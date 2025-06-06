package com.guanshangyu.auto_tpa.client.config;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class AutoTpaConfigScreen extends Screen {
    private final Screen parent;
    private static ModConfig config;

    protected AutoTpaConfigScreen(Screen parent) {
        super(Text.literal("自动TPA配置"));
        this.parent = parent;
        config = ConfigManager.getConfig();
    }

    public static Screen create(Screen parent) {
        return AutoConfig.getConfigScreen(ModConfig.class, parent).get();
    }

    public static ModConfig getConfig() {
        return config;
    }

    public Screen getParent() {
        return parent;
    }
}