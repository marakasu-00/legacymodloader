package com.example.compatmod.loader;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class McmodInfoParser {

    public static List<String> parseDependencies(Path modJarPath) throws IOException {
        List<String> dependencies = new ArrayList<>();
        Path mcmodInfoPath = modJarPath.resolve("mcmod.info");

        if (Files.exists(mcmodInfoPath)) {
            String content = Files.readString(mcmodInfoPath);
            JsonArray modInfoArray = JsonParser.parseString(content).getAsJsonArray();

            for (var element : modInfoArray) {
                JsonObject modInfo = element.getAsJsonObject();
                if (modInfo.has("dependencies")) {
                    String dependencyString = modInfo.get("dependencies").getAsString();
                    String[] dependencyArray = dependencyString.split(";");
                    for (String dependency : dependencyArray) {
                        dependencies.add(dependency.trim());
                    }
                }
            }
        }

        return dependencies;
    }
}