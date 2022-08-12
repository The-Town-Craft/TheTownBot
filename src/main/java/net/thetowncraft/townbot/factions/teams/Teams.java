package net.thetowncraft.townbot.factions.teams;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.managers.RoleManager;
import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
import net.thetowncraft.townbot.util.Constants;
import net.thetowncraft.townbot.util.Utils;
import net.thetowncraft.townbot.util.data.Data;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Teams {

    private static File DIR;

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

    public static void save() {
        JSONObject jsonObject = new JSONObject();
        JSONArray teamsArray = new JSONArray();

        for(Team team : TEAMS) {
            JSONObject teamObj = new JSONObject();
            teamObj.put("material", team.getMaterial().name());
            teamObj.put("role_id", team.getRoleId());
            teamObj.put("channel_id", team.getChannelId());
            teamObj.put("leader_id", team.getLeaderUUID().toString());
            teamsArray.put(teamObj);
        }

        jsonObject.put("teams", teamsArray);

        File file = new File(DIR, "teams.json");
        try {
            FileWriter write = new FileWriter(file);
            write.write(jsonObject.toString());
            write.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        DIR = new File(Plugin.get().getDataFolder(),  "teams");
        DIR.mkdirs();

        File file = new File(DIR, "teams.json");
        if(!file.exists()) return;

        JSONObject jsonObject = Data.getJSONObjectFromFile(file);
        JSONArray teamsArray = jsonObject.getJSONArray("teams");

        for(Object obj : teamsArray) {
            try {
                JSONObject teamObj = (JSONObject) obj;
                TEAMS.add(new Team(
                        Material.getMaterial(teamObj.getString("material")),
                        teamObj.getString("role_id"),
                        teamObj.getString("channel_id"),
                        UUID.fromString(teamObj.getString("leader_id"))
                ));
            }
            catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static EmbedBuilder getViewEmbed() {
        EmbedBuilder embed = new EmbedBuilder();

        if(TEAMS.size() == 0) {
            embed.setColor(Constants.RED);
            embed.setDescription(":x: **Error**! There are currently no teams!");
            return embed;
        }

        embed.setColor(Constants.GREEN);
        embed.setAuthor("List of Teams", null, Constants.THE_TOWN.getIconUrl());
        for(Team team : TEAMS) {
            embed.appendDescription("\n**" + team.getName() + "**");
        }
        embed.appendDescription("\nType `" + Bot.prefix + "team` `view` `<team>` for more info!");
        return embed;
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
