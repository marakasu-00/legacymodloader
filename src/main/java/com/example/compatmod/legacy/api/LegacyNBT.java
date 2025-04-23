package com.example.compatmod.legacy.api;

import net.minecraft.nbt.CompoundTag;

public class LegacyNBT {

    private final CompoundTag tag;

    public LegacyNBT() {
        this.tag = new CompoundTag();
    }

    public LegacyNBT(CompoundTag tag) {
        this.tag = tag;
    }

    public void setString(String key, String value) {
        tag.putString(key, value);
    }

    public String getString(String key) {
        return tag.getString(key);
    }

    public void setInt(String key, int value) {
        tag.putInt(key, value);
    }

    public int getInt(String key) {
        return tag.getInt(key);
    }

    public void setBoolean(String key, boolean value) {
        tag.putBoolean(key, value);
    }

    public boolean getBoolean(String key) {
        return tag.getBoolean(key);
    }

    public boolean hasKey(String key) {
        return tag.contains(key);
    }

    public CompoundTag getInternal() {
        return tag;
    }
}
