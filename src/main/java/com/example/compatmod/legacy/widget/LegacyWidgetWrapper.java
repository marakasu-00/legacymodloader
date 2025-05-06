package com.example.compatmod.legacy.widget;

import net.minecraft.client.gui.components.AbstractWidget;

public class LegacyWidgetWrapper {
    private final AbstractWidget widget;
    private final Runnable tickHandler; // Optional

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
}


