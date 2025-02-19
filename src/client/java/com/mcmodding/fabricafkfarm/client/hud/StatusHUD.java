package com.mcmodding.fabricafkfarm.client.hud;

import com.mcmodding.fabricafkfarm.client.FabricAFKFarmClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class StatusHUD implements net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback {

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        if (!FabricAFKFarmClient.config.showHUD) return;

        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;

        int x = FabricAFKFarmClient.config.hudX;
        int y = FabricAFKFarmClient.config.hudY;

        if (FabricAFKFarmClient.autoAttackEnabled) {
            drawContext.drawTextWithShadow(
                    textRenderer,
                    Text.translatable("text.fabricafkfarm.attack_status", FabricAFKFarmClient.config.attackInterval),
                    x, y, 0x00FF00
            );
            y += 10;
        }

        if (FabricAFKFarmClient.autoUseEnabled) {
            drawContext.drawTextWithShadow(
                    textRenderer,
                    Text.translatable("text.fabricafkfarm.use_status", FabricAFKFarmClient.config.useInterval),
                    x, y, 0x00FF00
            );
        }
        if (FabricAFKFarmClient.autoBreakEnabled) {
            drawContext.drawTextWithShadow(
                    textRenderer,
                    Text.translatable("text.fabricafkfarm.break_status"),
                    x, y, 0x00FF00
            );
        }
    }
}