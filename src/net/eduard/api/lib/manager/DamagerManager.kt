package net.eduard.api.lib.manager

import net.eduard.api.lib.kotlin.register
import org.bukkit.entity.Entity
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

/**
 * Sistema de verificar quem deu o ultimo Hit em você
 * @author Eduard
 */
class DamagerManager : Listener {
    companion object {
        fun getLastDamager(entity: Entity): Entity? {
            val damager = lastPvP[entity]
            if (damager != null) {
                if (damager is Projectile) {
                    val projectile = damager
                    if (projectile.shooter != null && projectile is Entity) {
                        return projectile.shooter as Entity
                    }
                }
            }
            return damager
        }

        private val lastPvP: MutableMap<Entity, Entity> = HashMap()


        init {
            DamagerManager().register()

        }
    }

    @EventHandler
    fun aoIrPvP(e: EntityDamageByEntityEvent) {
        lastPvP[e.entity] = e.damager
    }
}