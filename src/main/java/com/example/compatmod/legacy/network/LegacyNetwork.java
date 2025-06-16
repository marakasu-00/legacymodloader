package com.example.compatmod.legacy.network;

/**
 * Utility for obtaining legacy compatible channels.
 */
public class LegacyNetwork {
    public static LegacySimpleChannel createChannel(String name) {
        return new LegacySimpleChannel(name);
    }
}
