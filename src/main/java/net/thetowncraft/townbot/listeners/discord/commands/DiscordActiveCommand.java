package net.thetowncraft.townbot.listeners.discord.commands;

import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.listeners.minecraft.player_activity.active.ActivityManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class DiscordActiveCommand extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        if(args[0].equalsIgnoreCase(Bot.prefix + "active")) {

            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("Most Active Players");

            String activePlayers = "";

            int i = 1;
            for(Map.Entry<String, Long> entry : ActivityManager.sortedPlayerActivityMap().entrySet()) {
                activePlayers += i + ". " + Bukkit.getOfflinePlayer(UUID.fromString(entry.getKey())).getName() + " (" + entry.getValue() + " activity points)\n";
                i++;
            }
            if(i == 1) return;

            embed.setDescription(activePlayers);

            event.getChannel().sendMessage(embed.build()).queue();
        }
    }
}