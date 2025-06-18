import com.example.compatmod.legacy.loader.LegacyModJarLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Comparator;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import static org.junit.jupiter.api.Assertions.*;

public class LegacyModJarLoaderAssetTest {
    @Test
    public void testLegacyAssetsExtractedFromJar() throws Exception {
        Path gamedir = Files.createTempDirectory("gamedir");
        String originalDir = System.getProperty("user.dir");
        System.setProperty("user.dir", gamedir.toString());
        FMLPaths.loadAbsolutePaths(gamedir);

        Path modsDir = gamedir.resolve("mods");
        Files.createDirectories(modsDir);
        Path jarPath = modsDir.resolve("legacytest.jar");

        // Build temporary jar with assets/sample.txt
        try (JarOutputStream jos = new JarOutputStream(Files.newOutputStream(jarPath))) {
            JarEntry entry = new JarEntry("assets/testmod/sample.txt");
            jos.putNextEntry(entry);
            jos.write("hello".getBytes(StandardCharsets.UTF_8));
            jos.closeEntry();
        }

        try {
            LegacyModJarLoader loader = new LegacyModJarLoader(modsDir.toFile());
            loader.loadAllLegacyMods();

            Path extracted = gamedir.resolve("resources/assets/testmod/sample.txt");
            assertTrue(Files.exists(extracted), "Asset should be extracted to run/resources/assets");
            assertEquals("hello", Files.readString(extracted));
        } finally {
            System.setProperty("user.dir", originalDir);
            // Cleanup temporary directory
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
