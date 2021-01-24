package com.cadenkoehl.minecordbot.listeners.discord;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.cadenkoehl.minecordbot.MinecordBot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ModMail extends ListenerAdapter{
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		if(!event.getAuthor().isBot()) {
			String message = event.getMessage().getContentDisplay();
			String user = event.getAuthor().getAsTag();
			
			EmbedBuilder eb = new EmbedBuilder();
			eb.setDescription("```css\n" + message + "\n```");
			eb.setAuthor("DM from " + user, null, event.getAuthor().getEffectiveAvatarUrl());
			
			event.getJDA().getTextChannelById("781421376086999040").sendMessage(eb.build()).queue();
		}
	}
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");
		if(args[0].equalsIgnoreCase(MinecordBot.prefix + "mm")) {
			try {
				String message = Arrays.stream(args).skip(2).collect(Collectors.joining(" "));
				if(event.getMember().hasPermission(Permission.BAN_MEMBERS)) {
					Member member = event.getMessage().getMentionedMembers().get(0);
					event.getChannel().sendMessage("**Success!** ModMail was sent to **" + member.getUser().getAsTag() + "**!\nMessage: \"*" + message + "*\"").queue();
					member.getUser().openPrivateChannel().queue((channel -> {
						String senderName = event.getMember().getEffectiveName();
						String senderAvatar = event.getAuthor().getEffectiveAvatarUrl();

						EmbedBuilder embed = new EmbedBuilder();
						embed.setAuthor("ModMail from The Town!", null, event.getGuild().getIconUrl());
						embed.setDescription("```css\n" + message + "\n```");
						embed.setFooter(senderName, senderAvatar);
						
						channel.sendMessage(embed.build()).queue();
					}));
				}
				if(!event.getMember().hasPermission(Permission.BAN_MEMBERS)) {
					event.getChannel().sendMessage("You can't use that!").queue();
				}
			} catch(Exception e) {
				event.getChannel().sendMessage("**Something went wrong with the command!** Please try again!\n**Usage:** "
						+ "`/mm` `[membername]` `[message]`").queue();
				e.printStackTrace();
			}
		}
	}
}
