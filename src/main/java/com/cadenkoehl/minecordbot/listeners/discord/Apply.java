package com.cadenkoehl.minecordbot.listeners.discord;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Apply extends ListenerAdapter {
	
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		if(event.getGuild().getId().equalsIgnoreCase("730975912320827452")) {
			event.getUser().openPrivateChannel().queue((channel) -> {
				channel.sendMessage("Thanks for joining The Town! Below is a link to our whitelist application! Once submitted, "
						+ "it will be reviewed as soon as possible! If you are accepted, you will be notified, and you will gain access"
						+ " to all the channels in the Discord server, and will be whitelisted shortly! If you are not, you will be kicked from the Discord. (No hard feelings!)"
						+ " \nhttps://forms.gle/v6m9m29Uspb9XFYR6\nIf you have any questions, DM me! (your message will be sent straight to the staff!)").queue();
			});
		}
	}
}
