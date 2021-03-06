package net.eduard.api.server

import net.eduard.api.lib.modules.FakePlayer
import org.bukkit.inventory.ItemStack

interface DropSystem {

    fun addDrop(player : FakePlayer, item : ItemStack , amount : Double)
    fun getDropAmount(player : FakePlayer, item : ItemStack) : Double
    fun getDrops(player : FakePlayer) : Map<ItemStack, Double>

}