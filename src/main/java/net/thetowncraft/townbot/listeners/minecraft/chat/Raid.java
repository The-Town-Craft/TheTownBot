package net.thetowncraft.townbot.listeners.minecraft.chat;

import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.util.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.raid.RaidFinishEvent;
import org.bukkit.event.raid.RaidTriggerEvent;

import java.util.List;

public class Raid implements Listener {
    @EventHandler
    public void onRaid(RaidTriggerEvent event) {
        if(event.isCancelled()) {
            return;
        }
        Player player = event.getPlayer();
        String name = player.getName();
        TextChannel channel = Bot.jda.getTextChannelById(Constants.MC_CHAT);
        TextChannel logChannel = Bot.jda.getTextChannelById(Constants.MC_LOGS);
        EmbedBuilder embed = new EmbedBuilder();
        embed.setDescription("```css\n[" + name + " has triggered a raid]\n```");
        EmbedBuilder log = new EmbedBuilder();
        Location location = player.getLocation();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        embed.setColor(Constants.RED);
        log.setDescription("```css\n[" + name + " has triggered a raid]\n```");
        log.addField("X: " + x, "", false);
        log.addField("Y: " + y, "", false);
        log.setColor(Constants.RED);
        log.addField("Z: " + z, "", false);
        logChannel.sendMessage(log.build()).queue();
        channel.sendMessage(embed.build()).queue();
        Bukkit.getServer().broadcastMessage(ChatColor.RED + name + " has triggered a raid!");
    }

    @EventHandler
    public void onRaidComplete(RaidFinishEvent event) {
        List<Player> winners = event.getWinners();
        if(winners.size() == 0) {
            return;
        }
        StringBuilder players = new StringBuilder();
        if(winners.size() == 1) {
            players.append(winners.get(0).getName()).append(" ");
        }
        if(winners.size() != 1) {
            for (Player winner : winners) {
                players.append(winner.getName()).append(", ");
            }
        }
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Constants.GREEN);
        embed.setDescription("```\n" + players + "completed a raid\n```");
        TextChannel logChannel = Bot.jda.getTextChannelById(Constants.MC_LOGS);
        TextChannel channel = Bot.jda.getTextChannelById(Constants.MC_CHAT);
        logChannel.sendMessage(embed.build()).queue();
        channel.sendMessage(embed.build()).queue();
        Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "" + players + "completed a raid!");
    }
}
