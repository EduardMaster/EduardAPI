package net.eduard.api.kits;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.game.Ability;
import net.eduard.api.game.PlayerClick;
import net.eduard.api.game.PlayerClickEffect;
import net.eduard.api.setup.GameAPI;

public class Flash extends Ability {

	public int distance = 45;

	public Flash() {
		setIcon(Material.REDSTONE_TORCH_ON, "�fTeleporte para Longe");
		add(Material.REDSTONE_TORCH_ON);
		setTime(40);
		setClick(new PlayerClick(Material.REDSTONE_TORCH_ON, new PlayerClickEffect() {
			
			@Override
			public void onClick(Player player, Block block, ItemStack item) {
				// TODO Auto-generated method stub
				if (hasKit(player)) {
					if (cooldown(player)) {
						GameAPI.teleport(player, distance);
					}
				}
			}
		}));
	}
}
