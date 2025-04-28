package com.example.compatmod.legacy.api;

public interface ILegacyMod {

    default void onLoad() {}

    default void onInit() {}

    default void onEnable() {}

    default void onClientTick() {}

    default void onRenderOverlay() {}

    default void onServerTick() {}

    default void onRenderGameOverlay() {}

    default void onKeyInput(int keyCode, boolean pressed) {}

    default void onPlayerInteract() {}
}
