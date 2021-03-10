package com.cadenkoehl.minecordbot.listeners.minecraft.chat;

import com.cadenkoehl.minecordbot.Bot;
import com.cadenkoehl.minecordbot.listeners.chatmute.MuteManager;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

import com.cadenkoehl.minecordbot.listeners.util.Constants;

import net.dv8tion.jda.api.EmbedBuilder;

public class CommandLog implements Listener {
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		String player = event.getPlayer().getName();
		String command = event.getMessage();

		MuteManager manager = new MuteManager();
		if(manager.isMuted(event.getPlayer())) {
			event.getPlayer().sendMessage(ChatColor.RED + "You cannot send commands as you have been muted!");
			event.setCancelled(true);
			return;
		}
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setDescription(player + " ran command `" + command + "`");
		embed.setColor(0xf4271);
		
		Bot.jda.getTextChannelById(Constants.MC_LOGS).sendMessage(embed.build()).queue();
	}
	
	@EventHandler
	public void onGameModeChange(PlayerGameModeChangeEvent event) {
		String player = event.getPlayer().getName();
		String gamemode = event.getNewGameMode().name();
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setDescription(player + "'s gamemode was changed to " + gamemode.toLowerCase());
		embed.setColor(0x50bb5f);
		
		Bot.jda.getTextChannelById(Constants.MC_LOGS).sendMessage(embed.build()).queue();
		
		
	}
}
