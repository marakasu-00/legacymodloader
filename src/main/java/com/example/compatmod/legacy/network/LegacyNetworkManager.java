package com.example.compatmod.legacy.network;

/**
 * Utility class to create network channels for legacy mods.
 */
public final class LegacyNetworkManager {
    private LegacyNetworkManager() {}

    /**
     * Creates a new {@link LegacyNetworkChannel} for the given mod id.
     *
     * @param modId mod identifier
     * @return created channel
     */
    public static LegacyNetworkChannel createChannel(String modId) {
        return new LegacyNetworkChannel(modId);
    }
}
