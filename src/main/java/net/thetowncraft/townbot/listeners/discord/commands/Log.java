package net.thetowncraft.townbot.listeners.discord.commands;

import net.dv8tion.jda.api.Permission;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.util.data.Data;

import java.io.File;

public class Log extends DiscordCommand {
    @Override
    public void execute(CommandEvent.Discord event) {
        String[] args = event.getArgs();

        if(args.length == 1) {
            try {
                File file = Data.getLogFile();
                event.getChannel().sendMessage("Here is the compiled log file from " + file.getName().replace(".txt", "")).addFile(file).queue();
            }
            catch (Exception ex) {
                ex.printStackTrace();
                event.getChannel().sendMessage(":x: An error has occurred: `" + ex + "`").queue();
            }
            return;
        }
        try {
            String fileName = args[1].replace(".txt", "");
            File file = Data.getLogFile(fileName);
            if(!file.exists()) {
                event.getChannel().sendMessage(":x: Log file was not found!").queue();
                return;
            }
            event.getChannel().sendMessage("Here is the compiled log file from " + fileName).addFile(file).queue();
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