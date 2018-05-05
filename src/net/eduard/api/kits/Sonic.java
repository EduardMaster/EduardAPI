package net.eduard.api.kits;

import java.util.ArrayList;

import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.eduard.api.setup.Mine;
import net.eduard.api.setup.click.PlayerClick;
import net.eduard.api.setup.click.PlayerClickEffect;
import net.eduard.api.setup.game.Ability;
import net.eduard.api.setup.game.Effects;
import net.eduard.api.setup.game.Jump;
import net.eduard.api.setup.game.Sounds;

public class Sonic extends Ability {
	public static ArrayList<Player> inEffect = new ArrayList<>();
	public int effectSeconds = 5;
	public int range = 5;
	public Sonic() {
		setIcon(Material.LAPIS_BLOCK, "�fGanha um boost para frente");
		add(Material.LAPIS_BLOCK);
		display( new Effects(Effect.SMOKE, 10));
		jump(new Jump(true, 0.5, 2,
				Sounds.create("CLICK")));
		getPotions().add(new PotionEffect(PotionEffectType.POISON, 1, 20 * 5));
		setClick(new PlayerClick(Material.LAPIS_BLOCK, new PlayerClickEffect() {
			
			@Override
			public void onClick(Player player, Block block, ItemStack item) {
				// TODO Auto-generated method stub
				if (hasKit(player)) {
					if (cooldown(player)) {
						Mine.saveItems(player);
						Mine.setEquip(player, Color.BLUE, "�b" + getName());
						inEffect.add(player);
						Mine.TIME.delay(effectSeconds,new Runnable() {

							@Override
							public void run() {
								if (hasKit(player)) {
									Mine.getArmours(player);
								}
								inEffect.remove(player);
							}
						});
						jump(player);
					}
				}
			}
		}));
		setTime(40);
	}

	@EventHandler
	public void event(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (inEffect.contains(p)) {
			for (Entity entity : p.getNearbyEntities(range, range, range)) {
				if (entity instanceof LivingEntity) {
					LivingEntity livingEntity = (LivingEntity) entity;
					givePotions(livingEntity);
					getDisplay().create(livingEntity.getLocation());

				}
			}
		}

	}
}
