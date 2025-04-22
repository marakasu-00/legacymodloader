package com.example.compatmod.legacy.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.*;
@SuppressWarnings("removal")
public class OreDictionary {
    private static final Map<String, List<Item>> ORE_ENTRIES = new HashMap<>();

    public static void registerOre(String name, ItemStack stack) {
        ORE_ENTRIES.computeIfAbsent(name, k -> new ArrayList<>()).add(stack.getItem());
    }

    public static List<Item> getOres(String name) {
        return ORE_ENTRIES.getOrDefault(name, Collections.emptyList());
    }

    public static TagKey<Item> getOreTag(String name) {
        return ItemTags.create(new ResourceLocation("forge", name));
    }
    public static boolean doesOreNameExist(String name) {
        return ORE_ENTRIES.containsKey(name);
    }
    public static Ingredient getIngredient(String name) {
        List<Item> items = getOres(name);
        if (items.isEmpty()) {
            return Ingredient.EMPTY;
        }
        return Ingredient.of(items.toArray(new Item[0]));
    }
    public static TagKey<Item> getStandardizedTag(String oreName) {
        String path = oreName.toLowerCase(Locale.ROOT).replaceAll("([A-Z])", "_$1").toLowerCase();
        return ItemTags.create(new ResourceLocation("forge", path));
    }
    public static void registerOre(String name, Item... items) {
        for (Item item : items) {
            registerOre(name, new ItemStack(item));
        }
    }

}
