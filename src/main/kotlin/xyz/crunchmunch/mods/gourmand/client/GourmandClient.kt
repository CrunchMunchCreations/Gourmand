package xyz.crunchmunch.mods.gourmand.client

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.loader.api.FabricLoader
import xyz.crunchmunch.mods.gourmand.api.IgnoredBlockUpdateRegistry
import xyz.crunchmunch.mods.gourmand.network.clientbound.IgnoredBlockUpdateListPacket
import xyz.crunchmunch.mods.gourmand.network.serverbound.ModListPacket

class GourmandClient : ClientModInitializer {
    override fun onInitializeClient() {
        ClientPlayConnectionEvents.JOIN.register { _, _, _ ->
            if (ClientPlayNetworking.canSend(ModListPacket.TYPE)) {
                ClientPlayNetworking.send(ModListPacket(
                    FabricLoader.getInstance().allMods
                        .filter {
                            !it.metadata.containsCustomValue("fabric-loom:generated") // Remove all JiJ'd libraries
                        }
                        .associate {
                            it.metadata.id to it.metadata.version.friendlyString
                        }
                ))
            }
        }

        ClientPlayNetworking.registerGlobalReceiver(IgnoredBlockUpdateListPacket.TYPE) { packet, ctx ->
            for (state in packet.states) {
                IgnoredBlockUpdateRegistry.registerState(state)
            }
        }
    }
}
