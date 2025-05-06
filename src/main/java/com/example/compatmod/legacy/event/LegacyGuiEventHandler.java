package com.example.compatmod.legacy.event;

import com.example.compatmod.legacy.api.ILegacyMod;
import com.example.compatmod.legacy.loader.LegacyModManager;
import com.example.compatmod.legacy.widget.LegacyWidgetWrapper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
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

        for (ILegacyMod mod : LegacyModManager.getLegacyMods()) {
            mod.onGuiInit(event.getScreen(), legacyWidgets);
            for (LegacyWidgetWrapper wrapper : legacyWidgets) {
                event.addListener(wrapper.getWidget());
            }
        }
    }

    @SubscribeEvent
    public static void onGuiRender(ScreenEvent.Render.Post event) {
        GuiGraphics graphics = event.getGuiGraphics();
        for (LegacyWidgetWrapper wrapper : legacyWidgets) {
            wrapper.getWidget().render(graphics, event.getMouseX(), event.getMouseY(), event.getPartialTick());
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            for (LegacyWidgetWrapper wrapper : legacyWidgets) {
                wrapper.tick();
            }
        }
    }

}