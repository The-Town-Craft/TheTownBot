package com.cadenkoehl.minecordbot.listeners.discord;

import com.cadenkoehl.minecordbot.Constants;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MemberJoin extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        event.getGuild().addRoleToMember(event.getMember(), Constants.UNLINKED_ROLE).queue();

        event.getUser().openPrivateChannel().queue(channel -> channel.sendMessage("Heyo! Welcome to **The Town**! If you have any questions or concerns about the server, DM me! Your message is sent straight to the staff! :)").queue());
    }
}
