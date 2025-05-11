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

        // 追加ボタンなどの処理
        LegacyGuiButtonEventHandler.initWidgets(event, legacyWidgets);

        // 全ての ILegacyMod からウィジェットを集める
        for (ILegacyMod mod : LegacyModManager.getLegacyMods()) {
            mod.onGuiInit(event.getScreen(), legacyWidgets);
        }
        // ここで確実に addListener
        for (LegacyWidgetWrapper wrapper : legacyWidgets) {
            event.addListener(wrapper.getWidget());
        }
        // スライダー追加
        //LegacySlider slider = new LegacySlider(10, 90, 150, 20, 0.5);
        //slider.active = true;
        //slider.visible = true;
        //legacyWidgets.add(new LegacyWidgetWrapper(slider, slider::tick));

        // まとめてGUIに登録
        //for (LegacyWidgetWrapper wrapper : legacyWidgets) {
        //    event.addListener(wrapper.getWidget());
        //}
    }

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
}