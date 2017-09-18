package net.eduard.api.tutorial.comandos;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class ComandoGanharSuaCabe�a implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		Player jogador = (Player) sender;
		if (command.getName().equalsIgnoreCase("headof")) {
			ItemStack cabe�a = new ItemStack(Material.SKULL_ITEM,1,(short) 3);
			SkullMeta meta = (SkullMeta) cabe�a.getItemMeta();
			meta.setOwner(jogador.getName());
			cabe�a.setItemMeta(meta);
			jogador.sendMessage("�aVoce ganhou sua cabe�a!");
			jogador.getInventory().addItem(cabe�a);
		}
		return true;
	}
}
