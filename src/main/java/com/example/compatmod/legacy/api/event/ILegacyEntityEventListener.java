package com.example.compatmod.legacy.api.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;

/**
 * レガシーMOD用のエンティティイベントリスナーAPI
 */
public interface ILegacyEntityEventListener {

    /**
     * エンティティがダメージを受けたときに呼び出される
     *
     * @param entity ダメージを受けたエンティティ
     * @param source ダメージソース
     * @param amount ダメージ量
     */
    void onEntityHurt(LivingEntity entity, DamageSource source, float amount);
}
