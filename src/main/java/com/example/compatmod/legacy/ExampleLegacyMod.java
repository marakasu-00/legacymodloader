package com.example.compatmod.legacy;

import com.example.compatmod.config.ConfigHandler;
import com.example.compatmod.config.SafeConfigManager;
import com.example.compatmod.legacy.api.event.ILegacyEntityEventListener;
import com.example.compatmod.legacy.api.ILegacyMod;
import com.example.compatmod.legacy.event.LegacyEntityEventDispatcher;
import com.example.compatmod.legacy.event.LegacyGuiEventHandler;
import com.example.compatmod.legacy.screen.LegacyConfigScreen;
import com.example.compatmod.legacy.widget.LegacyCheckbox;
import com.example.compatmod.legacy.widget.LegacyEditBox;
import com.example.compatmod.legacy.widget.LegacySlider;
import com.example.compatmod.legacy.widget.LegacyWidgetWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

import static com.example.compatmod.config.SafeConfigManager.saveConfigSafe;
import static com.example.compatmod.legacy.event.LegacyGuiEventHandler.clearLegacyWidgets;

public class ExampleLegacyMod implements ILegacyMod, ILegacyEntityEventListener {

    private double savedSliderValue = 0.5;
    private LegacySlider exampleSlider;
    private boolean checkboxChecked = false;
    private String savedInputText = "";
    private int savedCursorPos = 0;
    private String savedText = "";
    private int savedCursor = 0;
    private LegacyCheckbox checkbox;
    private final List<List<LegacyWidgetWrapper>> pages = new ArrayList<>();
    private int currentPage = 0;



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

    private void rebuildGui() {
        Minecraft.getInstance().setScreen(new LegacyConfigScreen()); // 新しく生成して完全リセット
    }

    private void addPageControls(List<LegacyWidgetWrapper> widgets, int centerX) {
        int y = 180;

        if (currentPage > 0) {
            Button back = Button.builder(Component.translatable("compatmod.button.back"), btn -> {
                currentPage--;
                rebuildGui();
            }).bounds(centerX - 75, y, 150, 20).build(); // ✅ 修正: .pos → .bounds
            widgets.add(new LegacyWidgetWrapper(back));
        }

        if (currentPage < pages.size() - 1) {
            Button next = Button.builder(Component.translatable("compatmod.button.next"), btn -> {
                currentPage++;
                rebuildGui();
            }).bounds(centerX - 75, y + 25, 150, 20).build(); // ✅ 修正: .pos → .bounds
            widgets.add(new LegacyWidgetWrapper(next));
        }
    }

    @Override
    public void onGuiInit(Screen screen, List<LegacyWidgetWrapper> widgets) {
        pages.clear();

        int centerX = screen.width / 2;
        int baseY = 60;
        int spacing = 30;

        // === Page 1 ===
        List<LegacyWidgetWrapper> page1 = new ArrayList<>();

        int checkboxWidth = 150;
        int checkboxX = centerX - checkboxWidth / 2;

        LegacyCheckbox checkbox = new LegacyCheckbox(checkboxX, baseY, checkboxWidth, 20,
                Component.translatable("compatmod.checkbox.enable_feature"),
                SafeConfigManager.getCheckbox());
        checkbox.setResponder(checked -> {
            SafeConfigManager.setCheckbox(checked);
            saveConfigSafe();
        });
        page1.add(new LegacyWidgetWrapper(checkbox));
        baseY += spacing;

        int sliderWidth = 150;
        int sliderX = centerX - sliderWidth / 2;

        LegacySlider slider = new LegacySlider(sliderX, baseY, sliderWidth, 20,
                0.0, 1.0, SafeConfigManager.getSlider(),
                Component.translatable("compatmod.label.brightness"));
        slider.setResponder(val -> {
            SafeConfigManager.setSlider(val);
            saveConfigSafe();
        });
        page1.add(new LegacyWidgetWrapper(slider, slider::tick));
        pages.add(page1);

        // === Page 2 ===
        baseY = 60;
        List<LegacyWidgetWrapper> page2 = new ArrayList<>();

        int editBoxWidth = 150;
        int editBoxX = centerX - editBoxWidth / 2;

        LegacyEditBox editBox = new LegacyEditBox(editBoxX, baseY, editBoxWidth, 20);
        editBox.setMaxLength(50);
        editBox.setValue(SafeConfigManager.getText());
        editBox.setResponder(text -> {
            System.out.println("[EditBox] 入力内容変更: " + text);
            SafeConfigManager.setText("[SafeConfigManager] setText called: "  + text);
            SafeConfigManager.saveConfigSafe();
        });

        page2.add(new LegacyWidgetWrapper(editBox)
                .withTooltip((gfx, pos) -> gfx.renderTooltip(
                        Minecraft.getInstance().font,
                        Component.translatable("compatmod.editbox.hint"),
                        pos.x, pos.y)));

        baseY += spacing;

        Button submitButton = Button.builder(Component.translatable("compatmod.button.submit"), btn -> {
            String submitted = SafeConfigManager.getText().trim();

            if (submitted.isEmpty()) {
                Minecraft.getInstance().player.sendSystemMessage(
                        Component.literal("⚠ 入力が空です"));
                return;
            }

            Minecraft.getInstance().player.sendSystemMessage(
                    Component.literal("入力内容: " + submitted));

            Minecraft.getInstance().getToasts().addToast(
                    SystemToast.multiline(
                            Minecraft.getInstance(),
                            SystemToast.SystemToastIds.TUTORIAL_HINT,
                            Component.translatable("compatmod.toast.saved"),
                            Component.literal(submitted)
                    )
            );
        }).bounds(centerX - 75, baseY, 150, 20).build();

        page2.add(new LegacyWidgetWrapper(submitButton));

        pages.add(page2);

        widgets.addAll(pages.get(currentPage));
        addPageControls(widgets, centerX); // ← centerX を渡して中央化
    }

    @Override
    public void onGuiMouseClicked (Screen screen,double mouseX, double mouseY, int button){
        System.out.println("[LegacyExample] Mouse clicked: " + button + " at (" + mouseX + ", " + mouseY + ")");
    }
    public static void setText(String text) {
        if (text == null) text = "";
        try {
            System.out.println("[SafeConfigManager] Saving text: " + text);
            ConfigHandler.SAVED_TEXT.set(text);
            saveConfigSafe();
        } catch (Exception e) {
            System.err.println("[SafeConfigManager] Failed to save text: " + e.getMessage());
        }
    }

}
