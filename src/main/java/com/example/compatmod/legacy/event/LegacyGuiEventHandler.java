package com.example.compatmod.legacy.event;

import com.example.compatmod.legacy.api.ILegacyMod;
import com.example.compatmod.legacy.loader.LegacyModManager;
import com.example.compatmod.legacy.widget.LegacySlider;
import com.example.compatmod.legacy.widget.LegacyWidgetWrapper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.event.ScreenEvent.MouseButtonPressed.Pre;

import java.util.ArrayList;
import java.util.List;


@Mod.EventBusSubscriber(modid = "legacymodloader", value = Dist.CLIENT)
public class LegacyGuiEventHandler {
    private static final List<LegacyWidgetWrapper> legacyWidgets = new ArrayList<>();

    @SubscribeEvent
    public static void onScreenOpen(ScreenEvent.Opening event) {
        Screen newScreen = event.getNewScreen();
        for (ILegacyMod mod : LegacyModManager.getLegacyMods()) {
            mod.onScreenOpen(newScreen);
        }
    }

    @SubscribeEvent
    public static void onKeyPressed(ScreenEvent.KeyPressed.Pre event) {
        for (ILegacyMod mod : LegacyModManager.getLegacyMods()) {
            mod.onGuiKeyPressed(event.getScreen(), event.getKeyCode(), event.getScanCode(), event.getModifiers());
        }
    }

    @SubscribeEvent
    public static void onMouseClicked(ScreenEvent.MouseButtonPressed.Pre event) {
        Screen screen = event.getScreen();
        double mouseX = event.getMouseX();
        double mouseY = event.getMouseY();
        int button = event.getButton();

        for (ILegacyMod mod : LegacyModManager.getLegacyMods()) {
            mod.onGuiMouseClick(screen, mouseX, mouseY, button);
        }
    }

    @SubscribeEvent
    public static void onRenderPost(ScreenEvent.Render.Post event) {
        Screen screen = event.getScreen();
        GuiGraphics guiGraphics = event.getGuiGraphics();
        int mouseX = event.getMouseX();
        int mouseY = event.getMouseY();
        float partialTicks = event.getPartialTick();

        for (ILegacyMod mod : LegacyModManager.getLegacyMods()) {
            mod.onGuiRenderPost(screen, guiGraphics, mouseX, mouseY, partialTicks);
        }

    }
    @SubscribeEvent
    public static void onGuiInit(ScreenEvent.Init event) {
        legacyWidgets.clear();

        // ボタンなど追加
        LegacyGuiButtonEventHandler.initWidgets(event, legacyWidgets);

        // 各レガシーMODから取得
        for (ILegacyMod mod : LegacyModManager.getLegacyMods()) {
            mod.onGuiInit(event.getScreen(), legacyWidgets);
        }

        // 正常な追加（これだけで十分！）
        for (LegacyWidgetWrapper wrapper : legacyWidgets) {
            AbstractWidget widget = wrapper.getWidget();
            event.addListener(widget); // GUIに追加
        }
    }

    /*
    @SubscribeEvent
    public static void onGuiRender(ScreenEvent.Render.Post event) {
        GuiGraphics graphics = event.getGuiGraphics();

        for (LegacyWidgetWrapper wrapper : legacyWidgets) {
            wrapper.renderTooltip(graphics, event.getMouseX(), event.getMouseY());
        }
    }
     */

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            for (LegacyWidgetWrapper wrapper : legacyWidgets) {
                wrapper.tick();
            }
        }
    }
    public static void registerLegacyWidget(LegacyWidgetWrapper widget) {
        legacyWidgets.add(widget);
    }
    @SubscribeEvent
    public static void onMouseDragged(ScreenEvent.MouseDragged.Pre event) {
        for (LegacyWidgetWrapper wrapper : legacyWidgets) {
            // 一般的にドラッグは button=0 (左クリック) に紐づくので、仮にそれで処理
            if (wrapper.mouseDragged(event.getMouseX(), event.getMouseY(), 0, event.getDragX(), event.getDragY())) {
                event.setCanceled(true);
            }
        }
    }
    @SubscribeEvent
    public static void onMouseRelease(InputEvent.MouseButton event) {
        if (!Minecraft.getInstance().screen.isPauseScreen()) {
            double mouseX = Minecraft.getInstance().mouseHandler.xpos();
            double mouseY = Minecraft.getInstance().mouseHandler.ypos();
            int button = event.getButton();

            for (LegacyWidgetWrapper wrapper : legacyWidgets) {
                if (wrapper.mouseReleased(mouseX, mouseY, button)) {
                    event.setCanceled(true);
                }
            }
        }
    }
    @SubscribeEvent
    public static void onGuiInitPost(ScreenEvent.Init.Post event) {
        legacyWidgets.clear();
        LegacyGuiButtonEventHandler.initWidgets(event, legacyWidgets);

        for (ILegacyMod mod : LegacyModManager.getLegacyMods()) {
            mod.onGuiInit(event.getScreen(), legacyWidgets);
        }

        Screen screen = event.getScreen();
        for (LegacyWidgetWrapper wrapper : legacyWidgets) {
            AbstractWidget widget = wrapper.getWidget();
            ((List<net.minecraft.client.gui.components.events.GuiEventListener>) screen.children()).add(widget);
            ((List<net.minecraft.client.gui.components.Renderable>) screen.renderables).add(widget);
        }
    }
}