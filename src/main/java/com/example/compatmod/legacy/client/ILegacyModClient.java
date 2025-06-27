package com.example.compatmod.legacy.client;

import com.example.compatmod.legacy.widget.LegacyWidgetWrapper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public interface ILegacyModClient {
    default void onClientTick() {}
    default void onRenderOverlay() {}
    default void onRenderGameOverlay() {}
    default void onKeyInput(int keyCode, boolean pressed) {}
    default void onRenderOverlay(GuiGraphics guiGraphics) {}
    default void onScreenOpen(Screen screen) {}
    default void onPreRenderOverlay(GuiGraphics guiGraphics) {}
    default void onScreenRender(Screen screen, GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {}
    default void onGuiMouseClick(Screen screen, double mouseX, double mouseY, int button) {}
    default void onGuiKeyPressed(Screen screen, int keyCode, int scanCode, int modifiers) {}
    default void onGuiMouseClicked(Screen screen, double mouseX, double mouseY, int button) {}
    default void onGuiRenderPost(Screen screen, GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {}
    default void onGuiInit(Screen screen, List<LegacyWidgetWrapper> widgets) {}
}
