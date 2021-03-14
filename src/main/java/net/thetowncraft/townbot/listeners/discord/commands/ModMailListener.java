package net.thetowncraft.townbot.listeners.discord.commands;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.util.Constants;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ModMailListener extends ListenerAdapter {
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		if(!event.getAuthor().isBot()) {
			String message = event.getMessage().getContentDisplay();
			String member = event.getJDA().getGuildById(Constants.TOWN_DISCORD_ID).getMember(event.getAuthor()).getEffectiveName();
			
			EmbedBuilder eb = new EmbedBuilder();
			eb.setDescription(message);
			eb.setAuthor("DM from " + member, null, event.getAuthor().getEffectiveAvatarUrl());
			eb.setFooter("User ID: " + event.getAuthor().getId(), Constants.THE_TOWN.getIconUrl());
			
			event.getJDA().getTextChannelById("781421376086999040").sendMessage(eb.build()).queue();
		}
	}
}
