package org.incendo.varmkorv

import com.google.inject.AbstractModule
import com.google.inject.Inject
import com.google.inject.Module
import com.google.inject.name.Named
import com.google.inject.name.Names
import kotlinx.coroutines.*
import kotlinx.coroutines.future.asDeferred
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitScheduler

class VarmkorvPlugin : JavaPlugin() {

    @Inject
    @Named("Message")
    private lateinit var message: String
    @Inject
    private lateinit var scheduler: BukkitScheduler

    companion object {
        /** Dispatcher that synchronizes to the primary thread */
        lateinit var DISPATCHER: CoroutineDispatcher
    }

    override fun getInjectionModules(): MutableCollection<Module> {
        return mutableListOf(object : AbstractModule() {
            override fun configure() {
                bind(String::class.java).annotatedWith(Names.named("Message")) to "Hello World!"
            }
        })
    }

    override fun onEnable() {
        DISPATCHER = dispatcher
        logger.info("I'd just like to say: $message")
        commandManager.command(
            commandManager.commandBuilder("message")
                .handler {
                    it.sender.sendMessage(message)
                }
        ).command(
            commandManager.commandBuilder("korvbröd")
                .senderType(Player::class.java)
                .handler {
                    GlobalScope.launch {
                        doStuff(it.sender as Player)
                    }
                }
        )
    }

    /** Demonstration of random synchronization stuff */
    private suspend fun doStuff(player: Player) {
        withContext(Dispatchers.Default) {
            /* Supply the chunk asynchronously and suspend until the result is available */
            scheduler.supplySync(this@VarmkorvPlugin) {
                player.teleport(Bukkit.getWorlds().get(1).spawnLocation)
            }.asDeferred().await().let {
                player.sendMessage("Teleported? ${if (it) "yes" else "no"}")
            }
            /* Run the block on the primary thread */
            withContext(dispatcher) {
                player.sendMessage("Hello dis is primary thread? ${if (Bukkit.isPrimaryThread()) "yes" else "no"}")
            }
        }
    }

}

/* Add a dispatcher to the Dispatcher class */
val Dispatchers.Minecraft
    get() = VarmkorvPlugin.DISPATCHER
