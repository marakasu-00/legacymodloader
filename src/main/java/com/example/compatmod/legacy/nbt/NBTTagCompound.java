package com.example.compatmod.legacy.nbt;

import net.minecraft.nbt.CompoundTag;

public class NBTTagCompound {
    private final CompoundTag tag;

    public NBTTagCompound() {
        this.tag = new CompoundTag();
    }

    public NBTTagCompound(CompoundTag tag) {
        this.tag = tag;
    }

    public void setString(String key, String value) {
        tag.putString(key, value);
    }

    public String getString(String key) {
        return tag.getString(key);
    }

    public CompoundTag getHandle() {
        return tag;
    }
}
