package kenjoohono.overEnchant.Events

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import kotlin.math.round
import kotlin.random.Random

class OverTicketUse(private val plugin: JavaPlugin) : Listener {
    private val overEnchantKey = NamespacedKey(plugin, "overenchant_attempts")

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return

        val cursorItem = event.cursor ?: return
        val cursorMeta = cursorItem.itemMeta ?: return
        val expectedTicketName = ChatColor.translateAlternateColorCodes('&', "&6최초 도감 코인")
        if (cursorItem.type != Material.GLOW_INK_SAC || cursorMeta.displayName != expectedTicketName) return

        val clickedItem = event.currentItem ?: return
        if (clickedItem.type == Material.AIR) return
        if (clickedItem.enchantments.isEmpty()) return

        val totalEnchantCount = clickedItem.enchantments.size
        if (totalEnchantCount < 3 || totalEnchantCount > 10) {
            player.sendMessage(ChatColor.RED.toString() + "이 장비는 오버 인챈트가 불가능합니다.")
            return
        }

        val itemMeta = clickedItem.itemMeta ?: return
        val pdc = itemMeta.persistentDataContainer
        val attempts = pdc.get(overEnchantKey, PersistentDataType.INTEGER) ?: 0
        if (attempts >= 3) {
            player.sendMessage(ChatColor.RED.toString() + "이 장비는 더 이상 오버 인챈트 할 수 없습니다.")
            return
        }

        val n = round(totalEnchantCount / 3.0).toInt().coerceAtLeast(1)
        val enchantList = clickedItem.enchantments.keys.toList().shuffled(Random(System.currentTimeMillis())).take(n)

        val upgradeMessages = mutableListOf<String>()
        enchantList.forEach { enchant ->
            val currentLevel = itemMeta.enchants[enchant] ?: 0
            val newLevel = currentLevel + 1
            itemMeta.removeEnchant(enchant)
            itemMeta.addEnchant(enchant, newLevel, true)
            upgradeMessages.add("- " + ChatColor.AQUA + enchant.name + " " + ChatColor.GRAY + "$currentLevel -> " + ChatColor.AQUA + "$newLevel")
        }

        pdc.set(overEnchantKey, PersistentDataType.INTEGER, attempts + 1)
        val loreList = itemMeta.lore?.toMutableList() ?: mutableListOf()
        val lorePrefix = ChatColor.GRAY.toString() + "오버인챈트 : "
        val newLoreLine = lorePrefix + ChatColor.GOLD.toString() + "${attempts + 1}/3"
        val index = loreList.indexOfFirst { it.startsWith(lorePrefix) }
        if (index != -1) {
            loreList[index] = newLoreLine
        } else {
            loreList.add(newLoreLine)
        }
        itemMeta.lore = loreList
        clickedItem.setItemMeta(itemMeta)

        val upgradeMessage = ChatColor.GRAY.toString() + "오버 인챈트 :\n" + upgradeMessages.joinToString("\n")
        player.sendMessage(upgradeMessage)

        event.clickedInventory?.setItem(event.slot, clickedItem)

        val newTicketAmount = cursorItem.amount - 1
        if (newTicketAmount > 0) {
            cursorItem.amount = newTicketAmount
            player.setItemOnCursor(cursorItem)
        } else {
            player.setItemOnCursor(null)
        }
        player.updateInventory()
        event.isCancelled = true
    }
}