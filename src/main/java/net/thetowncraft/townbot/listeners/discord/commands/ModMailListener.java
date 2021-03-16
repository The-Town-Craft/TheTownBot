package net.thetowncraft.townbot.listeners.discord.commands;

import net.dv8tion.jda.api.entities.Message;
import net.thetowncraft.townbot.util.Constants;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class ModMailListener extends ListenerAdapter {
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		if(!event.getAuthor().isBot()) {
			String message = event.getMessage().getContentDisplay();
			String member = event.getJDA().getGuildById(Constants.TOWN_DISCORD_ID).getMember(event.getAuthor()).getEffectiveName();
			List<Message.Attachment> attachments = event.getMessage().getAttachments();

			EmbedBuilder embed = new EmbedBuilder();
			embed.setDescription(message);
			embed.setAuthor("DM from " + member, null, event.getAuthor().getEffectiveAvatarUrl());
			embed.setFooter("User ID: " + event.getAuthor().getId(), Constants.THE_TOWN.getIconUrl());

			if(attachments.size() == 1) {
				String url = attachments.get(0).getUrl();
				embed.setImage(url);
			}

			if(attachments.size() > 1) {
				event.getChannel().sendMessage(member + " uploaded " + attachments.size() + " attachments").queue();
				for(Message.Attachment attachment : attachments) {
					String url = attachment.getUrl();
					event.getChannel().sendMessage(url).queue();
				}
			}
			
			event.getJDA().getTextChannelById("781421376086999040").sendMessage(embed.build()).queue();
		}
	}
}
