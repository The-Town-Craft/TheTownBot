package com.cadenkoehl.minecordbot.listeners.minecraft;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

import com.cadenkoehl.minecordbot.Constants;
import com.cadenkoehl.minecordbot.MinecordBot;

import net.dv8tion.jda.api.EmbedBuilder;

public class CommandLog implements Listener {
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		String player = event.getPlayer().getName();
		String command = event.getMessage();
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setDescription(player + " ran command `" + command + "`");
		embed.setColor(0xf4271);
		
		MinecordBot.jda.getTextChannelById(Constants.logChannel).sendMessage(embed.build()).queue();
	}
	
	@EventHandler
	public void onGamemodeChange(PlayerGameModeChangeEvent event) {
		String player = event.getPlayer().getName();
		String gamemode = event.getNewGameMode().name();
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setDescription(player + "'s gamemode was changed to " + gamemode.toLowerCase());
		embed.setColor(0x50bb5f);
		
		MinecordBot.jda.getTextChannelById(Constants.logChannel).sendMessage(embed.build()).queue();
		
		
	}
}
