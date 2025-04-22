package com.example.compatmod.legacyexample;

import com.example.compatmod.legacy.LegacyGameRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;

public class LegacyExampleMod {
    public static void register() {
        Item exampleItem = new Item(new Properties());
        LegacyGameRegistry.registerItem("example_item", () -> new Item(new Item.Properties()));

    }
}
