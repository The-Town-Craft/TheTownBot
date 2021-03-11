package net.thetowncraft.townbot.listeners.minecraft.commands;

import net.thetowncraft.townbot.listeners.minecraft.player_activity.active.ActivityManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Map;
import java.util.UUID;

public class ActiveCommand implements Listener {
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if(event.getMessage().equals("/active")) {
            Player player = event.getPlayer();
            String activePlayers = ChatColor.DARK_PURPLE + "Most Active Players:\n" + ChatColor.RESET;

            int i = 1;
            for(Map.Entry<String, Long> entry : ActivityManager.sortedPlayerActivityMap().entrySet()) {
                activePlayers += i + ". " + ChatColor.AQUA + Bukkit.getOfflinePlayer(UUID.fromString(entry.getKey())).getName() + ChatColor.RESET + " (" + entry.getValue() + " activity points)\n";
                i++;
            }
            if(i != 1) player.sendMessage(activePlayers);
        }
    }
}
