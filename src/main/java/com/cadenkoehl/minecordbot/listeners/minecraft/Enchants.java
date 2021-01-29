package com.cadenkoehl.minecordbot.listeners.minecraft;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

import com.cadenkoehl.minecordbot.Constants;
import com.cadenkoehl.minecordbot.MinecordBot;

import net.dv8tion.jda.api.EmbedBuilder;

public class Enchants implements Listener {
	
	@EventHandler
	public void onEnchant(EnchantItemEvent event) {
		String player = event.getEnchanter().getName();
		String item = event.getItem().getType().name();
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setDescription("```css\n" + player + " has enchanted their " + item.replace("_", " ").toLowerCase() + "\n```");
		embed.setColor(0x50bb5f);
		
		MinecordBot.jda.getTextChannelById(Constants.logChannel).sendMessage(embed.build()).queue();
		
	}

}
