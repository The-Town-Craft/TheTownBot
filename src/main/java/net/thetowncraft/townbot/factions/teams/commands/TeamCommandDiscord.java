package net.thetowncraft.townbot.factions.teams.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.factions.economy.shop.ShopManager;
import net.thetowncraft.townbot.factions.teams.Team;
import net.thetowncraft.townbot.factions.teams.Teams;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TeamCommandDiscord extends DiscordCommand {


    @Override
    public void execute(CommandEvent.Discord event) {
        String[] args = Arrays.copyOfRange(event.getArgs(), 1, event.getArgs().length);

        Member member = event.getMember();
        TextChannel channel = event.getChannel();

        OfflinePlayer player = AccountManager.getInstance().getMinecraftPlayer(member);
        if(player == null) {
            channel.sendMessage(":x: **Error** Your account is not linked!").queue();
            return;
        }

        if(args.length == 0) {
            channel.sendMessage(getUsage()).queue();
            return;
        }

        switch (args[0]) {
            case "create": {

                if(!ShopManager.getPurchases(member).contains(ShopManager.TEAMS)) {
                    event.getChannel().sendMessage(ShopManager.TEAMS.getEmbed().build()).queue();
                    return;
                }

                String output = TeamCommands.create(args, player);
                if (output == null) {
                    channel.sendMessage(":white_check_mark: **Success**! Team was created!").queue();
                    member.getGuild().removeRoleFromMember(member, ShopManager.TEAMS.getRole()).queue();
                }
                else channel.sendMessage(":x: **Error**! " + output.replace("{usage}", getUsage())).queue();
                break;
            }
            case "list": {
                channel.sendMessage(Teams.getViewEmbed().build()).queue();
                break;
            }
            case "view": {
                if(args.length == 1) {
                    Team team = Teams.getTeam(player);
                    if(team == null) {
                        channel.sendMessage(getUsage()).queue();
                        return;
                    }
                    channel.sendMessage(team.getEmbed().build()).queue();
                }
                else {
                    String teamName = Arrays.stream(args).skip(1).collect(Collectors.joining(" "));
                    Team team = Teams.getByName(teamName);
                    if(team == null) {
                        channel.sendMessage(":x: **Error**! Could not find a team by the name of **" + teamName + "**").queue();
                        return;
                    }
                    event.getChannel().sendMessage(team.getEmbed().build()).queue();
                }
                break;
            }
            case "invite": {
                OfflinePlayer invited = getReferencedPlayer(args, event);
                if (invited == null) return;
                String output = TeamCommands.invite(args, invited, player);
                if (output == null)
                    channel.sendMessage(":white_check_mark: **Success**! **" + invited.getName() + "** was invited!").queue();
                else channel.sendMessage(":x: **Error**! " + output.replace("{usage}", getUsage())).queue();
                break;
            }
            case "join": {
                String output = TeamCommands.join(args, player);
                if (output == null)
                    channel.sendMessage(":white_check_mark: **Success**! You have been added the team!").queue();
                else channel.sendMessage(":x: **Error**! " + output.replace("{usage}", getUsage())).queue();
                break;
            }
            case "leave": {
                String output = TeamCommands.leave(args, player);
                if (output == null) channel.sendMessage(":sob: **Success**! You have left the team!").queue();
                else channel.sendMessage(":x: **Error**! " + output.replace("{usage}", getUsage())).queue();
                break;
            }
            case "kick": {
                OfflinePlayer kicked = getReferencedPlayer(args, event);
                if (kicked == null) return;
                String output = TeamCommands.kick(args, kicked, player);
                if (output == null) channel.sendMessage(":white_check_mark: **Success**! **" + kicked.getName() + "** was kicked!").queue();
                else channel.sendMessage(":x: **Error**! " + output.replace("{usage}", getUsage())).queue();
                break;
            }
            case "color": {
                String output = TeamCommands.color(args, player);
                if (output == null) channel.sendMessage(":white_check_mark: **Success**! The team color was changed!").queue();
                else channel.sendMessage(":x: **Error**! " + output.replace("{usage}", getUsage())).queue();
                break;
            }
            case "transfer": {
                OfflinePlayer to = getReferencedPlayer(args, event);
                if (to == null) return;
                String output = TeamCommands.transfer(args, player, to);
                if (output == null) channel.sendMessage(":white_check_mark: **Success**! Team was transferred to **" + to.getName() + "**!").queue();
                else channel.sendMessage(":x: **Error**! " + output.replace("{usage}", getUsage())).queue();
                break;
            }
            case "delete": {
                String output = TeamCommands.delete(args, player);
                if (output == null) channel.sendMessage(":white_check_mark: **Success**! Team was deleted!").queue();
                else channel.sendMessage(":x: **Error**! " + output.replace("{usage}", getUsage())).queue();
                break;
            }
        }
    }

    public OfflinePlayer getReferencedPlayer(String[] args, CommandEvent.Discord event) {
        TextChannel channel = event.getChannel();
        if(args.length == 1) {
            channel.sendMessage(getUsage()).queue();
            return null;
        }
        OfflinePlayer player;
        List<Member> members = event.getMessage().getMentionedMembers();
        if(members.size() == 0) {
            player = Bukkit.getServer().getOfflinePlayer(args[1]);
            if(!AccountManager.getInstance().isLinked(player)) {
                channel.sendMessage(":x: **Error**! **" + args[1] + "**'s account is not linked!").queue();
                return null;
            }
        }
        else {
            Member invitedMember = members.get(0);
            player = AccountManager.getInstance().getMinecraftPlayer(invitedMember);
            if(player == null) {
                channel.sendMessage(":x: **Error**! **" + invitedMember.getEffectiveName() + "**'s account is not linked!").queue();
                return null;
            }
        }
        return player;
    }

    public String getUsage() {
        return "**Usage**:" +
                "\n`" + Bot.prefix + "team` `create` `<material name>`" +
                "\n`" + Bot.prefix + "team` `list`" +
                "\n`" + Bot.prefix + "team` `view` `<team>`" +
                "\n`" + Bot.prefix + "team` `invite` `<player>`" +
                "\n`" + Bot.prefix + "team` `join` `<team>`"  +
                "\n`" + Bot.prefix + "team` `leave`"  +
                "\n`" + Bot.prefix + "team` `kick` `<player>`"  +
                "\n`" + Bot.prefix + "team` `color` `<hex>`"  +
                "\n`" + Bot.prefix + "team` `transfer` `<player>`"  +
                "\n`" + Bot.prefix + "team` `delete`";
    }

    @Override
    public String getName() {
        return "team";
    }

    @Override
    public String getDescription() {
        return "Create and manage player made teams!";
    }

    @Override
    public Permission getRequiredPermission() {
        return null;
    }
}
