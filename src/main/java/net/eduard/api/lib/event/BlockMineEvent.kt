package net.eduard.api.lib.event

import net.eduard.api.lib.abstraction.Blocks
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.inventory.ItemStack

class BlockMineEvent(
    val drops: MutableMap<ItemStack, Double>,
    var block: Block,
    var player: Player,
    var useEnchants : Boolean,
    var expToDrop: Int = 1
) : Event(), Cancellable {
    var needGiveDrops = true
    var needGiveExp = true
    var needApplyFortune = true
    private var cancel = false

    override fun isCancelled(): Boolean {
        return cancel
    }

    override fun setCancelled(toggle: Boolean) {
        cancel = toggle
    }

    fun breakBlock(){
        val blockInfo = Blocks.get(block.location)
        if (blockInfo != null)
            blockInfo.setType(Material.AIR)
        else block.type = Material.AIR
    }

    fun useDefaultDrops() {
        for (dropItem in block.getDrops(player.itemInHand)) {
            drops[dropItem] = dropItem.amount.toDouble()
        }
    }

    fun giveDrops() {
        for (item in drops.keys) {
            player.inventory.addItem(item)
        }
    }

    fun dropInWorld() {
        for (item in drops.keys) {
            player.world.dropItemNaturally(block.location, item)
        }
    }

    fun applyFortune() {
        val item = player.itemInHand ?: return
        val level = item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) ?: return
        for ((loopItem, amount) in drops) {
            drops[loopItem] = amount + (amount * level)
        }
    }

    init {
        if (drops.isEmpty()) {
            useDefaultDrops()
        }
    }

    override fun getHandlers(): HandlerList {
        return handlerList
    }

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }

}