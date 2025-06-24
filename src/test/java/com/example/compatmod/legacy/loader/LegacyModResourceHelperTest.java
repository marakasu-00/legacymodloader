import net.minecraftforge.fml.loading.FMLPaths;
import org.junit.jupiter.api.Test;
import com.example.compatmod.legacy.loader.LegacyModResourceHelper;

import java.nio.file.Files;
import java.nio.file.Path;

import java.nio.file.StandardOpenOption;

import static org.junit.jupiter.api.Assertions.*;

public class LegacyModResourceHelperTest {
    @Test
    public void testAssetsCopied() throws Exception {
        Path gamedir = Files.createTempDirectory("gamedir");
        String originalDir = System.getProperty("user.dir");
        System.setProperty("user.dir", gamedir.toString());
        FMLPaths.loadAbsolutePaths(gamedir);

        Path sourceDir = FMLPaths.GAMEDIR.get().resolve("resources").resolve("assets");
        Files.createDirectories(sourceDir);
        Path testFile = sourceDir.resolve("dummy.txt");
        Files.writeString(testFile, "hello", StandardOpenOption.CREATE, StandardOpenOption.WRITE);

        try {
            LegacyModResourceHelper.loadLegacyResources();

            Path targetFile = gamedir.resolve("legacy_assets/dummy.txt");
            assertTrue(Files.exists(targetFile), "Asset should be copied to legacy_assets");
            assertEquals("hello", Files.readString(targetFile));
        } finally {
            System.setProperty("user.dir", originalDir);
        }
    }
}
