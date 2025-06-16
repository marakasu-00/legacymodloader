package com.example.compatmod.legacy.network;

import net.minecraft.network.FriendlyByteBuf;

/**
 * Basic packet interface mirroring the 1.12.2 {@code IMessage} contract.
 */
public interface LegacyPacket {
    /**
     * Reads this packet from the given buffer.
     *
     * @param buf buffer to read from
     */
    void fromBytes(FriendlyByteBuf buf);

    /**
     * Writes this packet to the given buffer.
     *
     * @param buf buffer to write to
     */
    void toBytes(FriendlyByteBuf buf);
}
