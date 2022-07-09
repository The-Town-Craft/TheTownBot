package net.thetowncraft.townbot.listeners.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
import net.thetowncraft.townbot.listeners.minecraft.player_activity.active.ActivityManager;
import net.thetowncraft.townbot.util.Constants;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ModMail extends DiscordCommand {
    @Override
    public void execute(CommandEvent.Discord event) {
        String[] args = event.getArgs();
        List<Message.Attachment> attachments = event.getMessage().getAttachments();

        if(args.length == 1) {
            event.getChannel().sendMessage(":x: Please specify a member to ModMail!").queue();
            return;
        }

        String message = Arrays.stream(args).skip(2).collect(Collectors.joining(" "));
        if(message.isEmpty()) {
            event.getChannel().sendMessage(":x: Please specify a message!").queue();
            return;
        }

        if(args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("everyone") || args[1].equalsIgnoreCase("@everyone")) {
            List<Member> members = event.getGuild().getMembers();

            event.getChannel().sendMessage("Preparing to ModMail" + event.getGuild().getMemberCount() + " members with message \"" + message + "\"").queue();

            for(Member member : members) {
                try {
                    if(member.getUser().isBot()) continue;
                    if(member.hasPermission(Permission.ADMINISTRATOR) && !member.isOwner()) continue;
                    sendModMail(member.getUser(), event.getGuild(), message, attachments);
                }
                catch(Exception ex) {
                    event.getChannel().sendMessage(":x: **Something went wrong while messaging " + member.getEffectiveName() + "!** " + ex.getMessage()).queue();
                }
            }

            event.getChannel().sendMessage(":white_check_mark: **Finished!**").queue();
            return;
        }

        try {

            Member member;

            List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
            if(mentionedMembers.size() != 0) {
                member = mentionedMembers.get(0);
            }
            else {
                member = AccountManager.getInstance().getDiscordMember(Bukkit.getOfflinePlayer(args[1]));
                if(member == null) {
                    event.getChannel().sendMessage(":x: " + args[1] + "'s account is either not linked, or the player does not exist.").queue();
                    return;
                }
            }

            sendModMail(member.getUser(), event.getGuild(), message, attachments);
        }
        catch(Exception ex) {
            event.getChannel().sendMessage(":x: **Something went wrong!** " + ex.getMessage()).queue();
            ex.printStackTrace();
        }
    }

    public static void sendModMail(User user, Guild guild, String message, List<Message.Attachment> attachments) {
        user.openPrivateChannel().queue((channel -> {

            EmbedBuilder embed = new EmbedBuilder();
            embed.setAuthor("ModMail from The Town!", null, guild.getIconUrl());
            embed.setDescription(message);
            embed.setColor(Constants.GREEN);

            channel.sendMessage(embed.build()).queue();

            for(Message.Attachment attachment : attachments) {
                String url = attachment.getUrl();
                channel.sendMessage(url).queue();
            }

        }));
        Bot.jda.getTextChannelById(Constants.MODMAIL).sendMessage("**Success!** ModMail was sent to **" + user.getAsTag() + "**!\nMessage: \"*" + message + "*\"").queue();
        Member member = guild.getMember(user);
        if(member == null) return;
        OfflinePlayer offPlayer = AccountManager.getInstance().getMinecraftPlayer(member);
        if(offPlayer == null) return;
        Player player = offPlayer.getPlayer();
        if(player == null) return;
        player.sendMessage(Color.AQUA + "Check your DMs, you have ModMail from The Town!");
    }

    @Override
    public String getName() {
        return "mm";
    }

    @Override
    public String getDescription() {
        return "ModMail a member!";
    }

    @Override
    public Permission getRequiredPermission() {
        return Permission.MESSAGE_MANAGE;
    }
}