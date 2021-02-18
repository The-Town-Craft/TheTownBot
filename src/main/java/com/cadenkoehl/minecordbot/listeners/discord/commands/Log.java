package com.cadenkoehl.minecordbot.listeners.discord.commands;

import com.cadenkoehl.minecordbot.MinecordBot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class Log extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        if(args[0].equalsIgnoreCase(MinecordBot.prefix + "log")) {
            if(event.isWebhookMessage()) {
                return;
            }
            Member member = event.getMember();
            if(member.getUser().isBot()) {
                return;
            }
            if(!member.hasPermission(Permission.BAN_MEMBERS)) {
                event.getChannel().sendMessage(":x: You can't use that!").queue();
                return;
            }
            if(args.length == 1) {
                try {
                    File dir = new File("plugins/DetailedLogs/compiled-log");
                    String[] paths = dir.list();
                    String path = dir + "/" + paths[(paths.length - 1)];
                    File file = new File(path);
                    event.getChannel().sendMessage("Here is the compiled log file from " + file.getName().replace(".txt", "")).addFile(file).queue();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    event.getChannel().sendMessage(":x: An error has occurred: `" + ex + "`").queue();
                }
                return;
            }
            try {
                File dir = new File("plugins/DetailedLogs/compiled-log");
                File file = new File(dir,args[1] + ".txt");
                if(!file.exists()) {
                    event.getChannel().sendMessage(":x: Log file was not found!").queue();
                    return;
                }
                event.getChannel().sendMessage("Here is the compiled log file from " + args[1]).addFile(file).queue();
            }
            catch (Exception ex) {
                ex.printStackTrace();
                event.getChannel().sendMessage(":x: An error has occurred: `" + ex + "`").queue();
            }
        }
    }
}