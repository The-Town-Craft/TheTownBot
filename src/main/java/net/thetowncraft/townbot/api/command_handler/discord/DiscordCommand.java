package net.thetowncraft.townbot.api.command_handler.discord;

import java.security.Permission;

public abstract class DiscordCommand {

    /**
     * This method is called whenever this command is run
     */
    public abstract void execute();

    /**
     * @return The description of this command, showed in /help
     */
    public abstract String getDescription();

    /**
     * @return The Permission a member needs to be able to use this command, null if this command can be used by anyone.
     */
    public Permission getRequiredPermission() {
        return null;
    }
}
