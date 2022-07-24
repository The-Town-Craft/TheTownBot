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
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ModMail extends DiscordCommand {

    public static final List<String> INACTIVITY_MESSAGES = new ArrayList<>();

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
        player.sendMessage(ChatColor.AQUA + "Check your DMs, you have ModMail from The Town!");
    }

    public static String randomInactiveMessage() {
        Random random = new Random();
        int message = random.nextInt(INACTIVITY_MESSAGES.size());
        return INACTIVITY_MESSAGES.get(message);
    }

    public static void initActivityMessages() {
        INACTIVITY_MESSAGES.add("Heyo! Sorry to bother you, but we noticed you haven’t played on the server in a while. " +
                "We were just wondering if you had any plans to rejoin in the future? Thanks for reading, have a nice day! :)");

        INACTIVITY_MESSAGES.add("Howdy hey! Hope you have been doing well, sadly we noticed that you haven't been on the server recently. " +
                "We would love to see online again when you have the time, and we thank for the time you have already spent with us on the server! :D");

        INACTIVITY_MESSAGES.add("Well hello there you wonderful person! Wanna join us again for more fun times on the The Town SMP? Hope to see you online soon!");

        INACTIVITY_MESSAGES.add("Hey! We have noticed that you haven't played on our Minecraft Server in a while, and we understand; " +
                "You may have moved on to other servers or other games, but we would still love to have you join and have fun on our server. Chances are that we have added new features you will enjoy!");

        INACTIVITY_MESSAGES.add("Heyoooo we’ve noticed your inactivity on The Town Minecraft server could you perhaps be so kind as to hope on have chill with some peeps? " +
                "If not it’s no problem we’re just wondering! :)");

        INACTIVITY_MESSAGES.add("Yoo, you haven't come on The Town SMP for a while, if you want to come on and talk that would be cool! " +
                "If you ever find you have nothing else to do you could hop on and chat or whatever, we're here! Anyways, thanks for being part of the community!");

        INACTIVITY_MESSAGES.add("Ayo you haven't been on the server in a little bit, but it is a pretty cool place and finding more friends is always cool :) " +
                "Thanks for playing as much as you have tho, hope we've given you a fun time so far!");

        INACTIVITY_MESSAGES.add("Hey its The Staff form the Town SMP just here to say hey we miss you hanging out with us! " +
                "We know that life can be busy but if you ever have the time play some minecraft, chat, hangout, and hopefully you'll have a good as time as ever! :)");

        INACTIVITY_MESSAGES.add("Hey, what's up, how are you? We noticed that it's been some time since you've joined. " +
                "Don't worry, it's nothing serious, the staff just like to keep in touch. Thanks for your time.");

        INACTIVITY_MESSAGES.add("Yo what’s up? It’s been a while since you were on the server, and to be honest, your company is missed! " +
                "Do you have any plans to rejoin? We would love to see you on the server again!" +
                "\nFrom, The Town Staff Team");

        INACTIVITY_MESSAGES.add("Yoo, sorry to bother you! We noticed you haven’t been on The Town SMP recently, is there anything we could do to make it more enjoyable for you? " +
                "We constantly strive to create the best possible experience for everyone, and your input would be greatly appreciated! :)");

        INACTIVITY_MESSAGES.add("Heyy, how have you been. We were wondering if you would like to hop on The Town SMP and play or chat or whatever. " +
                "Also let us know if you have any suggestions on how to make the server more fun. " +
                "Your custom ideas could be implemented into the server. If you ever have a lack of something to do, jump on and talk to some old friends while playing Minecraft!");

        INACTIVITY_MESSAGES.add("Yo what’s up? Sorry to bother you, but we noticed it’s been a while since you logged on the server. " +
                "We miss playing Minecraft and chilling with you! I know you have a life, and we understand if you don’t have the time, but it would be really nice to play with you more often!\n" +
                "Sincerely, The Town Staff Team");
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