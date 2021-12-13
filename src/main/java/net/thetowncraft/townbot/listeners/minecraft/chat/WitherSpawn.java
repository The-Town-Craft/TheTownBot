package net.thetowncraft.townbot.listeners.minecraft.chat;

import net.thetowncraft.townbot.util.Constants;
import net.thetowncraft.townbot.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;


public class WitherSpawn implements Listener {
    @EventHandler
    public void onWither(CreatureSpawnEvent event) {
        if(event.getEntity().getWorld().getName().equals("world_1597802541_thetown_void")) return;
        if(event.getEntityType() == EntityType.WITHER) {
            Location pos = event.getLocation();
            int x = (int) pos.getX();
            int y = (int) pos.getY();
            int z = (int) pos.getZ();
            String biome = pos.getWorld().getBiome(x, y, z).getKey().getKey().replace("_", " ");

            TextChannel channel = Bot.jda.getTextChannelById(Constants.MC_CHAT);
            TextChannel logChannel = Bot.jda.getTextChannelById(Constants.MC_LOGS);
            EmbedBuilder embed = new EmbedBuilder();
            embed.setDescription("```css\n[A Wither has been summoned in a " + biome + " biome]\n```");
            embed.setColor(Constants.RED);
            channel.sendMessage(embed.build()).queue();
            logChannel.sendMessage(embed.build()).queue();
            Bukkit.getServer().broadcastMessage(ChatColor.RED + "A Wither has been summoned in a " + biome + " biome");
        }
    }
}
