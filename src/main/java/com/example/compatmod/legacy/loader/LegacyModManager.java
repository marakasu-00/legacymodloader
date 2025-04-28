package com.example.compatmod.legacy.loader;

import com.example.compatmod.legacy.api.ILegacyMod;
import com.google.gson.*;
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

@Mod.EventBusSubscriber(modid = "legacymodloader", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LegacyModManager {

    private static final List<ILegacyMod> legacyMods = new ArrayList<>();

    public static void loadLegacyMods() {

        Path legacyModsDir = Paths.get("mods", "Legacy");

        if (!Files.exists(legacyModsDir)) {
            System.out.println("[LegacyModLoader] No legacy mod folder found.");
            return;
        }

        try (Stream<Path> modFiles = Files.list(legacyModsDir)) {
            modFiles.filter(p -> p.toString().endsWith(".jar")).forEach(jar -> {
                Optional<String> modid = extractModIdFromToml(jar);
                if (modid.isEmpty()) modid = extractModIdFromMcmod(jar);

                Optional<String> mainClassName = extractMainClassFromMcmod(jar);

                if (mainClassName.isPresent()) {
                    try {
                        URL jarUrl = jar.toUri().toURL();
                        // ClassLoaderの親を "Minecraft本体＋このModのクラスローダー" に合わせる
                        URLClassLoader classLoader = new URLClassLoader(new URL[]{jarUrl}, Thread.currentThread().getContextClassLoader());
                        Class<?> modClass = classLoader.loadClass(mainClassName.get());
                        Object modInstance = modClass.getDeclaredConstructor().newInstance();

                        if (modInstance instanceof ILegacyMod legacyMod) {
                            LegacyModManager.addMod(legacyMod); // ちゃんとリストに登録
                            legacyMod.onLoad(); // <- ここでonLoadを呼び出す！
                            System.out.println("[LegacyModLoader] Loaded legacy mod (ILegacyMod): " + mainClassName.get());
                        } else {
                            MinecraftForge.EVENT_BUS.register(modInstance); // 通常のMODクラスならForgeイベントバスへ登録
                            System.out.println("[LegacyModLoader] Loaded standard mod: " + mainClassName.get());
                        }

                    } catch (Exception e) {
                        System.err.println("[LegacyModLoader] Failed to load mod class: " + mainClassName.get());
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("[LegacyModLoader] No main class found for: " + jar.getFileName());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        // --- 仮で ExampleLegacyMod を手動登録 ---
        com.example.compatmod.legacy.ExampleLegacyMod exampleMod = new com.example.compatmod.legacy.ExampleLegacyMod();
        exampleMod.onLoad();
        addMod(exampleMod);
        MinecraftForge.EVENT_BUS.register(exampleMod);
        System.out.println("[LegacyModLoader] ExampleLegacyMod loaded manually for testing.");

    }

    private static Optional<String> findModAnnotatedClass(Path jarPath) {
        try (FileSystem fs = FileSystems.newFileSystem(jarPath, (ClassLoader) null)) {
            for (Path root : fs.getRootDirectories()) {
                final Optional<String>[] result = new Optional[]{Optional.empty()};

                Files.walk(root)
                        .filter(path -> path.toString().endsWith(".class"))
                        .forEach(path -> {
                            try (InputStream is = Files.newInputStream(path)) {
                                ClassReader reader = new ClassReader(is);
                                reader.accept(new ClassVisitor(Opcodes.ASM9) {
                                    @Override
                                    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                                        if ("Lnet/minecraftforge/fml/common/Mod;".equals(descriptor)) {
                                            String className = path.toString()
                                                    .replace("/", ".")
                                                    .replace(".class", "")
                                                    .replaceFirst("^/", ""); // 先頭の / を消す
                                            result[0] = Optional.of(className);
                                        }
                                        return super.visitAnnotation(descriptor, visible);
                                    }
                                }, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });

                if (result[0].isPresent()) return result[0];
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }


    private static Optional<String> extractModIdFromToml(Path jarPath) {
        try (FileSystem fs = FileSystems.newFileSystem(jarPath, (ClassLoader) null)) {
            Path modsToml = fs.getPath("META-INF", "mods.toml");
            if (Files.exists(modsToml)) {
                for (String line : Files.readAllLines(modsToml)) {
                    line = line.trim();
                    if (line.startsWith("modId")) {
                        return Optional.of(line.split("=")[1].trim().replace("\"", ""));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private static Optional<String> extractModIdFromMcmod(Path jarPath) {
        try (FileSystem fs = FileSystems.newFileSystem(jarPath, (ClassLoader) null)) {
            Path mcmodInfo = fs.getPath("mcmod.info");
            if (Files.exists(mcmodInfo)) {
                String json = Files.readString(mcmodInfo);
                JsonArray arr = JsonParser.parseString(json).getAsJsonArray();
                return Optional.of(arr.get(0).getAsJsonObject().get("modid").getAsString());
            }
        } catch (IOException | JsonParseException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private static Optional<String> extractMainClassFromMcmod(Path jarPath) {
        try (FileSystem fs = FileSystems.newFileSystem(jarPath, (ClassLoader) null)) {
            Path mcmodInfo = fs.getPath("mcmod.info");
            if (Files.exists(mcmodInfo)) {
                String json = Files.readString(mcmodInfo);
                JsonArray arr = JsonParser.parseString(json).getAsJsonArray();
                JsonObject obj = arr.get(0).getAsJsonObject();
                if (obj.has("modid") && obj.has("class")) {
                    return Optional.of(obj.get("class").getAsString());
                }
            }
        } catch (IOException | JsonParseException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
    public static List<ILegacyMod> getLegacyMods() {
        return legacyMods;
    }

    public static void addMod(ILegacyMod mod) {
        legacyMods.add(mod);
    }
    // --- Tick イベントにフックする ---
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            for (ILegacyMod mod : legacyMods) {
                mod.onClientTick();
            }
        }
    }
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            for (ILegacyMod mod : legacyMods) {
                mod.onServerTick();
            }
        }
    }
}

