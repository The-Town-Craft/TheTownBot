package net.thetowncraft.townbot.listeners.discord;

import net.thetowncraft.townbot.modmail.ModMail;
import net.thetowncraft.townbot.util.Constants;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MemberJoin extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        event.getGuild().addRoleToMember(event.getMember(), Constants.MEMBER_ROLE).complete();
        event.getGuild().addRoleToMember(event.getMember(), Constants.UNLINKED_ROLE).queue();
        event.getGuild().addRoleToMember(event.getMember(), Constants.PINGS_CATEGORY_ROLE).queue();
        event.getGuild().addRoleToMember(event.getMember(), Constants.OTHER_CATEGORY_ROLE).queue();
        event.getGuild().addRoleToMember(event.getMember(), Constants.BOSSES_CATEGORY_ROLE).queue();

        ModMail.sendModMail(event.getUser(), event.getGuild(),
                "Heyo! Welcome to **The Town**! " +
                "If you have any questions or concerns about the server, " +
                        "DM me! Your message is sent straight to the staff! :)", new ArrayList<>());
    }
}