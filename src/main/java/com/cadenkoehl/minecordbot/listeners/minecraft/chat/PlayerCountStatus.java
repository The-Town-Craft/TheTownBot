package com.cadenkoehl.minecordbot.listeners.minecraft.chat;

import com.cadenkoehl.minecordbot.Bot;
import net.dv8tion.jda.api.entities.Activity;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerCountStatus implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        int players = Bukkit.getServer().getOnlinePlayers().size();

        if (players < 2) {
            Bot.jda.getPresence().setActivity(Activity.playing("The Town SMP!"));
        }
        else {
            Bot.jda.getPresence().setActivity(Activity.playing(players + " Players Online!"));
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        int players = Bukkit.getServer().getOnlinePlayers().size();

        if (players < 2) {
            Bot.jda.getPresence().setActivity(Activity.playing("The Town SMP!"));
        }
        else {
            Bot.jda.getPresence().setActivity(Activity.playing(players + " Players Online!"));
        }
    }
}
