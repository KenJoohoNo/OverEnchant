package kenjoohono.overEnchant.Events

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.plugin.java.JavaPlugin

class OverTicketCloseListener(private val plugin: JavaPlugin) : Listener {
    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        val player = event.player as? Player ?: return
        val cursorItem = player.itemOnCursor
        if (cursorItem != null) {
            val meta = cursorItem.itemMeta
            val expectedName = ChatColor.translateAlternateColorCodes('&', "&6최초 도감 코인")
            if (cursorItem.type == Material.GLOW_INK_SAC && meta?.displayName == expectedName) {
                player.setItemOnCursor(null)
                player.updateInventory()
            }
        }
    }
}