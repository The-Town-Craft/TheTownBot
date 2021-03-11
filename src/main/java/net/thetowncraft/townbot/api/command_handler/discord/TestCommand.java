package net.thetowncraft.townbot.api.command_handler.discord;

import net.thetowncraft.townbot.api.command_handler.CommandConfig;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;

@CommandConfig(name = "test")
public class TestCommand extends DiscordCommand {

    @Override
    public void execute(CommandEvent.Discord event) {
        event.getChannel().sendMessage("test!").queue();
    }

    @Override
    public String getDescription() {
        return "A test command!";
    }
}
