package net.thetowncraft.townbot.listeners.minecraft.chat;

import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.util.Constants;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class WorldChange implements Listener {
    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        World world = event.getPlayer().getLocation().getWorld();

        if(world.getName().contains("nether")) {
            Bot.jda.getTextChannelById(Constants.MC_CHAT).sendMessage(">>> <:netherrack:818241306198409237> **" + event.getPlayer().getName() + "** has entered the nether").queue();
        }
        else if(world.getName().contains("end")) {
            Bot.jda.getTextChannelById(Constants.MC_CHAT).sendMessage(">>> <:enderman:818241348439113748> **" + event.getPlayer().getName() + "** has entered the end").queue();
        }
        else {
            Bot.jda.getTextChannelById(Constants.MC_CHAT).sendMessage(">>> <:mc_cow:818243740569305118> **" + event.getPlayer().getName() + "** has returned to the overworld").queue();
        }
    }
}
