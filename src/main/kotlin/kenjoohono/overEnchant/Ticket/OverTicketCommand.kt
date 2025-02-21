package kenjoohono.overEnchant.Ticket

import org.bukkit.Material
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class OverTicketCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) return true

        if (!sender.isOp) return true

        val bookItem = ItemStack(Material.GLOW_INK_SAC)
        val meta = bookItem.itemMeta
        if (meta != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6최초 도감 코인"))
            bookItem.itemMeta = meta
        }

        sender.inventory.addItem(bookItem)
        return true
    }
}