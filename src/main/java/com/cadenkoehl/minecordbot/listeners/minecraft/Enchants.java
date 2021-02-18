package com.cadenkoehl.minecordbot.listeners.minecraft;

import com.cadenkoehl.minecordbot.listeners.util.SkinRender;
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

		if(item.equalsIgnoreCase("air")) {
			return;
		}
		EmbedBuilder embed = new EmbedBuilder();
		embed.setAuthor(player + " enchanted their " + item.replace("_", " ").toLowerCase(), null, SkinRender.renderHead(event.getEnchanter()));
		embed.setColor(Constants.PURPLE);
		
		MinecordBot.jda.getTextChannelById(Constants.MC_LOGS).sendMessage(embed.build()).queue();
	}
}