A mod to load and provide compatibility for 1.12.2 mods in 1.20.1.

in the making
現在制作中
<<<<<<< codex/create-helper-classes-for-packet-translation
## Legacy Networking

Use `LegacyNetwork.createChannel("channel")` to obtain a channel that mimics the 1.12.2 SimpleNetworkWrapper.
Messages implement `LegacyPacket` and can be registered with `registerMessage`.
=======

## Legacy networking helpers

The `legacy.network` package provides helper classes to translate old
1.12.2 packet handling to Forge's modern networking system. Legacy mods
can create a channel via `LegacyNetworkManager.createChannel(modId)` and
register packets implementing `LegacyPacket`. Packets can then be sent
to the server or specific players using the methods on
`LegacyNetworkChannel`.
>>>>>>> 20250612

