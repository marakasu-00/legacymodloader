package com.example.compatmod.legacy.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.function.Function;

public class LegacyNBTList {

    private final ListTag list;

    public LegacyNBTList() {
        this.list = new ListTag();
    }

    public LegacyNBTList(ListTag tag) {
        this.list = tag;
    }

    public void appendCompound(CompoundTag compound) {
        list.add(compound);
    }

    public CompoundTag getCompound(int index) {
        Tag tag = list.get(index);
        if (tag instanceof CompoundTag compound) {
            return compound;
        }
        throw new IllegalStateException("Element at index " + index + " is not a CompoundTag");
    }

    public int size() {
        return list.size();
    }

    public ListTag getInternal() {
        return list;
    }

    public LegacyNBT getCompoundAsWrapper(int index) {
        return new LegacyNBT(getCompound(index));
    }

    public <T> T mapCompound(int index, Function<CompoundTag, T> mapper) {
        return mapper.apply(getCompound(index));
    }
}
