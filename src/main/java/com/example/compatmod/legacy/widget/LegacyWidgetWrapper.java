package com.example.compatmod.legacy.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;

import java.awt.*;
import java.util.function.BiConsumer;

public class LegacyWidgetWrapper {
    private final AbstractWidget widget;
    private final Runnable tickHandler; // Optional
    private BiConsumer<GuiGraphics, Point> tooltipRenderer;
    private boolean visible = true;
    private boolean enabled = true;
    private Runnable refreshHandler;

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
        if (tooltipRenderer != null && widget.isMouseOver(mouseX, mouseY)) {
            tooltipRenderer.accept(graphics, new Point(mouseX, mouseY));
        }
    }

    public void setTooltipRenderer(BiConsumer<GuiGraphics, Point> renderer) {
        this.tooltipRenderer = renderer;
    }
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return widget.mouseClicked(mouseX, mouseY, button);
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return widget.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return widget.mouseReleased(mouseX, mouseY, button);
    }
    public boolean isHovered(double mouseX, double mouseY) {
        return widget.isMouseOver(mouseX, mouseY);
    }
    public void setVisible(boolean visible) {
        this.visible = visible;
        widget.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        widget.active = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public LegacyWidgetWrapper withTooltip(BiConsumer<GuiGraphics, Point> renderer) {
        this.tooltipRenderer = renderer;
        return this;
    }
    public LegacyWidgetWrapper withRefresh(Runnable refresher) {
        this.refreshHandler = refresher;
        return this;
    }

    public void refresh() {
        if (refreshHandler != null) refreshHandler.run();
    }

}


