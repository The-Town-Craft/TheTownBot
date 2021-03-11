package net.thetowncraft.townbot.api.command_handler.discord;

import net.dv8tion.jda.api.Permission;
import net.thetowncraft.townbot.api.command_handler.Command;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class DiscordCommand {

    public static final List<DiscordCommand> COMMANDS = new ArrayList<>();

    public static void registerCommands(DiscordCommand... commands) {
        COMMANDS.addAll(Arrays.asList(commands));
    }

    /**
     * This method is called whenever this command is run
     */
    public abstract void execute(CommandEvent.Discord event);

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

    public String getName() {

        for(Annotation annotation : this.getClass().getAnnotations()) {
            if(annotation instanceof Command) {
                Command cmd = (Command) annotation;
                return cmd.name();
            }
        }
        throw new IllegalArgumentException(this.getClass().getName() + " must be annotated with @Command!");
    }
}
