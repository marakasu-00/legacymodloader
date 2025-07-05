package com.example.codex.client;

import com.example.compatmod.legacy.item.LegacyItem;
import com.example.compatmod.legacy.lang.LegacyLangManager;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class CodexDetailScreen extends Screen {
    private final LegacyItem item;
    private Button backButton;

    public CodexDetailScreen(LegacyItem item) {
        super(Component.literal("Codex: " + item.getModId() + ":" + item.getLegacyId()));
        this.item = item;
    }

    @Override
    protected void init() {
        int btnWidth = 60;
        backButton = Button.builder(Component.literal("Back"), b -> onClose())
                .pos(10, 10)
                .size(btnWidth, 20)
                .build();
        addRenderableWidget(backButton);
    }

    @SuppressWarnings("removal")
    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        int x = this.width / 2 - 80;
        int y = 50;

        // テクスチャ
        ResourceLocation texture = new ResourceLocation(item.getModId(), "textures/item/" + item.getLegacyId() + ".png");
        graphics.blit(texture, x, y, 0, 0, 64, 64, 64, 64);

        // 名前
        Component name = item.getName(ItemStack.EMPTY);
        graphics.drawString(this.font, name, x, y + 70, 0xFFFFFF);

        // 説明
        Optional<String> descOpt = LegacyLangManager.getDisplayName(item.getModId(), item.getLegacyId() + ".desc");
        descOpt.ifPresent(desc ->
                graphics.drawString(this.font, Component.literal(desc), x, y + 90, 0xAAAAAA)
        );

        // mod名
        graphics.drawString(this.font, Component.literal("Mod: " + item.getModId()), x, y + 110, 0x888888);

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
