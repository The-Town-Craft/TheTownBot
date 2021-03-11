package net.thetowncraft.townbot.listeners.minecraft.commands;

import net.thetowncraft.townbot.listeners.chatmute.MuteManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class McMute implements Listener {
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String[] args = event.getMessage().split("\\s+");
        Player player = event.getPlayer();
        if(args[0].equalsIgnoreCase("/mute")) {
            if(!player.isOp()) {
                player.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
                return;
            }
            if(args.length == 1) {
                player.sendMessage(ChatColor.RED + "Incomplete command! Usage: /mute [player]");
                return;
            }
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
            MuteManager manager = new MuteManager();

            if(manager.mutePlayer(offlinePlayer)) {
                player.sendMessage(ChatColor.GREEN + offlinePlayer.getName() + " was muted");
            }
            else {
                player.sendMessage(ChatColor.RED + offlinePlayer.getName() + " is already muted!");
            }
        }
    }
}