package net.thetowncraft.townbot.listeners.discord.commands;

import net.dv8tion.jda.api.Permission;
import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.listeners.minecraft.commands.MaintenanceCommand;
import net.thetowncraft.townbot.listeners.minecraft.player_activity.PlayerCountStatus;
import org.bukkit.ChatColor;

public class MaintenanceDiscord extends DiscordCommand {

    @Override
    public void execute(CommandEvent.Discord event) {
        Plugin.serverUnderMaintenance = !Plugin.serverUnderMaintenance;
        System.out.println("serverUnderMaintenance is now " + Plugin.serverUnderMaintenance);

        if(Plugin.serverUnderMaintenance) {
            event.getChannel().sendMessage("**Server is now under maintenance**. Only members with the developer role will be able to join.").queue();
            MaintenanceCommand.kickNonDevs();
        }
        else {
            event.getChannel().sendMessage("**Server has been re-opened**. Members without the developer role will once again be able to join.").queue();
        }
        PlayerCountStatus.update();
    }

    @Override
    public String getName() {
        return "maintenance";
    }

    @Override
    public String getDescription() {
        return "Turns maintenance off and on.";
    }

    @Override
    public Permission getRequiredPermission() {
        return Permission.ADMINISTRATOR;
    }
}
