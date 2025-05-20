package com.example.compatmod.legacy;

import com.example.compatmod.config.ConfigHandler;
import com.example.compatmod.config.SafeConfigManager;
import com.example.compatmod.legacy.api.event.ILegacyEntityEventListener;
import com.example.compatmod.legacy.api.ILegacyMod;
import com.example.compatmod.legacy.event.LegacyEntityEventDispatcher;
import com.example.compatmod.legacy.event.LegacyGuiEventHandler;
import com.example.compatmod.legacy.widget.LegacyCheckbox;
import com.example.compatmod.legacy.widget.LegacyEditBox;
import com.example.compatmod.legacy.widget.LegacySlider;
import com.example.compatmod.legacy.widget.LegacyWidgetWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class ExampleLegacyMod implements ILegacyMod, ILegacyEntityEventListener {

    private double savedSliderValue = 0.5;
    private LegacySlider exampleSlider;
    private boolean checkboxChecked = false;
    private String savedInputText = "";
    private int savedCursorPos = 0;
    private LegacyEditBox legacyEditBox;
    private String savedText = "";
    private int savedCursor = 0;

    public ExampleLegacyMod() {
        LegacyEntityEventDispatcher.register(this); // ★ここで登録
    }

    @Override
    public void onLoad() {
        System.out.println("[LegacyExample] onLoad called!");
        LegacyEntityEventDispatcher.register(this);
    }

    @Override
    public void onClientTick() {
        if (exampleSlider != null) {
            double value = exampleSlider.getValue();
            // たとえばスニーク中はスライダーの値をプレイヤーのY座標に反映（テスト目的）
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null && mc.player.isCrouching()) {
                float angle = (float) (exampleSlider.getValue() * 180.0); // 値を角度に変換
                mc.player.setXRot(angle);  // ピッチを変化させる
                mc.player.setYRot((float) (value * 360));
            }
        }
    }

    @Override
    public void onEntityHurt(LivingEntity entity, DamageSource source, float amount) {
        System.out.println("[LegacyExample] Entity hurt: " + entity.getName().getString() + " Damage: " + amount);
    }

    @Override
    public void onServerTick() {
        //System.out.println("[LegacyExample] Server tick running!");
    }

    @Override
    public void onKeyInput(int keyCode, boolean pressed) {
        System.out.println("[LegacyExample] Key input detected! key=" + keyCode + " pressed=" + pressed);
    }

    @Override
    public void onRenderGameOverlay() {
        //System.out.println("[LegacyExample] onRenderGameOverlay called!");
    }

    @Override
    public void onPlayerInteract() {
        System.out.println("[LegacyExample] onPlayerInteract called!");
    }

    @SubscribeEvent
    public static void debugClick(PlayerInteractEvent.RightClickBlock event) {
        System.out.println("[LegacyEventDispatcher] Right click detected on block: " + event.getPos());
    }

    @Override
    public void onEntityInteract(LivingEntity target) {
        System.out.println("[LegacyExample] Interacted with entity: " + target.getName().getString());
    }

    @Override
    public void onPreRenderOverlay(GuiGraphics guiGraphics) {
        Minecraft mc = Minecraft.getInstance();
        guiGraphics.drawString(mc.font, "PRE-OVERLAY", 100, 90, 0xAAAAAA, false);
    }

    @Override
    public void onScreenOpen(Screen screen) {
        if (legacyEditBox != null) {
            savedText = legacyEditBox.getValue();
            savedCursor = legacyEditBox.getCursorPosition();
        }

        if (screen instanceof InventoryScreen) {
            System.out.println("インベントリ画面が開かれました！");
        }
    }

    @Override
    public void onScreenRender(Screen screen, GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        guiGraphics.drawString(Minecraft.getInstance().font, "Custom Overlay", 10, 10, 0xFFFFFF, false);
    }

    @Override
    public void onGuiRenderPost(Screen screen, GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (screen != null) {
            guiGraphics.drawString(Minecraft.getInstance().font, "Post Render", 150, 150, 0xFFAA00, false);
        }
    }

    @Override
    public void onGuiKeyPressed(Screen screen, int keyCode, int scanCode, int modifiers) {
        System.out.println("[LegacyExample] Key pressed: " + keyCode + ", Screen: " + screen.getClass().getSimpleName());
    }

    @Override
    public void onChatInput(String message) {
        System.out.println("[LegacyExample] Chat input detected: " + message);
    }

    @Override
    public void onGuiInit(Screen screen, List<LegacyWidgetWrapper> widgets) {
        SafeConfigManager.load();

        // スライダー
        exampleSlider = new LegacySlider(10, 90, 150, 20, SafeConfigManager.getSlider());
        exampleSlider.setResponder(val -> SafeConfigManager.setSlider(val));
        widgets.add(new LegacyWidgetWrapper(exampleSlider, exampleSlider::tick)
                .withTooltip((gfx, pos) -> gfx.renderTooltip(Minecraft.getInstance().font,
                        Component.literal("Adjust the slider value"), pos.x, pos.y)));

        // チェックボックス
        LegacyCheckbox checkbox = new LegacyCheckbox(10, 60, 150, 20,
                Component.literal("Enable Feature"), SafeConfigManager.getCheckbox()) {
            @Override
            public void onPress() {
                super.onPress();
                SafeConfigManager.setCheckbox(selected());
            }
        };
        widgets.add(new LegacyWidgetWrapper(checkbox)
                .withTooltip((gfx, pos) -> gfx.renderTooltip(Minecraft.getInstance().font,
                        Component.literal("Enable or disable the feature"), pos.x, pos.y)));

        // テキストボックス
        legacyEditBox = new LegacyEditBox(10, 120, 150, 20);
        legacyEditBox.setMaxLength(50);
        legacyEditBox.setResponder(text -> SafeConfigManager.setText(text));
        legacyEditBox.setValue(SafeConfigManager.getText());
        widgets.add(new LegacyWidgetWrapper(legacyEditBox)
                .withTooltip((gfx, pos) -> gfx.renderTooltip(Minecraft.getInstance().font,
                        Component.literal("Enter custom text here"), pos.x, pos.y)));
    }

    @Override
    public void onGuiMouseClicked (Screen screen,double mouseX, double mouseY, int button){
        System.out.println("[LegacyExample] Mouse clicked: " + button + " at (" + mouseX + ", " + mouseY + ")");
    }
}