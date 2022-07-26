package net.thetowncraft.townbot.listeners.discord.commands;

import net.dv8tion.jda.api.Permission;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.util.RepeatingTasks;

public class RewardActive extends DiscordCommand {

    @Override
    public void execute(CommandEvent.Discord event) {
        try {
            RepeatingTasks.rewardActivePlayers();
            event.getChannel().sendMessage(":white_check_mark: **Success**! Rewarded the most active players of the week!").queue();
        }
        catch(Exception ex) {
            event.getChannel().sendMessage(":x: **Error**! `" + ex.getMessage() + "`").queue();
        }
    }

    @Override
    public String getName() {
        return "rewardactive";
    }

    @Override
    public String getDescription() {
        return "Override weekly active player reward!";
    }

    @Override
    public Permission getRequiredPermission() {
        return Permission.BAN_MEMBERS;
    }
}
