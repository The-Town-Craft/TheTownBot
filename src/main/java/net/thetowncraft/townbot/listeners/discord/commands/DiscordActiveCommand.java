package net.thetowncraft.townbot.listeners.discord.commands;

import net.dv8tion.jda.api.Permission;
import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.listeners.minecraft.player_activity.active.ActivityManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class DiscordActiveCommand extends DiscordCommand {
    @Override
    public void execute(CommandEvent.Discord event) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Most Active Players");

        String activePlayers = "";

        int i = 1;
        for(Map.Entry<String, Long> entry : ActivityManager.sortedPlayerActivityMap().entrySet()) {
            String name = Bukkit.getOfflinePlayer(UUID.fromString(entry.getKey())).getName();
            if(name == null) continue;
            
            activePlayers += i + ". " + name + " (" + entry.getValue() + " activity points)\n";
            i++;
        }
        if(i == 1) {
            event.getChannel().sendMessage(":x: There are currently no active players this week!").queue();
            return;
        }

        embed.setDescription(activePlayers);

        event.getChannel().sendMessage(embed.build()).queue();
    }

    @Override
    public String getName() {
        return "active";
    }

    @Override
    public String getDescription() {
        return "See a list of most active players!";
    }

    @Override
    public Permission getRequiredPermission() {
        return null;
    }
}