package com.example.compatmod.legacy.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class LegacyItemModifier {

    public static void setDisplayName(ItemStack stack, Component name) {
        stack.setHoverName(name);
    }

    public static void addEnchantment(ItemStack stack, Enchantment enchantment, int level) {
        EnchantmentHelper.setEnchantments(
                EnchantmentHelper.getEnchantments(stack), stack
        );
        stack.enchant(enchantment, level);
    }

    public static boolean isEnchanted(ItemStack stack) {
        return stack.isEnchanted();
    }

    public static void clearCustomName(ItemStack stack) {
        if (stack.hasCustomHoverName()) {
            CompoundTag tag = stack.getTag();
            if (tag != null) {
                tag.remove("display");
            }
        }
    }
}
