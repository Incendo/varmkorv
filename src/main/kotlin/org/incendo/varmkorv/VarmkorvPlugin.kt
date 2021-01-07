package org.incendo.varmkorv

import com.google.inject.AbstractModule
import com.google.inject.Inject
import com.google.inject.Module
import com.google.inject.name.Named
import com.google.inject.name.Names
import org.bukkit.plugin.java.JavaPlugin

class VarmkorvPlugin : JavaPlugin() {

    @Inject
    private lateinit var message: String

    override fun onEnable() {
        logger.info("I'd just like to say: $message")
    }

    override fun getInjectionModules(): MutableCollection<Module> {
        return mutableListOf(object : AbstractModule() {
            override fun configure() {
                bind(String::class.java).annotatedWith(Names.named("Message")) to "Hello World!"
            }
        })
    }

}
