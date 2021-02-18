package com.cadenkoehl.minecordbot.listeners.discord.commands;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.cadenkoehl.minecordbot.Constants;
import com.cadenkoehl.minecordbot.MinecordBot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ModMail extends ListenerAdapter {
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		if(!event.getAuthor().isBot()) {
			String message = event.getMessage().getContentDisplay();
			String member = event.getJDA().getGuildById(Constants.TOWN_DISCORD).getMember(event.getAuthor()).getEffectiveName();
			
			EmbedBuilder eb = new EmbedBuilder();
			eb.setDescription(message);
			eb.setAuthor("DM from " + member, null, event.getAuthor().getEffectiveAvatarUrl());
			
			event.getJDA().getTextChannelById("781421376086999040").sendMessage(eb.build()).queue();
		}
	}

	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");
		if(args[0].equalsIgnoreCase(MinecordBot.prefix + "mm")) {

			if(event.isWebhookMessage()) return;
			if(event.getAuthor().isBot()) return;

			if(!event.getMember().hasPermission(Permission.BAN_MEMBERS)) {
				event.getChannel().sendMessage(":x: You can't use that!").queue();
				return;
			}

			if(args.length == 1) {
				event.getChannel().sendMessage(":x: Please specify a member to ModMail!").queue();
				return;
			}

			String message = Arrays.stream(args).skip(2).collect(Collectors.joining(" "));
			if(message.isEmpty()) {
				event.getChannel().sendMessage(":x: Please specify a message!").queue();
				return;
			}

			if(args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("everyone") || args[1].equalsIgnoreCase("@everyone")) {
				List<Member> members = event.getGuild().getMembers();

				event.getChannel().sendMessage("Preparing to ModMail" + event.getGuild().getMemberCount() + " members with message \"" + message + "\"").queue();

				for(Member member : members) {
					try {
						if(member.getUser().isBot()) continue;
						if(member.hasPermission(Permission.ADMINISTRATOR) && !member.isOwner()) continue;
						member.getUser().openPrivateChannel().queue((channel -> {

							EmbedBuilder embed = new EmbedBuilder();
							embed.setAuthor("ModMail from The Town!", null, event.getGuild().getIconUrl());
							embed.setDescription(message);

							channel.sendMessage(embed.build()).queue();

							event.getChannel().sendMessage("**Success!** ModMail was sent to **" + member.getUser().getAsTag() + "**!").queue();
						}));
					}
					catch(Exception ex) {
						event.getChannel().sendMessage(":x: **Something went wrong while messaging " + member.getEffectiveName() + "!** " + ex.getMessage()).queue();
					}
				}

				event.getChannel().sendMessage(":white_check_mark: **Finished!**").queue();
				return;
			}

			try {
				List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
				if(mentionedMembers.size() == 0) {
					event.getChannel().sendMessage(":x: Please specify a member to ModMail!").queue();
					return;
				}
				Member member = mentionedMembers.get(0);
				member.getUser().openPrivateChannel().queue((channel -> {
					String senderName = event.getMember().getEffectiveName();
					String senderAvatar = event.getAuthor().getEffectiveAvatarUrl();

					EmbedBuilder embed = new EmbedBuilder();
					embed.setAuthor("ModMail from The Town!", null, event.getGuild().getIconUrl());
					embed.setDescription(message);
					embed.setFooter("From " + senderName, senderAvatar);

					channel.sendMessage(embed.build()).queue();
				}));
				event.getChannel().sendMessage("**Success!** ModMail was sent to **" + member.getUser().getAsTag() + "**!\nMessage: \"*" + message + "*\"").queue();
			}
			catch(Exception ex) {
				event.getChannel().sendMessage(":x: **Something went wrong!** " + ex.getMessage()).queue();
				ex.printStackTrace();
			}
		}
	}
}
