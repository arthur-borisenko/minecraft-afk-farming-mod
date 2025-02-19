package com.mcmodding.fabricafkfarm.client;

import com.mcmodding.fabricafkfarm.client.config.ModConfig;
import com.mcmodding.fabricafkfarm.client.hud.StatusHUD;
import com.mcmodding.fabricafkfarm.client.input.KeyHandler;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

@Environment(EnvType.CLIENT)
public class FabricAFKFarmClient implements ClientModInitializer {
    public static boolean autoAttackEnabled = false;
    public static boolean autoUseEnabled = false;
    public static boolean autoBreakEnabled = false;
    public static int attackTimer = 0;
    public static int useTimer = 0;
    public static ModConfig config;

    @Override
    public void onInitializeClient() {
        AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        KeyHandler.register();
        HudRenderCallback.EVENT.register(new StatusHUD());
    }
}