package net.thetowncraft.townbot.factions.teams.commands;

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

        Teams.create(material, player);
        return null;
    }

    public static String invite(String[] args, OfflinePlayer player, OfflinePlayer inviter) {
        if(args.length == 1) return "{usage}";

        Team inviterTeam = Teams.getTeam(inviter);
        if(inviterTeam == null) return "You don't have a team to invite **" + player.getName() + "** to!";

        if(!inviterTeam.getLeaderUUID().toString().equals(inviter.getUniqueId().toString())) return "You do not have permission to invite **" + player.getName() + "**!";

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
