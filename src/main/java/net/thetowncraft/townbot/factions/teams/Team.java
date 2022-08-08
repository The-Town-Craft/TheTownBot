package net.thetowncraft.townbot.factions.teams;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.thetowncraft.townbot.Bot;

import java.util.List;

public abstract class Team {

    public abstract String getName();
    public abstract String getRoleId();
    public abstract String getChannelId();

    public void add() {

    }

    public List<Member> getMembers() {
        return getRole().getGuild().getMembersWithRoles(getRole());
    }

    public Role getRole() {
        return Bot.jda.getRoleById(getRoleId());
    }
    public TextChannel getChannel() {
        return Bot.jda.getTextChannelById(getChannelId());
    }
}
