package com.cadenkoehl.minecordbot.listeners.minecraft;

import java.util.Random;

import com.cadenkoehl.minecordbot.listeners.util.SkinRender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

import com.cadenkoehl.minecordbot.Constants;
import com.cadenkoehl.minecordbot.MinecordBot;

import net.dv8tion.jda.api.EmbedBuilder;

public class Sleep implements Listener {
	
	@EventHandler
	public void onSleep(PlayerBedEnterEvent event) {
		if(!event.isCancelled()) {
			String player = event.getPlayer().getName();
			String[] messages = {"Shh! " + player + " is sleeping!", player + " went to bed! Sweet dreams!"};
			
			Random rand = new Random();
			
			int message = rand.nextInt(messages.length);

			EmbedBuilder embed = new EmbedBuilder();
			embed.setAuthor(messages[message], null, SkinRender.renderHead(event.getPlayer()));
			embed.setColor(Constants.PURPLE);
			
			MinecordBot.jda.getTextChannelById(Constants.MC_CHAT).sendMessage(embed.build()).queue();
		}
	}
}
