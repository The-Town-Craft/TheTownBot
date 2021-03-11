package net.thetowncraft.townbot.listeners.minecraft.chat;

import java.util.Random;

import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.util.SkinRender;
import net.thetowncraft.townbot.util.Constants;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

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
			
			Bot.jda.getTextChannelById(Constants.MC_CHAT).sendMessage(embed.build()).queue();
		}
	}
}
