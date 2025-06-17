package com.example.compatmod.legacy.network;

import net.minecraft.network.FriendlyByteBuf;

/**
<<<<<<< codex/create-helper-classes-for-packet-translation
 * Simple packet interface mimicking the old 1.12.2 IMessage API.
 */
public interface LegacyPacket {
    /**
     * Reads packet data from the buffer.
=======
 * Basic packet interface mirroring the 1.12.2 {@code IMessage} contract.
 */
public interface LegacyPacket {
    /**
     * Reads this packet from the given buffer.
     *
     * @param buf buffer to read from
>>>>>>> 20250612
     */
    void fromBytes(FriendlyByteBuf buf);

    /**
<<<<<<< codex/create-helper-classes-for-packet-translation
     * Writes packet data to the buffer.
=======
     * Writes this packet to the given buffer.
     *
     * @param buf buffer to write to
>>>>>>> 20250612
     */
    void toBytes(FriendlyByteBuf buf);
}
