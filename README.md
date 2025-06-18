A mod to load and provide compatibility for 1.12.2 mods in 1.20.1.

in the making
現在制作中

Legacy mod jars are loaded from `run/mods/legacy` by default. This location can
be changed by modifying `LegacyModManager.legacyModsDir` before initialisation.
## Legacy Networking

Use `LegacyNetwork.createChannel("channel")` to obtain a channel that mimics the 1.12.2 SimpleNetworkWrapper.
Messages implement `LegacyPacket` and can be registered with `registerMessage`.


