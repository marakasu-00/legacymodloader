package com.example.compatmod.legacyexample;

import com.example.compatmod.legacy.api.BaseMod;
import com.example.compatmod.legacy.client.ILegacyModClient;

public class ExampleLegacyModKeyTest extends BaseMod implements ILegacyModClient {

    @Override
    public void onLoad() {
        System.out.println("[LegacyExample] ExampleLegacyModKeyTest loaded!");
    }

    @Override
    public void onKeyInput(int keyCode, boolean pressed) {
        System.out.println("[LegacyExample] Key pressed: " + keyCode + " Pressed: " + pressed);
    }
}
