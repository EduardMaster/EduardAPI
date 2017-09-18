
package net.eduard.api.command.admin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import net.eduard.api.API;
import net.eduard.api.game.Jump;
import net.eduard.api.game.PlayerClick;
import net.eduard.api.game.PlayerClickEffect;
import net.eduard.api.game.PlayerClickEntity;
import net.eduard.api.game.PlayerClickEntityEffect;
import net.eduard.api.game.Slot;
import net.eduard.api.game.Sounds;
import net.eduard.api.manager.CMD;
import net.eduard.api.setup.GameAPI;
import net.eduard.api.setup.ItemAPI;
import net.eduard.api.setup.VaultAPI;

public class AdminCommand extends CMD {

	public AdminCommand() {
		super("admin");
	}
	public void prison(Player player) {
		players.add(player);

		Location loc = player.getLocation();
		loc = loc.add(0, 10, 0);

		player.playSound(player.getLocation(), Sound.WITHER_SPAWN, 2, 1);
		loc.clone().add(0, 0, 0).getBlock().setType(Material.BEDROCK);
		loc.clone().add(0, 3, 0).getBlock().setType(Material.BEDROCK);
		loc.clone().add(0, 1, -1).getBlock().setType(Material.BEDROCK);
		loc.clone().add(-1, 1, 0).getBlock().setType(Material.BEDROCK);
		loc.clone().add(1, 1, 0).getBlock().setType(Material.BEDROCK);
		loc.clone().add(0, 1, 1).getBlock().setType(Material.BEDROCK);
		player.teleport(loc.clone().add(-0.4, 1, -0.4));
		player.sendMessage("�cVoce foi Aprisionado!");
	}
	public void removePrison(Player player) {
		players.remove(player);
		Location loc = player.getLocation();
		player.sendMessage("�aVoce foi liberto da Pris�o!");
		player.playSound(player.getLocation(), Sound.LEVEL_UP, 2, 1);
		loc.clone().add(0, -1, 0).getBlock().setType(Material.AIR);
		loc.clone().add(0, 2, 0).getBlock().setType(Material.AIR);
		loc.clone().add(0, 0, 1).getBlock().setType(Material.AIR);
		loc.clone().add(1, 0, 0).getBlock().setType(Material.AIR);
		loc.clone().add(0, 0, -1).getBlock().setType(Material.AIR);
		loc.clone().add(-1, 0, 0).getBlock().setType(Material.AIR);

	}
	public static List<Player> players = new ArrayList<>();
	public Jump jumpEffect = new Jump(Sounds.create(Sound.BAT_LOOP),
			new Vector(0, 2, 0));
	public String messageOn = "Voce entrou no Modo Admin!";
	public String messageOff = "Voce saiu do Modo Admin!";

	public Slot testAutoSoup = new Slot(
			API.newItem(Material.ENDER_PEARL, "Testar Auto-Soup"), 3);

	public Slot testFF = new Slot(
			API.newItem(Material.MAGMA_CREAM, "Ativar Troca Rapida"), 2);

	public Slot testPrison = new Slot(
			API.newItem(Material.MAGMA_CREAM, "Aprisionar Jogador"), 1);

	public Slot testNoFall = new Slot(
			API.newItem(Material.FEATHER, "Testar No-Fall"), 4);

	public Slot testInfo = new Slot(
			API.newItem(Material.BLAZE_ROD, "Ver Informa��es"), 5);

	public Slot testAntKB = new Slot(
			ItemAPI.addEnchant(API.newItem(Material.STICK, "Testar Knockback"),
					Enchantment.KNOCKBACK, 10),
			6);

	public void joinAdminMode(Player player) {
		ItemAPI.saveItems(player);
		GameAPI.hide(player);
		players.add(player);
		PlayerInventory inv = player.getInventory();
		testAutoSoup.give(inv);
		testFF.give(inv);
		testNoFall.give(inv);
		testInfo.give(inv);
		testAntKB.give(inv);
		testPrison.give(inv);

		player.setGameMode(GameMode.CREATIVE);

	}
	public void leaveAdminMode(Player player) {
		ItemAPI.getItems(player);
		GameAPI.show(player);
		player.setGameMode(GameMode.SURVIVAL);
		players.remove(player);
	}

