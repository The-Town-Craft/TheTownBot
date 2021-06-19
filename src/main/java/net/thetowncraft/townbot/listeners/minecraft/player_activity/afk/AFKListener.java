package net.thetowncraft.townbot.listeners.minecraft.player_activity.afk;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class AFKListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        AFKManager.AFK_PLAYER_TICKS.remove(event.getPlayer());
        AFKManager.setAFK(event.getPlayer(), false);
    }
}

