package com.cadenkoehl.minecordbot.listeners.discord;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import com.cadenkoehl.minecordbot.MinecordBot;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class WhitelistRemove extends ListenerAdapter {
	
	@SuppressWarnings("deprecation")
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");
		String username = Arrays.stream(args).skip(1).collect(Collectors.joining(" "));
		
		Member mod = event.getMember();
		
			if(args[0].equalsIgnoreCase(MinecordBot.prefix + "unwhitelist")) {
				if(mod.hasPermission(Permission.ADMINISTRATOR)) {
					OfflinePlayer player = Bukkit.getOfflinePlayer(username);
					if(!player.isWhitelisted()) {
					event.getChannel().sendMessage(username + " is not whitelisted!").queue();
					}
					if(player.isWhitelisted()) {
						player.setWhitelisted(false);
						event.getChannel().sendMessage("Removed " + username + " from the whitelist.").queue();
				}
			}
				if(!mod.hasPermission(Permission.ADMINISTRATOR)) {
					event.getChannel().sendMessage("You can't use that!").queue();
				}
		}
	}
}
