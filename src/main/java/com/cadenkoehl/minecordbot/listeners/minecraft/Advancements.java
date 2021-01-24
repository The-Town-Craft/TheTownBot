package com.cadenkoehl.minecordbot.listeners.minecraft;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import com.cadenkoehl.minecordbot.Constants;
import com.cadenkoehl.minecordbot.MinecordBot;

import net.dv8tion.jda.api.EmbedBuilder;

public class Advancements implements Listener {
	
	@EventHandler
	public void onAdv(PlayerAdvancementDoneEvent event) {
		String player = event.getPlayer().getName();
		String adv = event.getAdvancement().getKey().getKey();
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setDescription("```css\n" + player + " has made the advancement [" + adv + "]\n```");
		embed.setColor(0x50bb5f);
		
		if(!event.getAdvancement().getKey().getKey().contains("recipes/")) {
			MinecordBot.jda.getTextChannelById(Constants.chatLink).sendMessage(embed.build()).queue();
			MinecordBot.jda.getTextChannelById(Constants.logChannel).sendMessage(embed.build()).queue();
		}
	}
}
