package com.example.compatmod.legacy.event;

import com.example.compatmod.config.SafeConfigManager;
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
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
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
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


@Mod.EventBusSubscriber(modid = "legacymodloader", value = Dist.CLIENT)
public class LegacyGuiEventHandler {
    private static final List<LegacyWidgetWrapper> legacyWidgets = new ArrayList<>();
    private static int lastMouseButton = -1;

    public static void addLegacyWidget(LegacyWidgetWrapper wrapper) {
        legacyWidgets.add(wrapper);
    }

    public static void clearLegacyWidgets() {
        legacyWidgets.clear();
    }

    public static List<LegacyWidgetWrapper> getLegacyWidgets() {
        return new ArrayList<>(legacyWidgets); // 直接参照を渡さない
    }

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
        double mouseX = event.getMouseX();
        double mouseY = event.getMouseY();
        int button = event.getButton();

        for (LegacyWidgetWrapper wrapper : legacyWidgets) {
            if (wrapper.getWidget().isMouseOver(mouseX, mouseY)) {
                if (wrapper.mouseClicked(mouseX, mouseY, button)) {
                    event.setCanceled(true);
                    return;
                }
            }
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
        clearLegacyWidgets(); // ✔ 登録状態をリセット

        List<LegacyWidgetWrapper> widgets = new ArrayList<>();
        for (ILegacyMod mod : LegacyModManager.getLegacyMods()) {
            mod.onGuiInit(event.getScreen(), widgets);
        }

        // 前のウィジェット登録で画面に残っている描画用部品は Forge 側で自動的に置き換えられる
        for (LegacyWidgetWrapper wrapper : widgets) {
            event.addListener(wrapper.getWidget());
        }

        setLegacyWidgets(widgets); // ✔ 描画対象もリセット
    }

    public static void setLegacyWidgets(List<LegacyWidgetWrapper> widgets) {
        legacyWidgets.clear();
        legacyWidgets.addAll(widgets);
    }

    @SubscribeEvent
    public static void onGuiRender(ScreenEvent.Render.Post event) {
        GuiGraphics graphics = event.getGuiGraphics();
        for (LegacyWidgetWrapper wrapper : legacyWidgets) {
            if (wrapper.isVisible()) {
                wrapper.render(graphics, event.getMouseX(), event.getMouseY(), event.getPartialTick());
                wrapper.renderTooltip(graphics, event.getMouseX(), event.getMouseY());
            }
        }
    }



    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            for (LegacyWidgetWrapper wrapper : legacyWidgets) {
                if (wrapper.isVisible() && wrapper.isEnabled()) {
                    wrapper.tick();
                }
            }
        }
    }
    public static void registerLegacyWidget(LegacyWidgetWrapper widget) {
        legacyWidgets.add(widget);
    }

    @SubscribeEvent
    public static void onMouseDragged(ScreenEvent.MouseDragged.Pre event) {
        double mouseX = event.getMouseX();
        double mouseY = event.getMouseY();
        int button = lastMouseButton;

        for (LegacyWidgetWrapper wrapper : legacyWidgets) {
            if (wrapper.getWidget().isMouseOver(mouseX, mouseY)) {
                if (wrapper.mouseDragged(mouseX, mouseY, button, event.getDragX(), event.getDragY())) {
                    event.setCanceled(true);
                    return;
                }
            }
        }
    }

    @SubscribeEvent
    public static void onMouseButtonRaw(InputEvent.MouseButton event) {
        if (event.getAction() == GLFW.GLFW_PRESS) {
            lastMouseButton = event.getButton();
        }
    }

    @SubscribeEvent
    public static void onMouseReleased(ScreenEvent.MouseButtonReleased.Pre event) {
        double mouseX = event.getMouseX();
        double mouseY = event.getMouseY();

        int button = lastMouseButton;

        for (LegacyWidgetWrapper wrapper : legacyWidgets) {
            if (wrapper.getWidget().isMouseOver(mouseX, mouseY)) {
                if (wrapper.mouseReleased(mouseX, mouseY, button)) {
                    event.setCanceled(true); // ✅ 本当に legacy widget が使った場合だけ
                    return;
                }
            }
        }
    }
    /*
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
            event.addListener(widget);
            ((List<net.minecraft.client.gui.components.events.GuiEventListener>) screen.children()).add(widget);
            ((List<net.minecraft.client.gui.components.Renderable>) screen.renderables).add(widget);
        }
    }
 */
    @SubscribeEvent
    public static void onClientChatSent(net.minecraftforge.client.event.ClientChatEvent event) {
        String message = event.getMessage();

        for (ILegacyMod mod : LegacyModManager.getLegacyMods()) {
            mod.onChatInput(message);
        }
    }
    @SubscribeEvent
    public static void onChatInput(net.minecraftforge.client.event.ClientChatEvent event) {
        String message = event.getMessage();

        // すべてのレガシーMODに通知
        for (ILegacyMod mod : LegacyModManager.getLegacyMods()) {
            mod.onChatInput(message);
        }

        // もし独自コマンドや処理でチャット送信をブロックしたいならここでキャンセル
        if (message.startsWith("!legacy")) {
            event.setCanceled(true);
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("Legacy command intercepted: " + message));
        }
    }
    @SubscribeEvent
    public static void onScreenClose(ScreenEvent.Closing event) {
        System.out.println("[LegacyEvent] Screen closing - saving config");
        SafeConfigManager.saveConfigSafe();
    }
    @SubscribeEvent
    public static void onRender(ScreenEvent.Render.Post event) {
        GuiGraphics graphics = event.getGuiGraphics();
        int mouseX = event.getMouseX();
        int mouseY = event.getMouseY();
        float partialTicks = event.getPartialTick();

        for (LegacyWidgetWrapper wrapper : legacyWidgets) {
            if (wrapper.isVisible()) {
                wrapper.renderTooltip(graphics, mouseX, mouseY);
            }
        }

        for (ILegacyMod mod : LegacyModManager.getLegacyMods()) {
            mod.onGuiRenderPost(event.getScreen(), graphics, mouseX, mouseY, partialTicks);
        }
    }
}
