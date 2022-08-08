package net.thetowncraft.townbot.listeners.minecraft.player_activity;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.Presence;
import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.listeners.minecraft.commands.MaintenanceCommand;
import net.thetowncraft.townbot.util.Constants;
import net.thetowncraft.townbot.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PlayerCountStatus implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        update();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        update();
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        update();
    }

    /**
     * Updates the bot's status to show the amount of players online. This method is run every 500 ticks
     */
    public static void update() {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Plugin.get(), () -> {
            List<Player> onlinePlayers = Utils.getEffectiveOnlinePlayers();
            int players = onlinePlayers.size();
            Presence presence = Bot.jda.getPresence();

            if(Plugin.serverUnderMaintenance) {
                presence.setStatus(OnlineStatus.DO_NOT_DISTURB);
                presence.setActivity(Activity.playing("Under Maintenance"));
                Constants.PLAYER_COUNT_CHANNEL.getManager().setName("Under Maintenance").queue();
                return;
            }

            presence.setStatus(OnlineStatus.ONLINE);

            if(players == 0) {
                presence.setActivity(Activity.playing("The Town SMP"));
                Constants.PLAYER_COUNT_CHANNEL.getManager().setName("Server Online").queue();
            }
            if(players == 1) {
                presence.setActivity(Activity.playing("1 Player Online!"));
                Constants.PLAYER_COUNT_CHANNEL.getManager().setName(onlinePlayers.get(0).getName() + " is Online").queue();
            }
            if(players > 1) {
                presence.setActivity(Activity.playing(players + " Players Online!"));
                Constants.PLAYER_COUNT_CHANNEL.getManager().setName(players + " Players Online").queue();
            }
        }, 20);
    }
}
