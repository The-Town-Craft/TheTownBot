package net.thetowncraft.townbot.listeners.patches;

import net.thetowncraft.townbot.listeners.minecraft.chat.MinecraftChatListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.metadata.MetadataValue;

public class Vanish implements Listener {

    @EventHandler
    public void onVanish(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if(event.getMessage().equals("/vanish")) {
            if(isVanished(player)) MinecraftChatListener.sendPlayerJoinMessage(player);
            else MinecraftChatListener.sendPlayerLeaveMessage(player);
        }
        if(event.getMessage().equals("/vanish off")) {
            if(isVanished(player)) MinecraftChatListener.sendPlayerJoinMessage(player);
        }
        if(event.getMessage().equals("/vanish on")) {
            if(!isVanished(player)) MinecraftChatListener.sendPlayerLeaveMessage(player);
        }
    }

    public static boolean isVanished(Player player) {
        for (MetadataValue meta : player.getMetadata("vanished")) {
            if (meta.asBoolean()) return true;
        }
        return false;
    }
}
