package com.example.compatmod.legacy.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

/**
 * Replacement for 1.12.2's SimpleNetworkWrapper.
 * Internally uses Forge's modern SimpleChannel API.
 */
public class LegacySimpleChannel {
    private final SimpleChannel channel;
    private int index = 0;

    public LegacySimpleChannel(String name) {
        ResourceLocation id = new ResourceLocation("legacymodloader", name);
        channel = NetworkRegistry.ChannelBuilder.named(id)
                .networkProtocolVersion(() -> "1")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();
    }

    public <T extends LegacyPacket> void registerMessage(Class<T> type, Supplier<T> supplier, LegacyPacketHandler<T> handler) {
        channel.registerMessage(index++, type,
                (msg, buf) -> msg.toBytes(buf),
                buf -> {
                    T msg = supplier.get();
                    msg.fromBytes(buf);
                    return msg;
                },
                (msg, ctxSupplier) -> {
                    handler.handle(msg, ctxSupplier);
                    ctxSupplier.get().setPacketHandled(true);
                });
    }

    public void sendToServer(LegacyPacket msg) {
        channel.sendToServer(msg);
    }

    public void sendToPlayer(LegacyPacket msg, ServerPlayer player) {
        channel.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }

    public void sendToAll(LegacyPacket msg) {
        channel.send(PacketDistributor.ALL.noArg(), msg);
    }
}
