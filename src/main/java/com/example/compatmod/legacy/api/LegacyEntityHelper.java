package com.example.compatmod.legacy.api;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class LegacyEntityHelper {

    public static void attackEntityFrom(LivingEntity entity, DamageSource source, float amount) {
        entity.hurt(source, amount);
    }

    public static boolean isPotionActive(LivingEntity entity, MobEffect effect) {
        return entity.hasEffect(effect);
    }

    public static MobEffectInstance getActivePotionEffect(LivingEntity entity, MobEffect effect) {
        return entity.getEffect(effect);
    }
}
