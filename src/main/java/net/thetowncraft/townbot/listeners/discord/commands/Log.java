package net.thetowncraft.townbot.listeners.discord.commands;

import net.thetowncraft.townbot.Bot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import org.bukkit.command.Command;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class Log extends DiscordCommand {
    @Override
    public void execute(CommandEvent.Discord event) {
        String[] args = event.getArgs();

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

    @Override
    public String getName() {
        return "log";
    }

    @Override
    public String getDescription() {
        return "View the log file!";
    }

    @Override
    public Permission getRequiredPermission() {
        return Permission.BAN_MEMBERS;
    }
}