package net.thetowncraft.townbot.listeners.discord.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.util.Constants;
import net.thetowncraft.townbot.util.data.Data;

import java.io.File;
import java.util.List;

public class TodoCommand extends DiscordCommand {

    @Override
    public void execute(CommandEvent.Discord event) {
        String[] args = event.getArgs();
        TextChannel channel = event.getChannel();
        Member member = event.getMember();

        if(!member.getRoles().contains(Constants.DEV_ROLE)) {
            channel.sendMessage(":x: **Error**! You must be a developer to use this command! Type \"-apply developer\" to apply!").queue();
            return;
        }
        if(args.length < 3) {
            channel.sendMessage("**-----TODO LIST-----**" + getTodoList()).queue();
            return;
        }

        if(args[2].equalsIgnoreCase("add")) {

        }
        if(args[2].equalsIgnoreCase("remove")) {

        }
    }

    public static String getTodoList() {
        File file = Data.getFile("todo.txt");
        if(file == null) return "\nLooks like you're all caught up!";

        List<String> strings = Data.getStringsFromFile(file);
        if(strings == null) return "\nLooks like you're all caught up!";

        if(strings.size() == 0) return "\nLooks like you're all caught up!";

        String str = "";
        int i = 1;
        for(String item : strings) {
            str = str + "\n" + i + ". " + item;
        }
        return str;
    }

    @Override
    public String getName() {
        return "todo";
    }

    @Override
    public String getDescription() {
        return "Utility for developers!";
    }

    @Override
    public Permission getRequiredPermission() {
        return null;
    }
}
