package net.thetowncraft.townbot.listeners.discord.commands;

import net.dv8tion.jda.api.Permission;
import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.util.data.Data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LogSearch extends DiscordCommand {

    @Override
    public void execute(CommandEvent.Discord event) {
        String[] args = event.getArgs();
        if(args.length == 1) {
            event.getChannel().sendMessage("What would you like to search the logs for?").queue();
            return;
        }

        String query = Arrays.stream(args).skip(1).collect(Collectors.joining(" "));
        event.getChannel().sendMessage("Searching the logs for \"" + query + "\"...").complete();

        File dir = Data.getLogDir();
        File[] files = dir.listFiles();
        event.getChannel().sendMessage("Retrieving log files...").complete();
        if(files == null) {
            event.getChannel().sendMessage(":x: **Error**! Log files not found!").queue();
            return;
        }

        event.getChannel().sendMessage("Found " + files.length + " log files.").complete();

        File resultFile = new File(Plugin.get().getDataFolder(), "log/search/" + System.currentTimeMillis() + ".txt");

        if(resultFile.exists()) resultFile.delete();

        event.getChannel().sendMessage("Searching all " + files.length + " log files for \"" + query + "\"...").complete();

        try {
            int i = 0;
            FileWriter writer = new FileWriter(resultFile);
            writer.write("**All results for search query \"" + query + "\" (searched by " + event.getMember().getEffectiveName() + ")**");
            for(File file : files) {
                for(String line : Data.getStringsFromFile(file)) {
                    if(line.toLowerCase().contains(query.toLowerCase())) {
                        writer.append("\n").append(line);
                        i++;
                    }
                }
            }
            writer.close();
            event.getChannel().sendMessage("Searched all " + files.length + " files, found " + i + " results.").complete();
            event.getChannel().sendMessage("Saving all " + i + " results to a text file...").complete();
            event.getChannel().sendMessage(":white_check_mark: **Success**!").addFile(resultFile).queue();
        } catch (IOException e) {
            event.getChannel().sendMessage(":x: **Error**! " + e.getMessage()).queue();
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "searchlogs";
    }

    @Override
    public String getDescription() {
        return "Search the log files.";
    }

    @Override
    public Permission getRequiredPermission() {
        return Permission.BAN_MEMBERS;
    }
}
