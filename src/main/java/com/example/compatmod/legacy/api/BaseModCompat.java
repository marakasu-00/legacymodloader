package com.example.compatmod.legacy.api;

public interface BaseModCompat {

    /**
     * 初期読み込み時に呼ばれる
     */
    default void load() {}

    /**
     * 初期化段階で呼ばれる（アイテム・ブロック登録など）
     */
    default void init() {}

    /**
     * 有効化時に呼ばれる（イベントバス登録など）
     */
    default void onEnable() {}

    /**
     * 無効化時に呼ばれる（リソース破棄など）
     */
    default void onDisable() {}

    /**
     * このMODのIDまたは識別子（内部管理用）
     */
    String getModId();
}
