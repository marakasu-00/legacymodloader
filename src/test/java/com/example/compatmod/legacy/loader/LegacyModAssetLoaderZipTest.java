import com.example.compatmod.legacy.loader.LegacyModAssetLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Comparator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.*;

public class LegacyModAssetLoaderZipTest {
    @Test
    public void testZipArchiveSupport() throws Exception {
        Path gamedir = Files.createTempDirectory("gamedir");
        String originalDir = System.getProperty("user.dir");
        System.setProperty("user.dir", gamedir.toString());
        FMLPaths.loadAbsolutePaths(gamedir);

        Path modsLegacy = gamedir.resolve("mods").resolve("legacy");
        Files.createDirectories(modsLegacy);
        Path zipPath = modsLegacy.resolve("legacytest.zip");

        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipPath))) {
            ZipEntry entry = new ZipEntry("assets/testmod/sample.txt");
            zos.putNextEntry(entry);
            zos.write("hello".getBytes(StandardCharsets.UTF_8));
            zos.closeEntry();
        }

        try {
            LegacyModAssetLoader.loadLegacyAssets();

            Path extracted = gamedir.resolve("resources/assets/assets/testmod/sample.txt");

            assertTrue(Files.exists(extracted), "Asset should be extracted from zip");
            assertEquals("hello", Files.readString(extracted));
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

    @Test
    public void testMiscZipExtraction() throws Exception {
        Path gamedir = Files.createTempDirectory("gamedir");
        String originalDir = System.getProperty("user.dir");
        System.setProperty("user.dir", gamedir.toString());
        FMLPaths.loadAbsolutePaths(gamedir);

        Path modsLegacy = gamedir.resolve("mods").resolve("legacy");
        Files.createDirectories(modsLegacy);
        Path zipPath = modsLegacy.resolve("misc_pack.zip");

        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipPath))) {
            ZipEntry entry = new ZipEntry("textures/thing/test.txt");
            zos.putNextEntry(entry);
            zos.write("data".getBytes(StandardCharsets.UTF_8));
            zos.closeEntry();
        }

        try {
            LegacyModAssetLoader.loadLegacyAssets();
            Path extracted = gamedir.resolve("resources/legacy_misc/misc_pack/textures/thing/test.txt");
            assertTrue(Files.exists(extracted), "Legacy resource should be copied to misc directory");
            assertEquals("data", Files.readString(extracted));
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
