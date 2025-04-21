package com.example.compatmod.loader;

import java.util.List;
import java.util.Map;

public class DependencyResolver {

    public static void resolveDependencies(Map<String, List<String>> modDependencies) {
        for (Map.Entry<String, List<String>> entry : modDependencies.entrySet()) {
            String modId = entry.getKey();
            List<String> dependencies = entry.getValue();

            for (String dependency : dependencies) {
                if (!modDependencies.containsKey(dependency)) {
                    throw new RuntimeException("Missing dependency: " + dependency + " for mod: " + modId);
                }
            }
        }
    }
}