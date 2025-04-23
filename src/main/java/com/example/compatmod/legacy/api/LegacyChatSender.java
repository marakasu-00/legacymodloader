package com.example.compatmod.legacy.api;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class LegacyChatSender {

    public static void sendChat(ServerPlayer player, Component message) {
        player.sendSystemMessage(message);
    }

    public static void sendColored(ServerPlayer player, String message, ChatFormatting color) {
        Component text = Component.literal(message).withStyle(color);
        player.sendSystemMessage(text);
    }

    public static void sendTranslation(ServerPlayer player, String translationKey) {
        player.sendSystemMessage(Component.translatable(translationKey));
    }
}
