package net.thetowncraft.townbot.listeners.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;

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
                    member.getUser().openPrivateChannel().queue((channel -> {

                        EmbedBuilder embed = new EmbedBuilder();
                        embed.setAuthor("ModMail from The Town!", null, event.getGuild().getIconUrl());
                        embed.setDescription(message);

                        channel.sendMessage(embed.build()).queue();

                        for(Message.Attachment attachment : attachments) {
                            String url = attachment.getUrl();
                            channel.sendMessage(url).queue();
                        }

                        event.getChannel().sendMessage("**Success!** ModMail was sent to **" + member.getUser().getAsTag() + "**!").queue();
                    }));
                }
                catch(Exception ex) {
                    event.getChannel().sendMessage(":x: **Something went wrong while messaging " + member.getEffectiveName() + "!** " + ex.getMessage()).queue();
                }
            }

            event.getChannel().sendMessage(":white_check_mark: **Finished!**").queue();
            return;
        }

        try {
            List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
            if(mentionedMembers.size() == 0) {
                event.getChannel().sendMessage(":x: Please specify a member to ModMail!").queue();
                return;
            }
            Member member = mentionedMembers.get(0);
            member.getUser().openPrivateChannel().queue((channel -> {

                EmbedBuilder embed = new EmbedBuilder();
                embed.setAuthor("ModMail from The Town!", null, event.getGuild().getIconUrl());
                embed.setDescription(message);

                channel.sendMessage(embed.build()).queue();

                for(Message.Attachment attachment : attachments) {
                    String url = attachment.getUrl();
                    channel.sendMessage(url).queue();
                }

            }));
            event.getChannel().sendMessage("**Success!** ModMail was sent to **" + member.getUser().getAsTag() + "**!\nMessage: \"*" + message + "*\"").queue();
        }
        catch(Exception ex) {
            event.getChannel().sendMessage(":x: **Something went wrong!** " + ex.getMessage()).queue();
            ex.printStackTrace();
        }
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
        return Permission.BAN_MEMBERS;
    }
}
