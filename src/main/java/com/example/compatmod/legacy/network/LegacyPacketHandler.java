package com.example.compatmod.legacy.network;

import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Handler for legacy packets. Implementations should process the message and
 * mark the packet as handled when done.
 */
public interface LegacyPacketHandler<T extends LegacyPacket> {
    void handle(T message, Supplier<NetworkEvent.Context> context);
}
