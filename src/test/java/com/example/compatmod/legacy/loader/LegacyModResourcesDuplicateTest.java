import com.example.compatmod.config.ConfigHandler;
import com.example.compatmod.legacy.loader.LegacyModResources;
import net.minecraftforge.fml.loading.FMLPaths;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class LegacyModResourcesDuplicateTest {
    @Test
    public void testHighPriorityDuplicateThrows() throws Exception {
        Path gamedir = Files.createTempDirectory("gamedir");
        String originalDir = System.getProperty("user.dir");
        System.setProperty("user.dir", gamedir.toString());
        FMLPaths.loadAbsolutePaths(gamedir);

        try {
            Path legacy = gamedir.resolve("legacy_assets/assets/testmod");
            Files.createDirectories(legacy);
            Files.writeString(legacy.resolve("dup.txt"), "a", StandardOpenOption.CREATE);

            Path existing = gamedir.resolve("resources/assets/testmod");
            Files.createDirectories(existing);
            Files.writeString(existing.resolve("dup.txt"), "b", StandardOpenOption.CREATE);

            Assertions.assertThrows(IllegalStateException.class,
                    () -> LegacyModResources.verifyNoDuplicateResources(gamedir.resolve("legacy_assets"), ConfigHandler.Priority.HIGH));
        } finally {
            System.setProperty("user.dir", originalDir);
        }
    }

    @Test
    public void testLowPriorityDuplicateWarns() throws Exception {
        Path gamedir = Files.createTempDirectory("gamedir");
        String originalDir = System.getProperty("user.dir");
        System.setProperty("user.dir", gamedir.toString());
        FMLPaths.loadAbsolutePaths(gamedir);

        try {
            Path legacy = gamedir.resolve("legacy_assets/assets/testmod");
            Files.createDirectories(legacy);
            Files.writeString(legacy.resolve("dup.txt"), "a", StandardOpenOption.CREATE);

            Path existing = gamedir.resolve("resources/assets/testmod");
            Files.createDirectories(existing);
            Files.writeString(existing.resolve("dup.txt"), "b", StandardOpenOption.CREATE);

            // Should not throw
            LegacyModResources.verifyNoDuplicateResources(gamedir.resolve("legacy_assets"), ConfigHandler.Priority.LOW);
        } finally {
            System.setProperty("user.dir", originalDir);
        }
    }
}
