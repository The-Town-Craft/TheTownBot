package net.thetowncraft.townbot.listeners.discord.commands;

import net.dv8tion.jda.api.Permission;
import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.util.Constants;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.stream.Collectors;

public class RunMCCommand extends DiscordCommand {
    @Override
    public void execute(CommandEvent.Discord event) {

        String command = Arrays.stream(event.getArgs()).skip(1).collect(Collectors.joining(" "));

        if(!event.getMember().getRoles().contains(Constants.DEV_ROLE)) {
            event.getChannel().sendMessage(":x: You must have the developer role to use this command!").queue();
            return;
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.get(), () -> {
            boolean success = Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);

            if(success) {
                event.getChannel().sendMessage(":white_check_mark: **Successfully executed command**!").queue();
            }
            else {
                event.getChannel().sendMessage(":x: **Unknown or incomplete command!**").queue();
            }
        });
    }

    @Override
    public String getName() {
        return "mccmd";
    }

    @Override
    public String getDescription() {
        return "Allows staff to run any Minecraft command from Discord!";
    }

    @Override
    public Permission getRequiredPermission() {
        return Permission.ADMINISTRATOR;
    }
}
