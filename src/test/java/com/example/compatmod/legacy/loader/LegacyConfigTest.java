import com.example.compatmod.legacy.loader.LegacyConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class LegacyConfigTest {
    @Test
    public void testHighPriorityConfig() throws Exception {
        Path gamedir = Files.createTempDirectory("gamedir");
        String originalDir = System.getProperty("user.dir");
        System.setProperty("user.dir", gamedir.toString());
        FMLPaths.loadAbsolutePaths(gamedir);

        Path configDir = FMLPaths.CONFIGDIR.get();
        Files.createDirectories(configDir);
        Path config = configDir.resolve("legacy_loader.toml");
        Files.writeString(config, "[resource_pack]\npriority = \"high\"\n");

        try {
            LegacyConfig.packPriority = LegacyConfig.PackPriority.LOW;
            LegacyConfig.load();
            assertEquals(LegacyConfig.PackPriority.HIGH, LegacyConfig.packPriority, "Should set high priority");
        } finally {
            System.setProperty("user.dir", originalDir);
            Files.walk(gamedir).sorted(Comparator.reverseOrder()).forEach(p -> {
                try { Files.deleteIfExists(p); } catch (IOException ignored) {}
            });
        }
    }

    @Test
    public void testLowPriorityConfig() throws Exception {
        Path gamedir = Files.createTempDirectory("gamedir");
        String originalDir = System.getProperty("user.dir");
        System.setProperty("user.dir", gamedir.toString());
        FMLPaths.loadAbsolutePaths(gamedir);

        Path configDir = FMLPaths.CONFIGDIR.get();
        Files.createDirectories(configDir);
        Path config = configDir.resolve("legacy_loader.toml");
        Files.writeString(config, "[resource_pack]\npriority = \"low\"\n");

        try {
            LegacyConfig.packPriority = LegacyConfig.PackPriority.HIGH;
            LegacyConfig.load();
            assertEquals(LegacyConfig.PackPriority.LOW, LegacyConfig.packPriority, "Should set low priority");
        } finally {
            System.setProperty("user.dir", originalDir);
            Files.walk(gamedir).sorted(Comparator.reverseOrder()).forEach(p -> {
                try { Files.deleteIfExists(p); } catch (IOException ignored) {}
            });
        }
    }

    @Test
    public void testMissingConfigUsesDefault() throws Exception {
        Path gamedir = Files.createTempDirectory("gamedir");
        String originalDir = System.getProperty("user.dir");
        System.setProperty("user.dir", gamedir.toString());
        FMLPaths.loadAbsolutePaths(gamedir);

        try {
            LegacyConfig.packPriority = LegacyConfig.PackPriority.LOW;
            LegacyConfig.load();
            assertEquals(LegacyConfig.PackPriority.LOW, LegacyConfig.packPriority, "Default priority when config missing");
        } finally {
            System.setProperty("user.dir", originalDir);
            Files.walk(gamedir).sorted(Comparator.reverseOrder()).forEach(p -> {
                try { Files.deleteIfExists(p); } catch (IOException ignored) {}
            });
        }
    }
}
