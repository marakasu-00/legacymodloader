package com.example.compatmod.loader;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import static org.junit.jupiter.api.Assertions.*;

public class McmodInfoParserTest {
    @Test
    public void parseDependenciesFromJar() throws Exception {
        Path jar = Files.createTempFile("testmod", ".jar");
        String mcmod = "[{\"modid\":\"test\",\"name\":\"Test\",\"version\":\"1.0\",\"dependencies\":\"required-after:forge@[14.23,);\"}]";

        try (JarOutputStream jos = new JarOutputStream(Files.newOutputStream(jar))) {
            JarEntry entry = new JarEntry("mcmod.info");
            jos.putNextEntry(entry);
            jos.write(mcmod.getBytes(StandardCharsets.UTF_8));
            jos.closeEntry();
        }

        try {
            List<String> deps = McmodInfoParser.parseDependencies(jar);
            assertEquals(1, deps.size());
            assertEquals("required-after:forge@[14.23,)", deps.get(0));
        } finally {
            Files.deleteIfExists(jar);
        }
    }
}
