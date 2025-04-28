package com.example.compatmod.legacy.api;

public abstract class BaseMod implements ILegacyMod {

    public void load() {
        // サブクラスがオーバーライドして使う（デフォルトは何もしない）
    }

    public void onEnable() {
        // 何もしない、サブクラスがオーバーライドして使う
    }

    public void onDisable() {
        // 何もしない、サブクラスがオーバーライドして使う
    }

    public void onTick() {
        // 何もしない、サブクラスがオーバーライドして使う
    }
}
