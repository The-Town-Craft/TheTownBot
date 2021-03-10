package com.cadenkoehl.minecordbot.listeners.minecraft.chat;

import com.cadenkoehl.minecordbot.Bot;
import com.cadenkoehl.minecordbot.listeners.util.SkinRender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import com.cadenkoehl.minecordbot.listeners.util.Constants;

import net.dv8tion.jda.api.EmbedBuilder;

public class Advancements implements Listener {
	
	@EventHandler
	public void onAdv(PlayerAdvancementDoneEvent event) {
		String player = event.getPlayer().getName();
		String adv = event.getAdvancement().getKey().getKey();
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setAuthor(player + " has made the advancement [" + adv + "]", null, SkinRender.renderHead(event.getPlayer()));
		embed.setColor(0x50bb5f);
		
		if(!event.getAdvancement().getKey().getKey().contains("recipes/")) {
			Bot.jda.getTextChannelById(Constants.MC_CHAT).sendMessage(embed.build()).queue();
			Bot.jda.getTextChannelById(Constants.MC_LOGS).sendMessage(embed.build()).queue();
		}
	}
}
