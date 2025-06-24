A mod to load and provide compatibility for 1.12.2 mods in 1.20.1.

in the making
現在制作中

Legacy mods are loaded from `run/mods/legacy` by default. Files placed here may
be either `.jar` or `.zip` archives. The location can be changed by modifying
`LegacyModManager.legacyModsDir` before initialisation.

When a legacy mod is processed, any resources under its `assets/` directory are
extracted to `run/resources/assets/`. Other legacy data is extracted to
`run/resources/legacy_misc/`. Existing files in these destinations are
overwritten when duplicates are encountered.
## Legacy Networking

Use `LegacyNetwork.createChannel("channel")` to obtain a channel that mimics the 1.12.2 SimpleNetworkWrapper.
Messages implement `LegacyPacket` and can be registered with `registerMessage`.


