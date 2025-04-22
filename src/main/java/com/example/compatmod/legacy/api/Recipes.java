package com.example.compatmod.legacy.api;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.core.NonNullList;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.item.crafting.CookingBookCategory;

import java.util.HashMap;
import java.util.Map;

public class Recipes {

    public static void addShapedRecipe(ResourceLocation id, ItemStack result, Object... params) {
        String[] pattern = new String[3];
        int patternLines = 0;

        Map<Character, Ingredient> key = new HashMap<>();
        int i = 0;

        // 1. パターン読み取り
        while (i < params.length && params[i] instanceof String && patternLines < 3) {
            pattern[patternLines++] = (String) params[i++];
        }

        // 2. キー読み取り
        while (i + 1 < params.length) {
            char symbol = (Character) params[i++];
            Object value = params[i++];

            Ingredient ingredient;

            if (value instanceof ItemStack stack) {
                ingredient = Ingredient.of(stack);
            } else if (value instanceof Item item) {
                ingredient = Ingredient.of(item);
            } else if (value instanceof String oreName) {
                ingredient = com.example.compatmod.legacy.api.OreDictionary.getIngredient(oreName);
            } else {
                throw new IllegalArgumentException("Invalid shaped recipe ingredient for symbol: " + symbol);
            }

            key.put(symbol, ingredient);
        }

        int width = pattern[0].length();
        NonNullList<Ingredient> ingredients = NonNullList.withSize(width * patternLines, Ingredient.EMPTY);

        for (int row = 0; row < patternLines; ++row) {
            String line = pattern[row];
            for (int col = 0; col < width; ++col) {
                char symbol = line.charAt(col);
                ingredients.set(row * width + col, key.getOrDefault(symbol, Ingredient.EMPTY));
            }
        }

        ShapedRecipe recipe = new ShapedRecipe(
                id,
                "",
                CraftingBookCategory.MISC, // 追加
                width,
                patternLines,
                ingredients,
                result,
                true // 通知ON（旧仕様に近づけるため）
        );
        com.example.compatmod.legacy.api.RecipeRegistrar.queue(recipe);
    }
    public static void addShapelessRecipe(ResourceLocation id, ItemStack result, Object... ingredientsRaw) {
        NonNullList<Ingredient> ingredients = NonNullList.create();

        for (Object obj : ingredientsRaw) {
            Ingredient ingredient;

            if (obj instanceof ItemStack stack) {
                ingredient = Ingredient.of(stack);
            } else if (obj instanceof Item item) {
                ingredient = Ingredient.of(item);
            } else if (obj instanceof String oreName) {
                ingredient = OreDictionary.getIngredient(oreName);
            } else {
                throw new IllegalArgumentException("Invalid shapeless recipe ingredient: " + obj);
            }

            ingredients.add(ingredient);
        }

        var recipe = new ShapelessRecipe(
                id,
                "",
                CraftingBookCategory.MISC, // 追加
                result,
                ingredients
        );

        RecipeRegistrar.queue(recipe);
    }
    public static void addSmelting(ResourceLocation id, Item input, ItemStack result, float xp) {
        Ingredient ingredient = Ingredient.of(input);
        var recipe = new SmeltingRecipe(
                id,
                "",
                CookingBookCategory.MISC, // 追加
                ingredient,
                result,
                xp,
                200
        );
        RecipeRegistrar.queue(recipe);
    }

}
