
package net.eduard.api.command.map;

import net.eduard.api.EduardAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.lib.modules.Mine;
import net.eduard.api.server.minigame.GameSchematic;
import net.eduard.api.lib.manager.CommandManager;

public class MapPos1Command extends CommandManager {

	public MapPos1Command() {
		super("pos1","setpos1","setlow");
		setDescription("Define a posição 1");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (Mine.onlyPlayer(sender)) {
			Player p = (Player) sender;
			GameSchematic schema = EduardAPI.Companion.getSchematic(p);
			schema.setLow(p.getLocation().toVector());
			p.sendMessage("§bEduardAPI §aPosicão 1 setada!");
		}
		return true;
	}

}
