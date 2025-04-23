package com.example.compatmod.legacy.api;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

public class LegacyEventBus {

    public static void register(Object listener) {
        MinecraftForge.EVENT_BUS.register(listener);
    }

    public static void unregister(Object listener) {
        MinecraftForge.EVENT_BUS.unregister(listener);
    }

    public static IEventBus getBus() {
        return MinecraftForge.EVENT_BUS;
    }
}
