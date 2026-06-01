package xyz.crunchmunch.mods.gourmand

import com.mojang.brigadier.Message
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import de.phyrone.brig.wrapper.literal
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.player.AttackEntityCallback
import net.fabricmc.fabric.api.event.player.UseEntityCallback
import net.fabricmc.fabric.api.event.registry.DynamicRegistries
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.commands.arguments.ResourceKeyArgument
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.InteractionResult
import xyz.crunchmunch.mods.gourmand.api.GourmandAttachments
import xyz.crunchmunch.mods.gourmand.api.GourmandRegistries
import xyz.crunchmunch.mods.gourmand.api.GourmandRegistryKeys
import xyz.crunchmunch.mods.gourmand.api.IgnoredBlockUpdateRegistry
import xyz.crunchmunch.mods.gourmand.api.behavior.BehaviorTriggers
import xyz.crunchmunch.mods.gourmand.api.behavior.TriggerableEntityBehavior
import xyz.crunchmunch.mods.gourmand.network.clientbound.IgnoredBlockUpdateListPacket
import xyz.crunchmunch.mods.gourmand.network.serverbound.ModListPacket

class Gourmand : ModInitializer {
    override fun onInitialize() {
        GourmandRegistries.init()
        GourmandAttachments.init()

        // Dynamic registries
        DynamicRegistries.registerSynced(GourmandRegistryKeys.BEHAVIOR, TriggerableEntityBehavior.CODEC)

        // Network packets
        PayloadTypeRegistry.serverboundPlay().registerLarge(ModListPacket.TYPE, ModListPacket.CODEC, -1)
        PayloadTypeRegistry.clientboundPlay().register(IgnoredBlockUpdateListPacket.TYPE, IgnoredBlockUpdateListPacket.CODEC)

        ServerPlayNetworking.registerGlobalReceiver(ModListPacket.TYPE) { packet, ctx ->
            ctx.player().setAttached(GourmandAttachments.MOD_IDS, packet.modIdsToVersions)
        }

        ServerPlayConnectionEvents.JOIN.register { handler, _, _ ->
            if (ServerPlayNetworking.canSend(handler, IgnoredBlockUpdateListPacket.TYPE)) {
                ServerPlayNetworking.send(handler.player, IgnoredBlockUpdateListPacket(IgnoredBlockUpdateRegistry.states))
            }
        }

        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            dispatcher.literal("gourmand") {
                require { Commands.LEVEL_GAMEMASTERS.check(permissions()) }

                literal("behavior") {
                    literal("add") {
                        argument("entity", EntityArgument.entities()) {
                            argument("behavior", ResourceKeyArgument.key(GourmandRegistryKeys.BEHAVIOR)) {
                                executes { ctx ->
                                    val entities = EntityArgument.getEntities(ctx, "entity")
                                    val behaviorId = ResourceKeyArgument.getRegistryKey(ctx, "behavior", GourmandRegistryKeys.BEHAVIOR,
                                        DynamicCommandExceptionType {
                                            Message {
                                                "Unknown behavior ID $it"
                                            }
                                        })

                                    var count = 0
                                    for (entity in entities) {
                                        val list = entity.getAttachedOrElse(GourmandAttachments.BEHAVIORS, listOf()).toMutableList()
                                        if (!list.contains(behaviorId)) {
                                            list.add(behaviorId)
                                            count++

                                            entity.setAttached(GourmandAttachments.BEHAVIORS, list)
                                        }
                                    }

                                    sendSuccess({ Component.literal("Added behavior ${behaviorId.identifier()} to $count entities.") }, true)

                                    count
                                }
                            }
                        }
                    }

                    literal("remove") {
                        argument("entity", EntityArgument.entities()) {
                            argument("behavior_id", ResourceKeyArgument.key(GourmandRegistryKeys.BEHAVIOR)) {
                                executes { ctx ->
                                    val entities = EntityArgument.getEntities(ctx, "entity")
                                    val behaviorId = ResourceKeyArgument.getRegistryKey(ctx, "behavior", GourmandRegistryKeys.BEHAVIOR,
                                        DynamicCommandExceptionType {
                                            Message {
                                                "Unknown behavior ID $it"
                                            }
                                        })

                                    var count = 0
                                    for (entity in entities) {
                                        val list = entity.getAttachedOrElse(GourmandAttachments.BEHAVIORS, listOf()).toMutableList()
                                        if (list.contains(behaviorId)) {
                                            list.remove(behaviorId)
                                            count++

                                            entity.setAttached(GourmandAttachments.BEHAVIORS, list)
                                        }
                                    }

                                    sendSuccess({ Component.literal("Removed behavior ${behaviorId.identifier()} from $count entities.") }, true)

                                    count
                                }
                            }
                        }
                    }

                    literal("list") {
                        argument("entity", EntityArgument.entity()) {
                            executes { ctx ->
                                val entity = EntityArgument.getEntity(ctx, "entity")
                                val behaviors = entity.getAttachedOrElse(GourmandAttachments.BEHAVIORS, emptyList())

                                if (behaviors.isNotEmpty()) {
                                    ctx.source.sendSuccess({ Component.literal("Entity ").append(entity.displayName).append(" has ${behaviors.size} behaviors:") }, false)
                                } else {
                                    ctx.source.sendSuccess({ Component.literal("Entity ").append(entity.displayName).append(" has no behaviors.") }, false)
                                }

                                for (key in behaviors) {
                                    ctx.source.sendSuccess({ Component.literal("- ${key.identifier()}") }, false)
                                }

                                behaviors.size
                            }
                        }
                    }
                }

                literal("mods") {
                    argument("player", EntityArgument.player()) {
                        executes { ctx ->
                            val player = EntityArgument.getPlayer(ctx, "player")

                            if (!player.hasAttached(GourmandAttachments.MOD_IDS)) {
                                sendFailure(Component.empty().append(player.displayName).append(" does not have Gourmand installed!"))
                                return@executes 0
                            }

                            val mods = player.getAttachedOrThrow(GourmandAttachments.MOD_IDS)
                            sendSystemMessage(Component.empty().append(player.displayName).append(" has ${mods.size} mods installed:"))
                            for ((modId, version) in mods) {
                                sendSystemMessage(Component.literal(" - $modId v$version"))
                            }

                            1
                        }
                    }
                }
            }
        }

        AttackEntityCallback.EVENT.register { player, _, _, entity, _ ->
            BehaviorTriggers.triggerBehaviors(entity, player, BehaviorTriggers.ATTACK)
            InteractionResult.PASS
        }

        UseEntityCallback.EVENT.register { player, _, _, entity, _ ->
            BehaviorTriggers.triggerBehaviors(entity, player, BehaviorTriggers.INTERACT)
            InteractionResult.PASS
        }
    }

    companion object {
        const val MOD_ID = "gourmand"

        @JvmStatic
        fun id(path: String): Identifier = Identifier.fromNamespaceAndPath(MOD_ID, path)
    }
}
