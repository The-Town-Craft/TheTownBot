package net.thetowncraft.townbot.listeners.discord.commands;

import net.dv8tion.jda.api.Permission;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Set;

public class Whitelist extends DiscordCommand {

	@Override
	public void execute(CommandEvent.Discord event) {
		String[] args = event.getArgs();

		String usage = "**Incomplete Command!**" +
				"\nCorrect Usage:" +
				"\n`/whitelist` `add` `<playername>`" +
				"\n`/whitelist` `remove` `<playername>`" +
				"\n`/whitelist` `list`";

		if(args.length == 1) {
			event.getChannel().sendMessage(usage).queue();
			return;
		}
		if(args[1].equalsIgnoreCase("add")) {
			if(args.length == 2) {
				event.getChannel().sendMessage(usage).queue();
				return;
			}
			OfflinePlayer player = Bukkit.getOfflinePlayer(args[2]);
			if(player.isWhitelisted()) {
				event.getChannel().sendMessage(":x: **" + player.getName() + "** is already whitelisted!").queue();
				return;
			}
			player.setWhitelisted(true);
			event.getChannel().sendMessage(":white_check_mark: Added **" + player.getName() + "** to the whitelist!").queue();
		}
		if(args[1].equalsIgnoreCase("remove")) {
			if(args.length == 2) {
				event.getChannel().sendMessage(usage).queue();
				return;
			}
			OfflinePlayer player = Bukkit.getOfflinePlayer(args[2]);
			if(!player.isWhitelisted()) {
				event.getChannel().sendMessage(":x: **" + player.getName() + "** is not whitelisted!").queue();
				return;
			}
			player.setWhitelisted(false);
			event.getChannel().sendMessage(":white_check_mark: Removed **" + player.getName() + "** from the whitelist.").queue();
		}
		if(args[1].equalsIgnoreCase("list")) {
			Set<OfflinePlayer> players = Bukkit.getServer().getWhitelistedPlayers();
			Object[] objects = players.toArray();
			StringBuilder message = new StringBuilder("**Whitelisted Players**");
			for (Object object : objects) {
				OfflinePlayer player = (OfflinePlayer) object;
				message.append("\n").append(player.getName());
			}
			event.getChannel().sendMessage(message).queue();
		}
	}

	@Override
	public String getName() {
		return "whitelist";
	}

	@Override
	public String getDescription() {
		return "Add/Remove players, or list the whitelist!";
	}

	@Override
	public Permission getRequiredPermission() {
		return Permission.BAN_MEMBERS;
	}
}
