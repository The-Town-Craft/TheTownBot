package com.cadenkoehl.minecordbot.listeners.minecraft;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.cadenkoehl.minecordbot.Constants;
import com.cadenkoehl.minecordbot.MinecordBot;

import net.dv8tion.jda.api.EmbedBuilder;

public class SuperVanishCompat implements Listener {
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		if(event.getMessage().equalsIgnoreCase("/vanish")) {
			String player = event.getPlayer().getName();
			EmbedBuilder embed = new EmbedBuilder();
			embed.setTitle(player + " left the game");
			embed.setColor(0xb83838);
			
			EmbedBuilder embed1 = new EmbedBuilder();
			embed1.setDescription(player + " has vanished!");
			embed1.setColor(0xb83838);
			
			MinecordBot.jda.getTextChannelById(Constants.chatLink).sendMessage(embed.build()).queue();
			MinecordBot.jda.getTextChannelById(Constants.logChannel).sendMessage(embed1.build()).queue();
		}
		
		if(event.getMessage().equalsIgnoreCase("/vanish off")) {
			String player = event.getPlayer().getName();
			EmbedBuilder embed = new EmbedBuilder();
			embed.setTitle(player + " joined the game");
			embed.setColor(0x50bb5f);
			
			EmbedBuilder embed1 = new EmbedBuilder();
			embed1.setDescription(player + " has un-vanished!");
			embed1.setColor(0x50bb5f);
			
			MinecordBot.jda.getTextChannelById(Constants.chatLink).sendMessage(embed.build()).queue();
			MinecordBot.jda.getTextChannelById(Constants.logChannel).sendMessage(embed1.build()).queue();
			
		}
		
		if(event.getMessage().equalsIgnoreCase("/vanish on")) {
			String player = event.getPlayer().getName();
			EmbedBuilder embed = new EmbedBuilder();
			embed.setTitle(player + " left the game");
			embed.setColor(0xb83838);
			
			EmbedBuilder embed1 = new EmbedBuilder();
			embed1.setDescription(player + " has vanished!");
			embed1.setColor(0xb83838);
			
			MinecordBot.jda.getTextChannelById(Constants.chatLink).sendMessage(embed.build()).queue();
			MinecordBot.jda.getTextChannelById(Constants.logChannel).sendMessage(embed1.build()).queue();
		}
	}
}
