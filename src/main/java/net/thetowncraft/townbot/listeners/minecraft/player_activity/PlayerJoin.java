package net.thetowncraft.townbot.listeners.minecraft.player_activity;

import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
import net.thetowncraft.townbot.util.Constants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        AccountManager manager = AccountManager.getInstance();
        Player player = event.getPlayer();
        if(manager.isLinked(player)) {
            return;
        }
        String password = manager.generatePassword(player);
        player.kickPlayer("Your account is not linked! Please DM TheTown bot on Discord with the password " + password + " to link your account!");
        Bukkit.getServer().broadcastMessage(ChatColor.RED + player.getName() + " tried to join, but their account is not linked!");
        Bot.jda.getTextChannelById(Constants.MC_LOGS).sendMessage(">>> :x: **" + player.getName() + "** tried to join but their account is not linked!").queue();
        Bot.jda.getTextChannelById(Constants.MC_CHAT).sendMessage(">>> :x: **" + player.getName() + "** tried to join but their account is not linked!").queue();
    }
}