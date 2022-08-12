package net.thetowncraft.townbot.listeners.discord.commands;

import net.dv8tion.jda.api.Permission;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.listeners.minecraft.player_activity.afk.active.ActivityManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.thetowncraft.townbot.util.Constants;
import org.bukkit.Bukkit;

import java.util.Map;
import java.util.UUID;

public class DiscordActiveCommand extends DiscordCommand {
    @Override
    public void execute(CommandEvent.Discord event) {
        if(ActivityManager.sortedPlayerActivityMap().size() == 0) {
            event.getChannel().sendMessage(":x: There are no active players this week!").queue();
            return;
        }
        event.getChannel().sendMessage(getActiveEmbed().build()).queue();
    }

    public static EmbedBuilder getActiveEmbed() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Most Active Players");
        embed.setColor(Constants.GREEN);

        String activePlayers = "";

        int i = 1;
        for(Map.Entry<String, Long> entry : ActivityManager.sortedPlayerActivityMap().entrySet()) {
            String name = Bukkit.getOfflinePlayer(UUID.fromString(entry.getKey())).getName();
            if(name == null) continue;

            activePlayers += i + ". " + name + " (" + entry.getValue() + " activity points)\n";
            i++;
        }

        embed.setDescription(activePlayers);
        return embed;
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