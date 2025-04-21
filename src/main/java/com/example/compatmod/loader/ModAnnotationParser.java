package com.example.compatmod.loader;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class ModAnnotationParser {

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
                    e.printStackTrace();
                }
            }
        }

        return dependencies;
    }
}