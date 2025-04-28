package com.example.compatmod.legacy;

import com.example.compatmod.legacy.api.event.ILegacyEntityEventListener;
import com.example.compatmod.legacy.api.ILegacyMod;
import com.example.compatmod.legacy.event.LegacyEntityEventDispatcher;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;


public class ExampleLegacyMod implements ILegacyMod, ILegacyEntityEventListener {

    public ExampleLegacyMod() {
        LegacyEntityEventDispatcher.register(this); // ★ここで登録
    }

    @Override
    public void onLoad() {
        System.out.println("[LegacyExample] onLoad called!");
        LegacyEntityEventDispatcher.register(this);
    }

    @Override
    public void onClientTick() {
        System.out.println("[LegacyExample] Client tick!");
    }

    @Override
    public void onEntityHurt(LivingEntity entity, DamageSource source, float amount) {
        System.out.println("[LegacyExample] Entity hurt: " + entity.getName().getString() + " Damage: " + amount);
    }
    @Override
    public void onServerTick() {
        System.out.println("[LegacyExample] Server tick called!");
    }

}

