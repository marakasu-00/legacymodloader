package com.example.compatmod.loader;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DependencyResolverTest {
    @Test
    public void resolveValidDependencies() {
        Map<String, List<String>> deps = new HashMap<>();
        deps.put("modA", List.of("modB"));
        deps.put("modB", List.of());

        // should not throw
        DependencyResolver.resolveDependencies(deps);
    }

    @Test
    public void resolveMissingDependency() {
        Map<String, List<String>> deps = new HashMap<>();
        deps.put("modA", List.of("missing"));

        assertThrows(RuntimeException.class, () -> DependencyResolver.resolveDependencies(deps));
    }
}
