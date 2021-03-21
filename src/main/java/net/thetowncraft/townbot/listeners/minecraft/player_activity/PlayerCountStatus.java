package net.thetowncraft.townbot.listeners.minecraft.player_activity;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.managers.Presence;
import net.thetowncraft.townbot.Bot;
import org.bukkit.Bukkit;

public class PlayerCountStatus {

    /**
     * Updates the bot's status to show the amount of players online. This method is run every 500 ticks from net.thetow
     */
    public static void update() {
        int players = Bukkit.getOnlinePlayers().size();
        Presence presence = Bot.jda.getPresence();

        //We do this so that it never says 1 or 0 Players Online, we just set it to Playing The Town SMP (it looks better)
        if(players < 2) {
            presence.setActivity(Activity.playing("The Town SMP"));
        }
        else if(players == 1){
            presence.setActivity(Activity.playing("With 1 Player Online!"))
        }
        else {
            presence.setActivity(Activity.playing("With " + players + " Players Online!"));
        }
    }
}
