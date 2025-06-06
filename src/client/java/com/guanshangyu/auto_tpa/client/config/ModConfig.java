package com.guanshangyu.auto_tpa.client.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Config(name = "AutoTpa")
public class ModConfig implements ConfigData {
    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip
    public boolean autoAcceptEnabled = true;

    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.Tooltip
    public boolean showNotification = true;

    @ConfigEntry.Category("filter")
    @ConfigEntry.Gui.Tooltip
    public boolean onlyFromFriends = false;

    @ConfigEntry.Category("filter")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = false)
    public List<String> whitelist = new ArrayList<>();

    @ConfigEntry.Category("filter")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = false)
    public List<String> blacklist = new ArrayList<>();

    @ConfigEntry.Category("advanced")
    @ConfigEntry.Gui.Tooltip
    public String customAcceptCommand = "tpac";

    @ConfigEntry.Category("advanced")
    @ConfigEntry.Gui.Tooltip
    public String customRejectCommand = "tpa refuse";

    @ConfigEntry.Category("advanced")
    @ConfigEntry.Gui.Tooltip
    public String tpaPattern = "^\\[TpaCommand] 你收到一条同意 (\\w+) 传送至你的请求，使用 /tpac \\| /tpa \\(accept\\|refuse\\) 接受或拒绝，有效期为一分钟。$";

    @ConfigEntry.Category("advanced")
    @ConfigEntry.Gui.Excluded
    public transient boolean configLoaded = false;
}
