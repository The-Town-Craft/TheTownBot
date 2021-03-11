package net.thetowncraft.townbot.listeners.discord.commands;

import net.dv8tion.jda.api.Permission;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.listeners.minecraft.player_activity.RepeatingTasks;

public class ResetActivity extends DiscordCommand {
    @Override
    public void execute(CommandEvent.Discord event) {
        RepeatingTasks.resetActivity();
        event.getChannel().sendMessage(":white_check_mark: **Success**! Reset player activity points!").queue();
    }

    @Override
    public String getName() {
        return "resetactivity";
    }

    @Override
    public String getDescription() {
        return "Reset the player activity points!";
    }

    @Override
    public Permission getRequiredPermission() {
        return Permission.ADMINISTRATOR;
    }
}
