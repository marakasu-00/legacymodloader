# LegacyModLoader

A mod to load and provide compatibility for 1.12.2 mods in Minecraft 1.20.1.

## Prerequisites

- **Java 17** or newer. The project uses the Gradle wrapper so you do not need a system Gradle installation.
- **Internet access** for Gradle to download dependencies on first build.

## Building

To compile the mod run:

```bash
./gradlew build
```

The resulting JAR can be found in `build/libs/`.

## Running and Using Legacy Mods

During development you can launch the game directly with:

```bash
./gradlew runClient
```

Legacy mods should be placed inside the `mods/Legacy` directory of the game folder. When using the Gradle `runClient` task this corresponds to `run/mods/Legacy`.
Any 1.12.2 mod JARs placed here will be loaded by the LegacyModLoader at startup.

