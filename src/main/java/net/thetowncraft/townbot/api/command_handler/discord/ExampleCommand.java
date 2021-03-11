package net.thetowncraft.townbot.api.command_handler.discord;

import net.dv8tion.jda.api.Permission;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;

/**
 * First, make a class and make it extend DiscordCommand (do this in listeners/discord package)
 */
public class ExampleCommand extends DiscordCommand {

    //The code inside this method will run when this command is used!
    @Override
    public void execute(CommandEvent.Discord event) {
        event.getChannel().sendMessage("test!").queue();
    }

    //This is the name of the command/what the member has to type to run it
    @Override
    public String getName() {
        return "test";
    }

    //The description of the command that will be showed in /help
    @Override
    public String getDescription() {
        return "A test command!";
    }

    //The permission a member needs to use this command, if they dont need a permission, return null
    @Override
    public Permission getRequiredPermission() {
        return Permission.ADMINISTRATOR;
    }
}
