package com.example.compatmod.legacy.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;

import java.awt.*;
import java.util.function.BiConsumer;

public class LegacyWidgetWrapper {
    private final AbstractWidget widget;
    private final Runnable tickHandler; // Optional
    private BiConsumer<GuiGraphics, Point> tooltipRenderer;

    public LegacyWidgetWrapper(AbstractWidget widget, Runnable tickHandler) {
        this.widget = widget;
        this.tickHandler = tickHandler;
    }

    public LegacyWidgetWrapper(AbstractWidget widget) {
        this(widget, null);
    }

    public AbstractWidget getWidget() {
        return widget;
    }

    public void tick() {
        if (tickHandler != null) {
            tickHandler.run();
        }
    }
    public void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        if (tooltipRenderer != null) {
            tooltipRenderer.accept(graphics, new Point(mouseX, mouseY));
        }
    }

    public void setTooltipRenderer(BiConsumer<GuiGraphics, Point> renderer) {
        this.tooltipRenderer = renderer;
    }
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return widget.mouseClicked(mouseX, mouseY, button);
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return widget.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (widget.isMouseOver(mouseX, mouseY)) {
            return widget.mouseReleased(mouseX, mouseY, button);
        }
        return false;
    }
    public boolean isHovered(double mouseX, double mouseY) {
        return widget.isMouseOver(mouseX, mouseY);
    }
}


