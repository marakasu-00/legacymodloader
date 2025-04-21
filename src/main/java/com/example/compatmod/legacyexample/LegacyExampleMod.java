package com.example.compatmod.legacyexample;

import com.example.compatmod.legacy.LegacyGameRegistry;
import net.minecraft.world.item.Item;

public class LegacyExampleMod {

    public static void init() {
        Item exampleItem = new Item(new Item.Properties());
        LegacyGameRegistry.registerItem(exampleItem, "example_item");
    }
}