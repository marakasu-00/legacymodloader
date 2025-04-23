package com.example.compatmod.legacy.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class LegacyWorldHelper {

    public static void playSound(Level world, BlockPos pos, SoundEvent sound, float volume, float pitch) {
        world.playSound(null, pos, sound, SoundSource.BLOCKS, volume, pitch);
    }

    public static void playEvent(Level world, BlockPos pos, GameEvent event) {
        world.gameEvent(null, event, pos);
    }

    public static void spawnParticle(Level world, ParticleOptions particle, double x, double y, double z,
                                     double dx, double dy, double dz) {
        world.addParticle(particle, x, y, z, dx, dy, dz);
    }
}
