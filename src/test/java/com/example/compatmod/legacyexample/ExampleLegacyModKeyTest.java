package com.example.compatmod.legacyexample;

import com.example.compatmod.legacy.api.BaseMod;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public class ExampleLegacyModKeyTest extends BaseMod {
    private static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void onLoad() {
        LOGGER.info("[LegacyExample] ExampleLegacyModKeyTest loaded!");
    }

    @Override
    public void onKeyInput(int keyCode, boolean pressed) {
        LOGGER.info("[LegacyExample] Key pressed: {} Pressed: {}", keyCode, pressed);
    }
}
