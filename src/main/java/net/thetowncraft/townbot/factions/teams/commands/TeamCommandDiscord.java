package net.thetowncraft.townbot.factions.teams.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.factions.teams.Teams;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;
import java.util.List;

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

        if(args[0].equals("create")) {
            String output = TeamCommands.create(args, player);
            if(output == null) channel.sendMessage(":white_check_mark: **Success**! Team was created!").queue();
            else channel.sendMessage(":x: **Error**! " + output.replace("{usage}", getUsage())).queue();
        }
        else if(args[0].equals("invite")) {
            if(args.length == 1) {
                channel.sendMessage(getUsage()).queue();
                return;
            }
            OfflinePlayer invited;
            List<Member> members = event.getMessage().getMentionedMembers();
            if(members.size() == 0) {
                invited = Bukkit.getServer().getOfflinePlayer(args[1]);
                if(!AccountManager.getInstance().isLinked(invited)) {
                    channel.sendMessage(":x: **Error**! **" + args[1] + "**'s account is not linked!").queue();
                    return;
                }
            }
            else {
                Member invitedMember = members.get(0);
                invited = AccountManager.getInstance().getMinecraftPlayer(invitedMember);
                if(invited == null) {
                    channel.sendMessage(":x: **Error**! **" + invitedMember.getEffectiveName() + "**'s account is not linked!").queue();
                    return;
                }
            }
            String output = TeamCommands.invite(args, invited, player);
            if(output == null) channel.sendMessage(":white_check_mark: **Success**! **" + invited.getName() + "** was invited!").queue();
            else channel.sendMessage(":x: **Error**! " + output.replace("{usage}", getUsage())).queue();
        }
        else if(args[0].equals("join")) {
            String output = TeamCommands.join(args, player);
            if(output == null) channel.sendMessage(":white_check_mark: **Success**! You have been added the team!").queue();
            else channel.sendMessage(":x: **Error**! " + output.replace("{usage}", getUsage())).queue();
        }
        else if(args[0].equals("delete")) {
            String output = TeamCommands.delete(args, player);
            if(output == null) channel.sendMessage(":white_check_mark: **Success**! Team was deleted!").queue();
            else channel.sendMessage(":x: **Error**! " + output.replace("{usage}", getUsage())).queue();
        }
    }

    public String getUsage() {
        return "**Usage**:" +
                "\n`" + Bot.prefix + "team` `create` `<material name>`" +
                "\n`" + Bot.prefix + "team` `invite` `<player>`" +
                "\n`" + Bot.prefix + "team` `join` `<team>`"  +
                "\n`" + Bot.prefix + "team` `leave` `<team>`"  +
                "\n`" + Bot.prefix + "team` `kick` `<player>`"  +
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
        return Permission.BAN_MEMBERS;
    }
}
