package net.thetowncraft.townbot.listeners.minecraft.player_activity;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.managers.Presence;
import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.util.Utils;
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
        Collection<? extends Player> onlinePlayers = Utils.getEffectiveOnlinePlayers();
        int players = onlinePlayers.size();
        Presence presence = Bot.jda.getPresence();

        if(players == 0) {
            presence.setActivity(Activity.playing("The Town SMP"));
        }
        if(players == 1) {
            presence.setActivity(Activity.playing("1 Player Online!"));
        }
        if(players > 1) {
            presence.setActivity(Activity.playing(players + " Players Online!"));
        }
    }
}
