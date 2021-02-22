package net.eduard.api.lib.abstraction
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack


/**
 * Representa uma Api como a do Bukkit porem com muito menos métodos
 *
 * @author Eduard
 */
abstract class Minecraft {
    /**
     * Envia um Pacote de dados para o jogador
     *
     * @param packet Pacote
     * @param player Jogador
     */
    abstract fun sendPacket(packet: Any, player: Player)

    /**
     * Envia um Pacote de dados para o jogador
     *
     * @param player Jogador
     * @param packet Pacote
     */
    fun sendPacket(player: Player, packet: Any) {
        sendPacket(packet, player)
    }

   abstract fun sendActionBar(player: Player, message: String)


    abstract fun sendParticle(
        player: Player,
        name: String,
        location: Location,
        amount: Int,
        xOffset: Float = 0f,
        yOffset: Float = 0f,
        zOffset: Float = 0f,
        speed: Float = 1f
    )
    abstract fun getPlayers() : Collection<Player>

    fun sendPacketsToAll(vararg packets: Any) {
        for (player in getPlayers()) {
            for (packet in packets) {
                sendPacket(packet, player)
            }
        }
    }

    fun sendPacketsToOthers(player: Player, vararg packets: Any) {
        for (playerLoop in getPlayers()) {
            if (playerLoop == player) continue
            if (playerLoop.canSee(player)) {
                for (packet in packets) {
                    sendPacket(packet, player)
                }
            }
        }
    }

    abstract fun setHeadSkin(head: ItemStack, texture: String, signature: String)
    abstract fun performRespawn(player: Player)
    abstract fun setPlayerSkin(player: Player, newSkin: String)
    abstract fun setPlayerName(player: Player, newName: String)
    abstract fun respawnPlayer(playerToRespawn: Player)
    abstract fun reloadPlayer(player: Player)
    abstract fun setTabList(player: Player, header: String, footer: String)
    abstract fun removeFromTab(playerRemoved: Player)
    abstract fun addToTab(playerToAdd: Player)
    abstract fun updateDisplayName(playerToAdd: Player)
    abstract fun getItemNBT(item: ItemStack): Any
    abstract fun setItemNBT(item: ItemStack, nbt: Any): ItemStack
    abstract fun disableAI(entity: Entity)

    companion object {
        fun getVersion() = "1_8_9_R0"
        /**
         * Inicia automaticamente uma istancia da versão atual do servidor
         *
         * @return Instancia feita com Reflection
         */
        lateinit var instance: Minecraft

        init{
            instance = Class.forName("net.eduard.api.lib.abstraction.Minecraft_" + getVersion())
                .newInstance() as Minecraft
        }

    }
}