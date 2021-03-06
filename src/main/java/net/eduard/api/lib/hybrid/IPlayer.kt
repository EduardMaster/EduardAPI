package net.eduard.api.lib.hybrid

import net.md_5.bungee.api.chat.TextComponent
import java.util.*

interface IPlayer<T : Any> : ISender {
    val uuid : UUID
    val instance : T?
    val isOnline : Boolean
    val uniqueId get() = uuid
    fun search() : T?
    fun connect(serverName: String)
    val offline get() = PlayerUser(name, uuid)
    fun sendMessage(message : TextComponent)
    fun chat(message : String)


}