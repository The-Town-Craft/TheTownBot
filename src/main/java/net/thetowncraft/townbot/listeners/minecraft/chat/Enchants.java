package net.thetowncraft.townbot.listeners.minecraft.chat;

import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.util.SkinRender;
import net.thetowncraft.townbot.util.Constants;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

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
		
		Bot.jda.getTextChannelById(Constants.MC_LOGS).sendMessage(embed.build()).queue();
	}
}