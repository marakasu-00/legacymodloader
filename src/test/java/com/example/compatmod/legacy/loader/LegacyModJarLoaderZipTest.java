import com.example.compatmod.legacy.loader.LegacyModJarLoader;
import com.example.compatmod.legacyexample.ExampleLegacyModKeyTest;
import net.minecraftforge.fml.loading.FMLPaths;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.*;

public class LegacyModJarLoaderZipTest {
    @Test
    public void testZipModAssetsAndClassesLoaded() throws Exception {
        Path gamedir = Files.createTempDirectory("gamedir");
        String originalDir = System.getProperty("user.dir");
        System.setProperty("user.dir", gamedir.toString());
        FMLPaths.loadAbsolutePaths(gamedir);

        Path modsDir = gamedir.resolve("mods");
        Files.createDirectories(modsDir);
        Path zipPath = modsDir.resolve("legacytest.zip");

        Path classFile = Path.of(originalDir,
                "build/classes/java/test/com/example/compatmod/legacyexample/ExampleLegacyModKeyTest.class");

        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipPath))) {
            ZipEntry asset = new ZipEntry("assets/testmod/sample.txt");
            zos.putNextEntry(asset);
            zos.write("hello".getBytes(StandardCharsets.UTF_8));
            zos.closeEntry();

            ZipEntry clazz = new ZipEntry("com/example/compatmod/legacyexample/ExampleLegacyModKeyTest.class");
            zos.putNextEntry(clazz);
            zos.write(Files.readAllBytes(classFile));
            zos.closeEntry();
        }

        try {
            LegacyModJarLoader loader = new LegacyModJarLoader(modsDir.toFile());
            List<Class<?>> loaded = loader.loadAllLegacyMods();

            Path extracted = gamedir.resolve("legacy_assets/testmod/sample.txt");
            assertTrue(Files.exists(extracted), "Asset should be extracted from zip");
            assertEquals("hello", Files.readString(extracted));
            assertTrue(loaded.contains(ExampleLegacyModKeyTest.class), "Class from zip should be loaded");
        } finally {
            System.setProperty("user.dir", originalDir);
            Files.walk(gamedir)
                    .sorted(Comparator.reverseOrder())
                    .forEach(p -> {
                        try {
                            Files.deleteIfExists(p);
                        } catch (IOException ignored) {
                        }
                    });
        }
    }
}
