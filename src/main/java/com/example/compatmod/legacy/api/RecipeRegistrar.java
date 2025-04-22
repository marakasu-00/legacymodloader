package com.example.compatmod.legacy.api;

import net.minecraft.world.item.crafting.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeRegistrar {

    private static final List<Recipe<?>> RECIPES = new ArrayList<>();

    public static void queue(Recipe<?> recipe) {
        RECIPES.add(recipe);
    }

    public static List<Recipe<?>> getQueuedRecipes() {
        return List.copyOf(RECIPES);
    }
}
