package kenjoohono.overEnchant

import org.bukkit.plugin.java.JavaPlugin
import kenjoohono.overEnchant.Ticket.OverTicketCommand
import kenjoohono.overEnchant.Events.OverTicketUse
import kenjoohono.overEnchant.Events.OverTicketCloseListener
import kenjoohono.overEnchant.Events.AntiAnvil

class OverEnchant : JavaPlugin() {

    override fun onEnable() {
        this.getCommand("overticket")?.setExecutor(OverTicketCommand())
        server.pluginManager.registerEvents(OverTicketUse(this), this)
        server.pluginManager.registerEvents(OverTicketCloseListener(this), this)
        server.pluginManager.registerEvents(AntiAnvil(this), this)
    }

    override fun onDisable() {
    }
}
