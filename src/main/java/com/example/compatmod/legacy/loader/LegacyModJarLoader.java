package com.example.compatmod.legacy.loader;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.lang.reflect.Modifier;

public class LegacyModJarLoader {

    private final File legacyModsFolder;

    public LegacyModJarLoader(File modsFolder) {
        this.legacyModsFolder = new File(modsFolder, "legacy");
        if (!legacyModsFolder.exists()) {
            legacyModsFolder.mkdirs();
        }
    }

    public List<Class<?>> loadAllLegacyMods() {
        List<Class<?>> loadedClasses = new ArrayList<>();

        File[] jars = legacyModsFolder.listFiles((dir, name) -> name.endsWith(".jar"));
        if (jars == null) return loadedClasses;

        for (File jar : jars) {
            try {
                URL jarUrl = jar.toURI().toURL();
                URLClassLoader classLoader = new URLClassLoader(new URL[]{jarUrl}, this.getClass().getClassLoader());

                try (JarFile jarFile = new JarFile(jar)) {
                    jarFile.stream()
                            .filter(entry -> entry.getName().endsWith(".class") && !entry.isDirectory())
                            .forEach(entry -> {
                                String className = entry.getName()
                                        .replace('/', '.')
                                        .replace('\\', '.')
                                        .replace(".class", "");
                                try {
                                    Class<?> clazz = classLoader.loadClass(className);

                                    // BaseMod 継承チェック
                                    if (!Modifier.isAbstract(clazz.getModifiers()) && isLegacyModClass(clazz)) {
                                        System.out.println("[LegacyLoader] Loaded legacy mod class: " + className);
                                        initializeModClass(clazz); // インスタンス化と初期化を実行
                                        loadedClasses.add(clazz);
                                    }

                                } catch (Throwable t) {
                                    // クラス読み込み失敗しても無視
                                }
                            });
                }

            } catch (Exception e) {
                System.err.println("[LegacyLoader] Failed to process: " + jar.getName());
                e.printStackTrace();
            }
        }

        return loadedClasses;
    }

    private boolean isLegacyModClass(Class<?> clazz) {
        // BaseMod を継承しているかどうかをチェック
        return clazz.getSuperclass() != null &&
                clazz.getSuperclass().getSimpleName().equals("BaseMod");
    }
    private void initializeModClass(Class<?> clazz) {
        try {
            // クラスのインスタンス化
            Object instance = clazz.getDeclaredConstructor().newInstance();
            System.out.println("[LegacyLoader] Instantiated mod class: " + clazz.getName());

            // 初期化メソッドを順に呼び出す
            tryCallMethod(instance, "load");
            tryCallMethod(instance, "init");
            tryCallMethod(instance, "onEnable");

        } catch (Exception e) {
            System.err.println("[LegacyLoader] Failed to initialize mod class: " + clazz.getName());
            e.printStackTrace();
        }
    }

    private void tryCallMethod(Object instance, String methodName) {
        try {
            // メソッドを取得して呼び出し
            var method = instance.getClass().getMethod(methodName);
            method.setAccessible(true);
            method.invoke(instance);
            System.out.println("[LegacyLoader] Called method: " + methodName);
        } catch (NoSuchMethodException e) {
            // メソッドが存在しない場合は無視
        } catch (Exception e) {
            System.err.println("[LegacyLoader] Error calling " + methodName + ": " + e.getMessage());
        }
    }
}