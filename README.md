A mod to load and provide compatibility for 1.12.2 mods in 1.20.1.

in the making
現在制作中

## Legacy networking helpers

The `legacy.network` package provides helper classes to translate old
1.12.2 packet handling to Forge's modern networking system. Legacy mods
can create a channel via `LegacyNetworkManager.createChannel(modId)` and
register packets implementing `LegacyPacket`. Packets can then be sent
to the server or specific players using the methods on
`LegacyNetworkChannel`.
