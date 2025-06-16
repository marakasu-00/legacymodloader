package com.example.compatmod.legacy.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

/**
 * A lightweight wrapper around Forge's {@link SimpleChannel} that mimics the
 * behaviour of the old 1.12.2 {@code SimpleNetworkWrapper} API.
 */
public class LegacyNetworkChannel {
    private final SimpleChannel channel;
    private int id = 0;

    public LegacyNetworkChannel(String modId) {
        channel = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(modId, "legacy"))
                .networkProtocolVersion(() -> "1")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();
    }

    /**
     * Registers a packet and its handler.
     */
    public <T extends LegacyPacket> void register(Class<T> type,
                                                  Supplier<T> supplier,
                                                  LegacyPacketHandler<T> handler) {
        channel.messageBuilder(type, id++)
                .encoder(LegacyPacket::toBytes)
                .decoder(buf -> {
                    T msg = supplier.get();
                    msg.fromBytes(buf);
                    return msg;
                })
                .consumerMainThread((msg, ctx) -> {
                    handler.handle(msg, ctx);
                    ctx.get().setPacketHandled(true);
                })
                .add();
    }

    /** Sends a packet to the server. */
    public void sendToServer(LegacyPacket msg) {
        channel.sendToServer(msg);
    }

    /** Sends a packet to the specified player. */
    public void sendToPlayer(LegacyPacket msg, ServerPlayer player) {
        channel.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }

    /** Broadcasts a packet to all connected players. */
    public void broadcast(LegacyPacket msg) {
        channel.send(PacketDistributor.ALL.noArg(), msg);
    }
}
