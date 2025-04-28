package com.example.compatmod.legacy.event;

import com.example.compatmod.legacy.api.event.ILegacyEntityEventListener;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

/**
 * レガシーMODのエンティティイベントを管理・中継するクラス
 */
@Mod.EventBusSubscriber(modid = "legacymodloader")
public class LegacyEntityEventDispatcher {

    private static final List<ILegacyEntityEventListener> LISTENERS = new ArrayList<>();

    /**
     * リスナーを登録
     */
    public static void register(ILegacyEntityEventListener listener) {
        LISTENERS.add(listener);
        System.out.println("[LegacyEntityEventDispatcher] Registered listener: " + listener.getClass().getName());
    }

    /**
     * ForgeのLivingHurtEventからレガシーリスナーに通知
     */
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();
        float amount = event.getAmount();

        for (ILegacyEntityEventListener listener : LISTENERS) {
            listener.onEntityHurt(entity, source, amount);
        }
    }
}
