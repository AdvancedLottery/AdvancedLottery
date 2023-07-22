package com.gmail.ianlim224.advancedlottery.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.ianlim224.advancedlottery.AdvancedLottery;
import com.gmail.ianlim224.advancedlottery.messages.Messages;

public class LotteryCommand implements CommandExecutor {

	private final AdvancedLottery plugin;
	private final List<Executable> commands;

	public LotteryCommand(AdvancedLottery plugin, List<Executable> commands) {
		this.plugin = plugin;
		this.commands = commands;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			if(plugin.getConfig().getBoolean("open_help_menu_on_base_cmd")) {
				Bukkit.dispatchCommand(sender, "lottery help");
			} else {
				Bukkit.dispatchCommand(sender, "lottery menu");
			}
			return true;
		}

		for (Executable executable : commands) {
			if (executable.getLabel().equalsIgnoreCase(args[0])) {
				if(!(sender instanceof Player) && executable.isCmdPlayerOnly()) {
					sender.sendMessage(AdvancedLottery.f("&4This command can only be executed by a player"));
					return true;
				}
				
				if(!sender.hasPermission(executable.getPermission().getName())) {
					sender.sendMessage(Messages.NOT_ENOUGH_PERMISSIONS.getConfigValue((Player) sender));
					return true;
				}
				
				CommandResponse response = executable.onExecute(sender, args, plugin);
				return response == CommandResponse.SUCCESS;
			}
		}
		return false;
	}
}
