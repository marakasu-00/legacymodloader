package com.example.compatmod.legacy.network;

import net.minecraft.network.FriendlyByteBuf;

/**
 * Simple packet interface mimicking the old 1.12.2 IMessage API.
 */
public interface LegacyPacket {
    /**
     * Reads packet data from the buffer.
     */
    void fromBytes(FriendlyByteBuf buf);

    /**
     * Writes packet data to the buffer.
     */
    void toBytes(FriendlyByteBuf buf);
}
