package com.example.compatmod.legacy.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class LegacyItemHelper {

    public static Component getDisplayName(ItemStack stack) {
        return stack.getHoverName(); // 旧: getDisplayName()
    }

    public static ItemStack copy(ItemStack stack) {
        return stack.copy();
    }

    public static CompoundTag getTagCompound(ItemStack stack) {
        return stack.getTag();
    }

    public static boolean hasTagCompound(ItemStack stack) {
        return stack.hasTag();
    }

    public static void setTagCompound(ItemStack stack, CompoundTag tag) {
        stack.setTag(tag);
    }
}
