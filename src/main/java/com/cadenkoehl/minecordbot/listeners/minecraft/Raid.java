package com.cadenkoehl.minecordbot.listeners.minecraft;

import com.cadenkoehl.minecordbot.Constants;
import com.cadenkoehl.minecordbot.MinecordBot;
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
        int level = event.getRaid().getBadOmenLevel();
        TextChannel channel = MinecordBot.jda.getTextChannelById(Constants.chatLink);
        TextChannel logChannel = MinecordBot.jda.getTextChannelById(Constants.logChannel);
        EmbedBuilder embed = new EmbedBuilder();
        embed.setDescription("```css\n[" + name + " has triggered a level " + level + " raid]\n```");
        EmbedBuilder log = new EmbedBuilder();
        Location location = player.getLocation();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        log.setDescription("```css\n[" + name + " has triggered a level " + level + " raid]\n```");
        log.addField("X: " + x, "", false);
        log.addField("Y: " + y, "", false);
        log.addField("Z: " + z, "", false);
        logChannel.sendMessage(log.build()).queue();
        channel.sendMessage(embed.build()).queue();
        Bukkit.getServer().broadcastMessage(ChatColor.RED + name + " has triggered a level " + level + " raid!");
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
            for(int i = 0; i < winners.size(); i++) {
                players.append(winners.get(i).getName() + ", ");
            }
        }
        EmbedBuilder embed = new EmbedBuilder();
        embed.setDescription("```css\n[" + players + "completed a raid!");
        TextChannel logChannel = MinecordBot.jda.getTextChannelById(Constants.logChannel);
        TextChannel channel = MinecordBot.jda.getTextChannelById(Constants.chatLink);
        logChannel.sendMessage(embed.build()).queue();
        channel.sendMessage(embed.build()).queue();
        Bukkit.getServer().broadcastMessage(players + "completed a raid!");
    }
}
