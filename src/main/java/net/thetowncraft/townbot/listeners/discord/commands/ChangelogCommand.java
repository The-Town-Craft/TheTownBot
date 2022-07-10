package net.thetowncraft.townbot.listeners.discord.commands;

import com.sun.org.apache.bcel.internal.Const;
import net.dv8tion.jda.api.Permission;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.util.Constants;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ChangelogCommand extends DiscordCommand {

    @Override
    public void execute(CommandEvent.Discord event) {
        String[] args = event.getArgs();
        if(args.length < 3) {
            event.getChannel().sendMessage(":x: Incorrect usage!").queue();
            return;
        }
        String patch = args[1];
        String messageRaw = Arrays.stream(args).skip(2).collect(Collectors.joining(" "));

        String[] changes = messageRaw.split("\\|");

        StringBuilder message = new StringBuilder();

        for(String change : changes) {
            message.append("- ").append(change).append("\n");
        }

        Constants.CHANGELOGS.sendMessage("**-------------------The Town Plugin - Patch " + patch + "-------------------**\n" +
                message +
                "------------------------------------------------------------------------ \n" +
                "[" + Constants.CHANGELOG_PING.getAsMention() + "]").queue();
    }

    @Override
    public String getName() {
        return "changelog";
    }

    @Override
    public String getDescription() {
        return "Add a new changelog!";
    }

    @Override
    public Permission getRequiredPermission() {
        return Permission.ADMINISTRATOR;
    }
}
