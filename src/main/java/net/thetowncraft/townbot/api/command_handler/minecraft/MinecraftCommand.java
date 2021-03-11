package net.thetowncraft.townbot.api.command_handler.minecraft;

import net.thetowncraft.townbot.api.command_handler.CommandConfig;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class MinecraftCommand {

    public static final List<MinecraftCommand> COMMANDS = new ArrayList<>();

    public static void registerCommands(MinecraftCommand... commands) {
        COMMANDS.addAll(Arrays.asList(commands));
    }

    /**
     * This method is called whenever the command is executed
     */
    public abstract void execute(CommandEvent.Minecraft event);

    /**
     * Should this command only be used by admins?
     * @return true if the command should only be used by admins
     */
    public abstract boolean isAdminCommand();

    public String getName() {

        for(Annotation annotation : this.getClass().getAnnotations()) {
            if(annotation instanceof CommandConfig) {
                CommandConfig cmd = (CommandConfig) annotation;
                return cmd.name().toLowerCase();
            }
        }
        throw new IllegalArgumentException(this.getClass().getName() + " must be annotated with @CommandConfig!");
    }
}
