package com.cadenkoehl.minecordbot.listeners.discord;

import org.bukkit.Bukkit;

import com.cadenkoehl.minecordbot.Constants;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.md_5.bungee.api.ChatColor;

public class DiscordChatListener extends ListenerAdapter {
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		if(!event.getAuthor().isBot()) {
			if(event.getChannel().getId().equalsIgnoreCase(Constants.MC_CHAT)) {
				String message = event.getMessage().getContentDisplay();
				String name = event.getMember().getEffectiveName();

				Bukkit.getServer().broadcastMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "[Discord]" + ChatColor.RESET + " <" + name + "> " + message);
			}
		}
		if(event.getChannel().getId().equalsIgnoreCase(Constants.ANNOUNCEMENTS)) {
			String message = event.getMessage().getContentDisplay();
			Bukkit.getServer().broadcastMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "[Discord] " + ChatColor.RESET + "" + ChatColor.GOLD + "" + ChatColor.BOLD + "[ANNOUNCEMENT] " + ChatColor.RESET + "" + ChatColor.AQUA + message);
		}
	}
}