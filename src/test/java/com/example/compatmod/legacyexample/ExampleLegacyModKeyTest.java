package com.example.compatmod.legacyexample;

import com.example.compatmod.legacy.api.BaseMod;

public class ExampleLegacyModKeyTest extends BaseMod {

    @Override
    public void onLoad() {
        System.out.println("[LegacyExample] ExampleLegacyModKeyTest loaded!");
    }

    @Override
    public void onKeyInput(int keyCode, boolean pressed) {
        System.out.println("[LegacyExample] Key pressed: " + keyCode + " Pressed: " + pressed);
    }
}
