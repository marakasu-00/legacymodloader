package com.example.compatmod.loader;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class ModAnnotationParser {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static List<String> parseDependencies(Class<?> modClass) {
        List<String> dependencies = new ArrayList<>();

        for (Annotation annotation : modClass.getAnnotations()) {
            if (annotation.annotationType().getName().equals("net.minecraftforge.fml.common.Mod")) {
                try {
                    Object dependenciesField = annotation.annotationType().getMethod("dependencies").invoke(annotation);
                    if (dependenciesField instanceof String) {
                        String[] dependencyArray = ((String) dependenciesField).split(";");
                        for (String dependency : dependencyArray) {
                            dependencies.add(dependency.trim());
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("[LegacyLoader] Failed to parse mod dependencies", e);
                }
            }
        }

        return dependencies;
    }
}
