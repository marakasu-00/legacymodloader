package com.example.compatmod.legacy.network;

import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Simple packet handler functional interface.
 */
@FunctionalInterface
public interface LegacyPacketHandler<T extends LegacyPacket> {
    /**
     * Handles the packet.
     *
     * @param message packet instance
     * @param context network context
     */
    void handle(T message, Supplier<NetworkEvent.Context> context);
}
