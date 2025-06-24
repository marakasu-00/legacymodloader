package com.example.compatmod.legacy.loader;

import com.example.compatmod.legacy.api.ILegacyMod;
import net.minecraftforge.fml.loading.FMLPaths;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import static com.example.compatmod.legacy.loader.LegacyPaths.LEGACY_ASSETS_PATH;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.lang.reflect.Modifier;

public class LegacyModJarLoader {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final File legacyModsFolder;
    private final String thisModFileName;

    public LegacyModJarLoader(File legacyModsFolder) {
        this.legacyModsFolder = legacyModsFolder;
        // 自分自身のJAR名を取得（例: legacymodloader-1.20.1-1.0.jar）
        thisModFileName = getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
    }

    public List<Class<?>> loadAllLegacyMods() {
        List<Class<?>> loadedClasses = new ArrayList<>();
        File[] jars = legacyModsFolder.listFiles((dir, name) -> name.endsWith(".jar"));
        if (jars == null) return loadedClasses;

        File assetsDir = FMLPaths.GAMEDIR.get().resolve(LEGACY_ASSETS_PATH).toFile();
        if (!assetsDir.exists()) {
            assetsDir.mkdirs();
        }

        for (File jar : jars) {
            try {
                // 自分自身のJARファイルはスキップ
                if (thisModFileName.contains(jar.getName())) {
                    LOGGER.info("[LegacyModJarLoader] Skipped own mod jar: {}", jar.getName());
                    continue;
                }

                // Extract legacy assets from this jar
                extractLegacyAssets(jar, assetsDir);

                URL jarUrl = jar.toURI().toURL();
                try (URLClassLoader classLoader = new URLClassLoader(new URL[]{jarUrl}, this.getClass().getClassLoader());
                     JarFile jarFile = new JarFile(jar)) {
                    Enumeration<JarEntry> entries = jarFile.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        if (entry.getName().endsWith(".class")) {
                            String className = entry.getName().replace('/', '.').replace(".class", "");
                            try {
                                Class<?> clazz = classLoader.loadClass(className);
                                if (!Modifier.isAbstract(clazz.getModifiers()) && isLegacyModClass(clazz)) {
                                    LOGGER.info("[LegacyLoader] Loaded legacy mod class: {}", className);
                                    initializeModClass(clazz);
                                    loadedClasses.add(clazz);
                                }
                            } catch (Throwable ignored) {
                            }
                        }
                    }
                }
            } catch (IOException e) {
                LOGGER.error("[LegacyLoader] Failed to process jar: {}", jar.getName(), e);
            }
        }

        // After extraction, ensure the assets path is registered
        LegacyModResources.checkLegacyAssetsExist();

        return loadedClasses;
    }

    private void extractLegacyAssets(File legacyJar, File outputDir) {
        try (JarFile jar = new JarFile(legacyJar)) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().startsWith("assets/") && !entry.isDirectory()) { // ←ここ追加
                    String relativePath = entry.getName().substring("assets/".length());
                    File outFile = new File(outputDir, relativePath);

                    File parent = outFile.getParentFile();
                    if (parent != null && !parent.exists()) {
                        parent.mkdirs();
                    }

                    try (InputStream in = jar.getInputStream(entry);
                         OutputStream out = new FileOutputStream(outFile)) {
                        in.transferTo(out);
                        LOGGER.info("[LegacyLoader] Extracted asset: {}", relativePath);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("[LegacyLoader] Failed to extract assets: {}", legacyJar.getName(), e);
        }
    }

    private boolean isLegacyModClass(Class<?> clazz) {
        // BaseMod を継承しているかどうかをチェック
        return clazz.getSuperclass() != null &&
                clazz.getSuperclass().getSimpleName().equals("BaseMod");
    }
    private void initializeModClass(Class<?> clazz) {
        try {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            LOGGER.info("[LegacyLoader] Instantiated mod class: {}", clazz.getName());

            // --- レガシーMODインターフェースを実装していたら登録 ---
            if (instance instanceof ILegacyMod legacyMod) {
                LegacyModManager.addMod(legacyMod);
                LOGGER.info("[LegacyLoader] Registered legacy mod: {}", clazz.getName());
            }

            tryCallMethod(instance, "load");
            tryCallMethod(instance, "init");
            tryCallMethod(instance, "onEnable");

        } catch (Exception e) {
            LOGGER.error("[LegacyLoader] Failed to initialize mod class: {}", clazz.getName(), e);
        }
    }


    private void tryCallMethod(Object instance, String methodName) {
        try {
            // メソッドを取得して呼び出し
            var method = instance.getClass().getMethod(methodName);
            method.setAccessible(true);
            method.invoke(instance);
            LOGGER.info("[LegacyLoader] Called method: {}", methodName);
        } catch (NoSuchMethodException e) {
            // メソッドが存在しない場合は無視
        } catch (Exception e) {
            LOGGER.error("[LegacyLoader] Error calling {}: {}", methodName, e.getMessage());
        }
    }
}
