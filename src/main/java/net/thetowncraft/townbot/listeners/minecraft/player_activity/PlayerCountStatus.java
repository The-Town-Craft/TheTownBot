package net.thetowncraft.townbot.listeners.minecraft.player_activity;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.managers.Presence;
import net.thetowncraft.townbot.Bot;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PlayerCountStatus {

    /**
     * Updates the bot's status to show the amount of players online. This method is run every 500 ticks
     */
    public static void update() {
        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        int players = onlinePlayers.size();
        Presence presence = Bot.jda.getPresence();

        if(players == 0) {
            presence.setActivity(Activity.playing("The Town SMP"));
        }
        if(players == 1) {
            List<Player> playerList = new ArrayList<>(onlinePlayers);
            presence.setActivity(Activity.playing("with " + playerList.get(0).getName()));
        }
        if(players > 1) {
            presence.setActivity(Activity.playing(players + " Players Online!"));
        }
    }
}
