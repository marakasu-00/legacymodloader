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

import java.util.ArrayList;
import java.util.List;


@Mod.EventBusSubscriber(modid = "legacymodloader", value = Dist.CLIENT)
public class LegacyGuiEventHandler {
    private static final List<LegacyWidgetWrapper> legacyWidgets = new ArrayList<>();
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
        // 既存のウィジェットをクリア
        clearLegacyWidgets();
        // 古いウィジェットをすべてクリア
        List<LegacyWidgetWrapper> legacyWidgets = new ArrayList<>();

        // 追加ボタンなどの処理（必要なら外部ハンドラを使う）
        LegacyGuiButtonEventHandler.initWidgets(event, legacyWidgets);

        // LegacyMod からウィジェットを収集
        for (ILegacyMod mod : LegacyModManager.getLegacyMods()) {
            mod.onGuiInit(event.getScreen(), legacyWidgets);
        }

        // GUIに追加
        for (LegacyWidgetWrapper wrapper : legacyWidgets) {
            event.addListener(wrapper.getWidget());

            // widget が Renderable や GuiEventListener のインターフェースを持つ場合、明示的に登録
            if (event.getScreen() != null) {
                Screen screen = event.getScreen();
                if (screen.children() instanceof List) {
                    ((List) screen.children()).add(wrapper.getWidget());
                }
                if (screen.renderables instanceof List) {
                    ((List) screen.renderables).add(wrapper.getWidget());
                }
            }
        }

        // 必要なら LegacyGuiEventHandler 側で再利用するために保存
        LegacyGuiEventHandler.setLegacyWidgets(legacyWidgets);
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
                wrapper.getWidget().render(graphics, event.getMouseX(), event.getMouseY(), event.getPartialTick());
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
        for (LegacyWidgetWrapper wrapper : legacyWidgets) {
            if (wrapper.getWidget().isMouseOver(event.getMouseX(), event.getMouseY())) {
                boolean result = wrapper.mouseDragged(
                        event.getMouseX(),
                        event.getMouseY(),
                        0, // 通常は左クリック（button = 0）
                        event.getDragX(),
                        event.getDragY()
                );
                if (result) {
                    event.setCanceled(true); // 本当に処理されたときだけキャンセル
                    break;
                }
            }
        }
    }

    @SubscribeEvent
    public static void onMouseReleased(InputEvent.MouseButton event) {
        if (event.getAction() != GLFW.GLFW_RELEASE) return; // RELEASEイベントのみ処理

        Minecraft mc = Minecraft.getInstance();
        if (!mc.screen.isPauseScreen()) {
            double mouseX = mc.mouseHandler.xpos() * mc.getWindow().getGuiScaledWidth() / mc.getWindow().getScreenWidth();
            double mouseY = mc.mouseHandler.ypos() * mc.getWindow().getGuiScaledHeight() / mc.getWindow().getScreenHeight();
            int button = event.getButton();

            for (LegacyWidgetWrapper wrapper : legacyWidgets) {
                if (wrapper.getWidget().isMouseOver(mouseX, mouseY)) {
                    if (wrapper.mouseReleased(mouseX, mouseY, button)) {
                        event.setCanceled(true);
                        return;
                    }
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
            event.addListener(widget);
            ((List<net.minecraft.client.gui.components.events.GuiEventListener>) screen.children()).add(widget);
            ((List<net.minecraft.client.gui.components.Renderable>) screen.renderables).add(widget);
        }
    }
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
        com.example.compatmod.config.SafeConfigManager.apply();
    }
}