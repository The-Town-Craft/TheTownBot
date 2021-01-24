package com.cadenkoehl.minecordbot.listeners.discord;

import org.bukkit.Bukkit;

import com.cadenkoehl.minecordbot.Constants;
import com.cadenkoehl.minecordbot.MinecordBot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class OnlinePlayers extends ListenerAdapter {
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");
		if (args[0].equalsIgnoreCase(MinecordBot.prefix + "onlineplayers")) {
			
			int playerCount = Bukkit.getServer().getOnlinePlayers().size();
			int maxPlayerCount = Bukkit.getServer().getMaxPlayers();
			
			EmbedBuilder embed = new EmbedBuilder();
			embed.setTitle("There are " + playerCount + " out of " + maxPlayerCount + " players online." );
			embed.setColor(0x50bb5f);

			MinecordBot.jda.getTextChannelById(Constants.chatLink).sendMessage(embed.build()).queue();
			MinecordBot.jda.getTextChannelById(Constants.logChannel).sendMessage(embed.build()).queue();
		}
	}

}
