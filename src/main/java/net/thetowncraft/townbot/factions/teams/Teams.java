package net.thetowncraft.townbot.factions.teams;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.IPermissionHolder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.managers.RoleManager;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
import net.thetowncraft.townbot.util.Constants;
import net.thetowncraft.townbot.util.Utils;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Teams {

    private static final List<Team> TEAMS = new ArrayList<>();

    public static Team getByName(String name) {
        for(Team team : TEAMS) {
            if(team.getName().equalsIgnoreCase(name)) return team;
        }
        return null;
    }

    public static Team getTeam(OfflinePlayer player) {
        Member member = AccountManager.getInstance().getDiscordMember(player);
        if(member == null) return null;
        else return getTeam(member);
    }

    public static Team getTeam(Member member) {
        for(Team team : TEAMS) {
            if(member.getRoles().contains(team.getRole())) return team;
        }
        return null;
    }

    public static void create(Material material, OfflinePlayer leader) {
        Member member = AccountManager.getInstance().getDiscordMember(leader);
        String name = getName(material);
        Constants.THE_TOWN.createRole().queue(role -> {
            RoleManager manager = role.getManager();
            manager.setName(name).queue();

            Constants.THE_TOWN.createTextChannel(name.toLowerCase().replace(" ", "-")).queue(channel -> {
                channel.createPermissionOverride(Constants.THE_TOWN.getPublicRole()).setDeny(Permission.VIEW_CHANNEL).queue();
                channel.createPermissionOverride(role).setAllow(Permission.VIEW_CHANNEL).queue();
                channel.getManager().setParent(Constants.COMMUNITY_CATEGORY).queue();
                member.getGuild().addRoleToMember(member, role).queue();
                TEAMS.add(new Team(material, role.getId(), channel.getId(), leader.getUniqueId()));
                channel.sendMessage(":partying_face: " + member.getAsMention() + " has created **" + name + "**!").queue();
            });
        });
    }

    public static void delete(Team team) {
        TEAMS.remove(team);
        if(team.getRole() != null) team.getRole().delete().queue();
        if(team.getChannel() != null) team.getChannel().delete().queue();
    }

    public static String getName(Material material) {
        return "Team " + Utils.capitalizeString(material.name().replace("_", " "));
    }

    public static List<Team> getTeams() {
        return TEAMS;
    }
}
