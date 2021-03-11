package net.thetowncraft.townbot.api.command_handler.minecraft;

public abstract class MinecraftCommand {

    /**
     * This method is called whenever the command is executed
     */
    public abstract void execute();

    /**
     * Should this command only be used by admins?
     * @return true if the command should only be used by admins
     */
    public abstract boolean isAdminCommand();
}
