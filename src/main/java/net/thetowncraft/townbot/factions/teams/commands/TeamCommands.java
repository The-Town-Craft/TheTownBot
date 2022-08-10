package net.thetowncraft.townbot.factions.teams.commands;

import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.factions.teams.Team;
import net.thetowncraft.townbot.factions.teams.Teams;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;

import java.lang.reflect.Member;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

/**
ALL METHODS RETURN AN ERROR STRING ON FAILURE, AND NULL ON SUCCESS
 **/
public class TeamCommands {

    public static String create(String[] args, OfflinePlayer player) {
        if(args.length == 1) return "{usage}";

        Team team = Teams.getTeam(player);
        if(team != null) return "You must leave **" + team.getName() + "** to create a new one!";

        String materialName = Arrays.stream(args).skip(1).collect(Collectors.joining(" "));
        Material material = Material.getMaterial(materialName.toUpperCase().replace(" ", "_"));
        if(material == null) return "Please chose the name of a block or an item.";

        String name = Teams.getName(material);
        if(Teams.getByName(name) != null) return "**" + name + "** already exists!";

        Teams.create(material, player);
        return null;
    }

    public static String invite(String[] args, OfflinePlayer player, OfflinePlayer inviter) {
        if(args.length == 1) return "{usage}";

        Team inviterTeam = Teams.getTeam(inviter);
        if(inviterTeam == null) return "You don't have a team to invite **" + player.getName() + "** to!";

        if(inviterTeam.equals(Teams.getTeam(player))) return "**" + player.getName() + "** is already in **" + inviterTeam.getName() + "**!";

        if(!inviterTeam.getLeaderUUID().toString().equals(inviter.getUniqueId().toString())) return "You do not have permission to invite **" + player.getName() + "**!";

        if(inviterTeam.getInvites().contains(player.getUniqueId().toString())) return "You have already invited **" + player.getName() + "**!";

        inviterTeam.createInvite(player);
        return null;
    }

    public static String join(String[] args, OfflinePlayer player) {
        if(args.length == 1) return "{usage}";

        Team playerTeam = Teams.getTeam(player);
        if(playerTeam != null) return "You must leave **" + playerTeam.getName() + "** before joining another team!";

        String teamName = Arrays.stream(args).skip(1).collect(Collectors.joining(" "));
        Team team = Teams.getByName(teamName);
        if(team == null) return "Could not find a team by the name of **" + teamName + "**!";

        if(!team.getInvites().contains(player.getUniqueId().toString())) return "Please ask **" + team.getLeader().getName() + "** for an invite!";

        team.add(player);
        return null;
    }

    public static String leave(String[] args, OfflinePlayer player) {
        Team team = Teams.getTeam(player);
        if(team == null) return "You are not currently in a team!";

        OfflinePlayer leader = team.getLeader();
        if(leader.getUniqueId().equals(player.getUniqueId())) {
            return "As the leader of **" + team.getName() + "**, you must transfer it's ownership before leaving.";
        }

        team.remove(player);
        return null;
    }

    public static String kick(String[] args, OfflinePlayer player, OfflinePlayer kicker) {
        Team team = Teams.getTeam(kicker);
        if(team == null) return "You are not currently in a team!";

        if(args.length == 1) return "{usage}";

        OfflinePlayer leader = team.getLeader();
        if(!leader.getUniqueId().equals(kicker.getUniqueId())) {
            return "You do not have permission to do this!";
        }

        if(player.getUniqueId().toString().equals(kicker.getUniqueId().toString())) return "You cannot kick yourself from your own team!";

        Team playerTeam = Teams.getTeam(player);
        if(!team.equals(playerTeam)) return "**" + player.getName() + "** is not part of **" + team.getName() + "**";

        team.remove(player);
        return null;
    }

    public static String transfer(String[] args, OfflinePlayer player, OfflinePlayer to) {
        Team team = Teams.getTeam(player);
        if(team == null) return "You are not currently in a team!";

        if(args.length == 1) return "{usage}";

        OfflinePlayer leader = team.getLeader();
        if(!leader.getUniqueId().equals(player.getUniqueId())) {
            return "You do not have permission to do this!";
        }

        if(player.getUniqueId().toString().equals(to.getUniqueId().toString())) return "You already own **" + team.getName() + "**!";

        Team toTeam = Teams.getTeam(to);
        if(!team.equals(toTeam)) return "**" + to.getName() + "** is not part of **" + team.getName() + "**";

        team.transfer(to);
        return null;
    }

    public static String delete(String[] args, OfflinePlayer player) {
        Team team = Teams.getTeam(player);
        if(team == null) return "You are not currently part of any team!";
        if(!team.getLeaderUUID().toString().equals(player.getUniqueId().toString())) return "You do not have permission to delete this team!";

        if(args.length == 1) return "Are you sure you want to delete **" + team.getName() + "**? Type your Discord tag as the final argument to this command to confirm.";
        if(!args[1].equals(AccountManager.getInstance().getDiscordMember(player).getUser().getDiscriminator()))
            return "Are you sure you want to delete **" + team.getName() + "**? Type your Discord tag as the final argument to this command to confirm.";

        Teams.delete(team);
        return null;
    }
}
