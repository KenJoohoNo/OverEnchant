package kenjoohono.overEnchant.Events

import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.inventory.ClickType
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin

class AntiAnvil(private val plugin: JavaPlugin) : Listener {
    private val overEnchantKey = NamespacedKey(plugin, "overenchant_attempts")

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (event.view.topInventory.type != InventoryType.ANVIL) return

        if (event.click == ClickType.NUMBER_KEY || event.click == ClickType.NUMBER_KEY) {
            event.isCancelled = true
            return
        }

        val clickedItem = event.currentItem ?: return
        val meta = clickedItem.itemMeta ?: return

        val attempts = meta.persistentDataContainer.get(overEnchantKey, PersistentDataType.INTEGER) ?: 0
        if (attempts >= 1) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onInventoryDrag(event: InventoryDragEvent) {
        if (event.view.topInventory.type != InventoryType.ANVIL) return

        for ((_, item) in event.newItems) {
            if (item != null) {
                val meta = item.itemMeta ?: continue
                val attempts = meta.persistentDataContainer.get(overEnchantKey, PersistentDataType.INTEGER) ?: 0
                if (attempts >= 1) {
                    event.isCancelled = true
                    return
                }
            }
        }
    }
}