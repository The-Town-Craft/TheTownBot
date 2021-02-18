package com.cadenkoehl.minecordbot.listeners.discord.commands;

import org.bukkit.Bukkit;

import com.cadenkoehl.minecordbot.Constants;
import com.cadenkoehl.minecordbot.MinecordBot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.entity.Player;

import java.util.Collection;

public class OnlinePlayers extends ListenerAdapter {
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");
		if (args[0].equalsIgnoreCase(MinecordBot.prefix + "onlineplayers")) {

			Collection<? extends Player> players = Bukkit.getServer().getOnlinePlayers();
			int playerCount = players.size();
			int maxPlayerCount = Bukkit.getServer().getMaxPlayers();

			EmbedBuilder embed = new EmbedBuilder();

			embed.setTitle("There are " + playerCount + " out of " + maxPlayerCount + " players online." );
			embed.setColor(0x50bb5f);
			for(Player player : players) {
				String name = player.getName();
				embed.appendDescription("\n" + name);
			}
			MinecordBot.jda.getTextChannelById(Constants.MC_CHAT).sendMessage(embed.build()).queue();
			MinecordBot.jda.getTextChannelById(Constants.MC_LOGS).sendMessage(embed.build()).queue();
		}
	}
}