	@Override
	public void register(Plugin plugin) {
		new PlayerClickEntity(testNoFall.getItem(), new PlayerClickEntityEffect() {

			@Override
			public void onClickAtEntity(Player player, Entity entity,
					ItemStack item) {
				if (players.contains(player)) {
					jumpEffect.create(entity);
				}
			}
		}).register(plugin);
		new PlayerClick(testFF.getItem(), new PlayerClickEffect() {
			@Override
			public void onClick(Player player, Block block, ItemStack item) {
				if (players.contains(player)) {
					GameAPI.show(player);
					GameAPI.makeInvunerable(player, 1);
					API.chat(player, "�6Troca rapida ativada!");
					API.TIME.delay(20, new Runnable() {

						@Override
						public void run() {
							GameAPI.hide(player);
							API.chat(player, "�6Troca rapida desativada!");
						}
					});
				}
			}

		}).register(plugin);
		new PlayerClickEntity(testAutoSoup.getItem(), new PlayerClickEntityEffect() {

			@Override
			public void onClickAtEntity(Player player, Entity entity,
					ItemStack item) {
				if (players.contains(player)) {
					if (entity instanceof Player) {
						Player target = (Player) entity;
						player.openInventory(target.getInventory());

					}
				}
				
			}
		}).register(plugin);
		new PlayerClickEntity(testInfo.getItem(), new PlayerClickEntityEffect() {


			@Override
			public void onClickAtEntity(Player player, Entity entity,
					ItemStack item) {
				if (players.contains(player)) {
					if (entity instanceof Player) {
						Player target = (Player) entity;
						player.sendMessage("�6Informa��es do �e" + target.getName());
						player.sendMessage("�aGamemode: �2" + target.getGameMode());
						player.sendMessage("�aKills: �2"
								+ target.getStatistic(Statistic.PLAYER_KILLS));
						player.sendMessage("�aDeaths: �2"
								+ target.getStatistic(Statistic.DEATHS));
						player.sendMessage("�aIP: �2" + GameAPI.getIp(player));
						if (VaultAPI.hasVault() && VaultAPI.hasEconomy()) {
							player.sendMessage("�aMoney: �2"
									+ VaultAPI.getEconomy().getBalance(player));
						}

					}
				}
				
			}
		}).register(plugin);
		new PlayerClickEntity(testPrison.getItem(), new PlayerClickEntityEffect() {

			@Override
			public void onClickAtEntity(Player player, Entity entity,
					ItemStack item) {
				if (players.contains(player)) {
					if (entity instanceof Player) {
						Player target = (Player) entity;
						Location loc = target.getLocation();
						loc.clone().add(0, 10, 0).getBlock()
								.setType(Material.BEDROCK);
						loc.clone().add(0, 11, 1).getBlock()
								.setType(Material.BEDROCK);
						loc.clone().add(0, 11, -1).getBlock()
								.setType(Material.BEDROCK);
						loc.clone().add(1, 11, 0).getBlock()
								.setType(Material.BEDROCK);
						loc.clone().add(-1, 11, 0).getBlock()
								.setType(Material.BEDROCK);
						loc.clone().add(0, 13, 0).getBlock()
								.setType(Material.BEDROCK);
						target.teleport(loc.add(0, 11, 0));

					}
				}
				
			}
		}).register(plugin);

		super.register(plugin);
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (API.onlyPlayer(sender)) {
			Player p = (Player) sender;
			if (players.contains(p)) {
				players.remove(p);
				leaveAdminMode(p);
				p.sendMessage(messageOff);
			} else {
				players.add(p);
				joinAdminMode(p);
				p.sendMessage(messageOn);
			}

		}
		return true;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void event(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			Player p = (Player) e.getDamager();
			if (players.contains(p)) {
				e.setCancelled(false);
			}

		}
	}

}
