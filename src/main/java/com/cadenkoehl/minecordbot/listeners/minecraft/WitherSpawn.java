package com.cadenkoehl.minecordbot.listeners.minecraft;

import com.cadenkoehl.minecordbot.Constants;
import com.cadenkoehl.minecordbot.MinecordBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;


public class WitherSpawn implements Listener {
    @EventHandler
    public void onWither(CreatureSpawnEvent event) {
        if(event.getEntityType() == EntityType.WITHER) {
            Location pos = event.getLocation();
            int x = (int) pos.getX();
            int y = (int) pos.getY();
            int z = (int) pos.getZ();
            String biome = pos.getWorld().getBiome(x, y, z).getKey().getKey().replace("_", " ");

            TextChannel channel = MinecordBot.jda.getTextChannelById(Constants.chatLink);
            TextChannel logChannel = MinecordBot.jda.getTextChannelById(Constants.logChannel);
            EmbedBuilder embed = new EmbedBuilder();
            embed.setDescription("```css\n[A Wither has been summoned in a " + biome + " biome]\n```");

        }
    }
}
