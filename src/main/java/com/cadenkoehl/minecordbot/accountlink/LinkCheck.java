package com.cadenkoehl.minecordbot.accountlink;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LinkCheck implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        AccountManager manager = new AccountManager();
        if(manager.isLinked(player)) {
            return;
        }
        String password = manager.generatePassword(player);
        player.kickPlayer("Your account is not linked! DM TheTown bot on Discord with the password " + password + " to link your account!");
    }
}