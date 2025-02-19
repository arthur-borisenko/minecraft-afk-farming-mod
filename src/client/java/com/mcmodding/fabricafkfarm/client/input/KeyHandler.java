package com.mcmodding.fabricafkfarm.client.input;

import net.minecraft.entity.Entity;
import net.minecraft.util.Hand;
import com.mcmodding.fabricafkfarm.client.FabricAFKFarmClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class KeyHandler {
    private static KeyBinding toggleAttack;
    private static KeyBinding toggleUse;
    private static KeyBinding toggleBreak; // Новый биндинг для autoBreak

    public static void register() {
        toggleAttack = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fabricafkfarm.toggle_attack",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.fabricafkfarm"
        ));

        toggleUse = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fabricafkfarm.toggle_use",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F,
                "category.fabricafkfarm"
        ));

        toggleBreak = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fabricafkfarm.toggle_break",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_B, // Клавиша "B"
                "category.fabricafkfarm"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.currentScreen != null) return;

            if (toggleAttack.wasPressed()) FabricAFKFarmClient.autoAttackEnabled = !FabricAFKFarmClient.autoAttackEnabled;
            if (toggleUse.wasPressed()) FabricAFKFarmClient.autoUseEnabled = !FabricAFKFarmClient.autoUseEnabled;
            if (toggleBreak.wasPressed()) FabricAFKFarmClient.autoBreakEnabled = !FabricAFKFarmClient.autoBreakEnabled; // Переключаем autoBreak

            handleAutoActions(client);
        });
    }

    private static void handleAutoActions(MinecraftClient client) {
        // Автоматическая атака (только по сущностям)
        if (FabricAFKFarmClient.autoAttackEnabled
                && FabricAFKFarmClient.attackTimer++ >= FabricAFKFarmClient.config.attackInterval) {

            if (client.interactionManager != null && client.player != null) {
                // Если цель — существо, атакуем его
                if (client.targetedEntity != null) {
                    client.interactionManager.attackEntity(client.player, client.targetedEntity);
                }
            }

            FabricAFKFarmClient.attackTimer = 0;
        }

        // Автоматическое использование предметов и установка блоков
        if (FabricAFKFarmClient.autoUseEnabled
                && FabricAFKFarmClient.useTimer++ >= FabricAFKFarmClient.config.useInterval) {

            if (client.interactionManager != null && client.player != null) {
                // Пытаемся использовать предмет в главной руке
                client.interactionManager.interactItem(client.player, Hand.MAIN_HAND);

                // Пытаемся установить блок перед игроком (если в руке блок)
                HitResult hitResult = client.crosshairTarget;
                if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
                    BlockHitResult blockHitResult = (BlockHitResult) hitResult;

                    // Установка блока
                    client.interactionManager.interactBlock(
                            client.player,
                            Hand.MAIN_HAND,
                            blockHitResult
                    );
                }
            } else {
                System.err.println("Cannot interact: interactionManager or player is null.");
            }

            FabricAFKFarmClient.useTimer = 0;
        }

        // Автоматическое разрушение блоков (удержание ЛКМ)
        if (FabricAFKFarmClient.autoBreakEnabled) {
            if (client.interactionManager != null && client.player != null) {
                HitResult hitResult = client.crosshairTarget;
                if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
                    BlockHitResult blockHitResult = (BlockHitResult) hitResult;

                    // Эмулируем удержание ЛКМ для разрушения блока
                    client.interactionManager.updateBlockBreakingProgress(blockHitResult.getBlockPos(), blockHitResult.getSide());
                }
            }
        }
    }
}