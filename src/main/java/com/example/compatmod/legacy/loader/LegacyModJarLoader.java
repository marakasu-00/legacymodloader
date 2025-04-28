package com.example.compatmod.legacy.loader;

import com.example.compatmod.legacy.api.ILegacyMod;

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

        File outputDir = new File("run/resources", "assets");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        for (File jar : jars) {
            try {
                URL jarUrl = jar.toURI().toURL();
                URLClassLoader classLoader = new URLClassLoader(new URL[]{jarUrl}, this.getClass().getClassLoader());

                // --- assets 展開を先に実行 ---
                extractLegacyAssets(jar, outputDir);

                try (JarFile jarFile = new JarFile(jar)) {
                    jarFile.stream()
                            .filter(entry -> entry.getName().endsWith(".class"))
                            .forEach(entry -> {
                                String className = entry.getName()
                                        .replace('/', '.')
                                        .replace('\\', '.')
                                        .replace(".class", "");
                                try {
                                    Class<?> clazz = classLoader.loadClass(className);
                                    if (!Modifier.isAbstract(clazz.getModifiers()) && isLegacyModClass(clazz)) {
                                        System.out.println("[LegacyLoader] Loaded legacy mod class: " + className);
                                        try {
                                            initializeModClass(clazz);
                                            loadedClasses.add(clazz);
                                        } catch (Exception initException) {
                                            System.err.println("[LegacyLoader] Failed to initialize: " + className);
                                            initException.printStackTrace();
                                        }
                                    }
                                } catch (Throwable loadError) {
                                    // クラスロード失敗は静かに無視（ログ出すならここ）
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
                        System.out.println("[LegacyLoader] Extracted asset: " + relativePath);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("[LegacyLoader] Failed to extract assets: " + legacyJar.getName());
            e.printStackTrace();
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
            System.out.println("[LegacyLoader] Instantiated mod class: " + clazz.getName());

            // --- レガシーMODインターフェースを実装していたら登録 ---
            if (instance instanceof ILegacyMod legacyMod) {
                LegacyModManager.addMod(legacyMod);
                System.out.println("[LegacyLoader] Registered legacy mod: " + clazz.getName());
            }

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