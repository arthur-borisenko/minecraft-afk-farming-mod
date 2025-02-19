package com.mcmodding.fabricafkfarm.client.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "fabricafkfarm")
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    public int attackInterval = 3;

    @ConfigEntry.Gui.Tooltip
    public int useInterval = 2;

    @ConfigEntry.Gui.Tooltip
    public boolean showHUD = true;

    @ConfigEntry.Gui.Tooltip
    public int hudX = 5;

    @ConfigEntry.Gui.Tooltip
    public int hudY = 5;
}