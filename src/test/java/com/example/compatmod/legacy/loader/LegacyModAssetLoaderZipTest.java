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
    public void testAssetsZipExtracted() throws Exception {
        Path gamedir = Files.createTempDirectory("gamedir");
        String originalDir = System.getProperty("user.dir");
        System.setProperty("user.dir", gamedir.toString());
        FMLPaths.loadAbsolutePaths(gamedir);


        Path modsDir = gamedir.resolve("mods").resolve("legacy");
        Files.createDirectories(modsDir);
        Path zipPath = modsDir.resolve("assets_pack.zip");

        // Build temporary zip with assets/testpack/sample.txt
        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipPath))) {
            ZipEntry entry = new ZipEntry("assets/testpack/sample.txt");
            zos.putNextEntry(entry);
            zos.write("hello".getBytes(StandardCharsets.UTF_8));
            zos.closeEntry();
        }

        try {
            LegacyModAssetLoader.loadLegacyAssets();


            Path extracted = gamedir.resolve("resources/assets/testpack/sample.txt");
            assertTrue(Files.exists(extracted), "Asset should be extracted to run/resources/assets");

            Path miscExtracted = gamedir.resolve("resources/legacy_misc/assets_pack/assets/testpack/sample.txt");
            assertTrue(Files.exists(miscExtracted), "Asset should also be duplicated under legacy_misc");
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
    public void testMiscDirectoriesZipExtracted() throws Exception {
        Path gamedir = Files.createTempDirectory("gamedir");
        String originalDir = System.getProperty("user.dir");
        System.setProperty("user.dir", gamedir.toString());
        FMLPaths.loadAbsolutePaths(gamedir);

        Path modsDir = gamedir.resolve("mods").resolve("legacy");
        Files.createDirectories(modsDir);
        Path zipPath = modsDir.resolve("misc_pack.zip");

        // Build temporary zip with textures/example.png
        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipPath))) {
            ZipEntry entry = new ZipEntry("textures/example.png");
            zos.putNextEntry(entry);
            zos.write("data".getBytes(StandardCharsets.UTF_8));
            zos.closeEntry();
        }

        try {
            LegacyModAssetLoader.loadLegacyAssets();

            Path miscFile = gamedir.resolve("resources/legacy_misc/misc_pack/textures/example.png");
            assertTrue(Files.exists(miscFile), "Texture should be extracted under legacy_misc");

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
