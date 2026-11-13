package xyz.crunchmunch.mods.gourmand.client

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelExtractionEvents
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.gizmos.GizmoStyle
import net.minecraft.gizmos.Gizmos
import net.minecraft.gizmos.TextGizmo
import net.minecraft.world.phys.Vec3
import xyz.crunchmunch.mods.gourmand.api.GourmandLevelAttachments
import xyz.crunchmunch.mods.gourmand.api.IgnoredBlockUpdateRegistry
import xyz.crunchmunch.mods.gourmand.network.clientbound.IgnoredBlockUpdateListPacket
import xyz.crunchmunch.mods.gourmand.network.serverbound.ModListPacket
import java.awt.Color

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

        LevelExtractionEvents.END_EXTRACTION.register { context ->
            val debugPaths = context.level().getAttached(GourmandLevelAttachments.DEBUG_PATHS) ?: emptyMap()

            for ((id, path) in debugPaths) {
                var wasInit = false
                lateinit var start: Vec3

                for (point in path.points) {
                    if (!wasInit) {
                        start = point
                        wasInit = true
                        Gizmos.billboardText(id.toString(), point, TextGizmo.Style.whiteAndCentered())
                        Gizmos.circle(point, 2.5f, GizmoStyle.fill(Color.GREEN.rgb))
                        continue
                    }

                    Gizmos.circle(point, 2f, GizmoStyle.fill(path.color))
                    Gizmos.line(start, point, path.color)
                    start = point
                }

                if (path.points.isNotEmpty()) {
                    Gizmos.circle(path.points.last(), 2.5f, GizmoStyle.fill(Color.RED.rgb))
                }
            }

            val debugAreas = context.level().getAttached(GourmandLevelAttachments.DEBUG_AREAS) ?: emptyMap()

            for ((id, area) in debugAreas) {
                Gizmos.cuboid(area.area, GizmoStyle.stroke(area.color, 8f))
                Gizmos.billboardText("$id", area.area.minPosition, TextGizmo.Style.whiteAndCentered())
            }
        }
    }
}
