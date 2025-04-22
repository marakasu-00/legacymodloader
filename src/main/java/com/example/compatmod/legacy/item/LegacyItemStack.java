package com.example.compatmod.legacy.item;

import com.example.compatmod.legacy.nbt.NBTTagCompound;
import net.minecraft.world.item.ItemStack;

public class LegacyItemStack {
    private final ItemStack handle;

    public LegacyItemStack(ItemStack stack) {
        this.handle = stack;
    }

    public void setTagCompound(NBTTagCompound tag) {
        handle.setTag(tag.getHandle());
    }

    public NBTTagCompound getTagCompound() {
        return new NBTTagCompound(handle.getTag());
    }

    public ItemStack getHandle() {
        return handle;
    }
}
