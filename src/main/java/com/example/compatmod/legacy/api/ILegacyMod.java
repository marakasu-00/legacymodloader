package com.example.compatmod.legacy.api;

import com.example.compatmod.legacy.widget.LegacyWidgetWrapper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public interface ILegacyMod {

    default void onLoad() {}

    default void onInit() {}

    default void onEnable() {}

    default void onClientTick() {}

    default void onRenderOverlay() {}

    default void onServerTick() {}

    default void onRenderGameOverlay() {}

    default void onKeyInput(int keyCode, boolean pressed) {}

    default void onPlayerInteract() {}

    default void onEntityInteract(LivingEntity target) {}

    default void onRenderOverlay(GuiGraphics guiGraphics) {}

    default void onScreenOpen(Screen screen) {
        // デフォルト実装（必要に応じてオーバーライド）
    }

    default void onPreRenderOverlay(GuiGraphics guiGraphics) {}

    default void onScreenRender(Screen screen, GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {}

    default void onChat(String messagePart) {
        // 実装するModがオーバーライド可能
    }
    default void onGuiMouseClick(Screen screen, double mouseX, double mouseY, int button) {
        // オーバーライドして使う
    }

    default void onGuiKeyPressed(Screen screen, int keyCode, int scanCode, int modifiers) {
        // Optional override in implementing mods
    }

    default void onGuiMouseClicked(Screen screen, double mouseX, double mouseY, int button){}

    default void onGuiRenderPost(Screen screen, GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        // 必要に応じてオーバーライド
    }
    default void onChatInput(String message) {}

    default void onGuiInit(Screen screen, List<LegacyWidgetWrapper> widgets){}

}
