package com.example.compatmod.legacy.network;

import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
<<<<<<< codex/create-helper-classes-for-packet-translation
 * Handler for legacy packets. Implementations should process the message and
 * mark the packet as handled when done.
 */
public interface LegacyPacketHandler<T extends LegacyPacket> {
=======
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
>>>>>>> 20250612
    void handle(T message, Supplier<NetworkEvent.Context> context);
}
