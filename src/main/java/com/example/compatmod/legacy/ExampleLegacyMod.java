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
        System.out.println("[LegacyExample] ExampleLegacyMod loaded!");
    }

    @Override
    public void onClientTick() {
        // ここも空でOK（テスト目的）
    }

    @Override
    public void onEntityHurt(LivingEntity entity, DamageSource source, float amount) {
        System.out.println("[LegacyExample] Entity hurt: " + entity.getName().getString() + " Damage: " + amount);
    }
}

