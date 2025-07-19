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

import java.util.List;
import java.util.Optional;

public class CodexListScreen extends Screen {
    private static final int ENTRY_HEIGHT = 40;
    private static final int ICON_SIZE = 32;

    private int scrollOffset = 0;
    private Button scrollUp;
    private Button scrollDown;

    public CodexListScreen() {
        super(Component.literal("Legacy Codex"));
    }

    @Override
    protected void init() {
        int btnW = 20, btnH = 20;
        int x = this.width - btnW - 10;
        int yUp = 20;
        int yDown = yUp + btnH + 5;

        scrollUp = Button.builder(Component.literal("\u25B2"), b -> scrollOffset = Math.max(0, scrollOffset - ENTRY_HEIGHT)).pos(x, yUp).size(btnW, btnH).build();
        scrollDown = Button.builder(Component.literal("\u25BC"), b -> scrollOffset += ENTRY_HEIGHT).pos(x, yDown).size(btnW, btnH).build();

        addRenderableWidget(scrollUp);
        addRenderableWidget(scrollDown);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = 30;
        int y = 30 - scrollOffset;

        for (LegacyItem item : LegacyItem.getAllLegacyItems()) {
            if (y + ENTRY_HEIGHT < 20) {
                y += ENTRY_HEIGHT;
                continue;
            }
            if (y > this.height - 40) break;

            if (mouseX >= x && mouseX <= x + 200 && mouseY >= y && mouseY <= y + ENTRY_HEIGHT) {
                Minecraft.getInstance().setScreen(new CodexDetailScreen(item));
                return true;
            }

            y += ENTRY_HEIGHT;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        int startY = 30 - scrollOffset;
        int x = 30;

        for (LegacyItem item : LegacyItem.getAllLegacyItems()) {
            if (startY + ENTRY_HEIGHT < 20) {
                startY += ENTRY_HEIGHT;
                continue;
            }
            if (startY > this.height - 40) break;

            drawEntry(graphics, x, startY, item);
            startY += ENTRY_HEIGHT;
        }

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @SuppressWarnings("removal")
    private void drawEntry(GuiGraphics graphics, int x, int y, LegacyItem item) {
        ResourceLocation texture = new ResourceLocation(item.getModId(), "textures/item/" + item.getLegacyId() + ".png");
        graphics.blit(texture, x, y, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);

        Component name = item.getName(ItemStack.EMPTY);
        graphics.drawString(this.font, name, x + ICON_SIZE + 8, y + 4, 0xFFFFFF);

        Optional<String> descOpt = LegacyLangManager.getDisplayName(item.getModId(), item.getLegacyId() + ".desc");
        descOpt.ifPresent(desc -> graphics.drawString(this.font, Component.literal(desc), x + ICON_SIZE + 8, y + 20, 0xAAAAAA));

        graphics.drawString(this.font, Component.literal("Mod: " + item.getModId()), x + ICON_SIZE + 8, y + 32, 0x888888);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}