package net.thetowncraft.townbot.listeners.discord.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.util.Constants;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModMailListener extends ListenerAdapter {

	public static final Map<String, String> MESSAGES = new HashMap<>();

	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		if(!event.getAuthor().isBot()) {
			String message = event.getMessage().getContentDisplay();
			String member = event.getJDA().getGuildById(Constants.TOWN_DISCORD_ID).getMember(event.getAuthor()).getEffectiveName();
			List<Message.Attachment> attachments = event.getMessage().getAttachments();

			EmbedBuilder embed = new EmbedBuilder();
			embed.setDescription(message);
			embed.setAuthor("DM from " + member, null, event.getAuthor().getEffectiveAvatarUrl());
			embed.setFooter("User ID: " + event.getAuthor().getId(), Constants.THE_TOWN.getIconUrl());
			embed.setColor(Constants.GREEN);

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
			
			event.getJDA().getTextChannelById("781421376086999040").sendMessage("[" + Constants.STAFF_ROLE.getAsMention() + "]").embed(embed.build()).queue(msg -> {
				MESSAGES.put(msg.getId(), event.getAuthor().getId());
			});
		}
	}

	@Override
	public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		if(!channel.getId().equals(Constants.MODMAIL)) return;

		Message message = event.getMessage();
		Message reply = message.getReferencedMessage();
		if(reply == null) return;

		String userId = MESSAGES.get(reply.getId());
		if(userId == null) return;

		ModMail.sendModMail(Bot.jda.getUserById(userId), event.getGuild(), message.getContentRaw(), event.getMessage().getAttachments());
	}
}
