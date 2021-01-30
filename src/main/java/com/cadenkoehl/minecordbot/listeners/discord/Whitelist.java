package com.cadenkoehl.minecordbot.listeners.discord;

import com.cadenkoehl.minecordbot.MinecordBot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import java.util.Set;

public class Whitelist extends ListenerAdapter {
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");
		if(args[0].equalsIgnoreCase(MinecordBot.prefix + "whitelist")) {
			Member member = event.getMember();
			if(!member.hasPermission(Permission.BAN_MEMBERS)) {
				event.getChannel().sendMessage(":x: You can't use that!").queue();
				return;
			}
			if(args.length == 1) {
				event.getChannel().sendMessage(usage).queue();
				return;
			}
			if(args[1].equalsIgnoreCase("add")) {
				if(args.length == 2) {
					event.getChannel().sendMessage(usage).queue();
					System.out.println("Sent usage message at line 29");
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
					System.out.println("Sent usage message at line 43");
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
					System.out.println("Attempting to cast object...");
					OfflinePlayer player = (OfflinePlayer) object;
					message.append("\n").append(player.getName());
					System.out.println("Completed!");
				}
				event.getChannel().sendMessage(message).queue();
			}
		}
		if(args[0].equalsIgnoreCase(MinecordBot.prefix + "unwhitelist")) {
			event.getChannel().sendMessage(
					":x: This command is no longer supported, and has been replaced with the `/whitelist` command!" +
					"\nCorrect usage:" +
					"\n`/whitelist` `add` `<playername>`" +
					"\n`/whitelist` `remove` `<playername>`" +
					"\n`/whitelist` `list`").queue();
		}
	}
	String usage = "**Incomplete Command!**" +
			"\nCorrect Usage:" +
			"\n`/whitelist` `add` `<playername>`" +
			"\n`/whitelist` `remove` `<playername>`" +
			"\n`/whitelist` `list`";
}
