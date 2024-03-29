package net.thetowncraft.townbot.listeners.minecraft.chat;

import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.dimension.CelestialKingdomListener;
import net.thetowncraft.townbot.dimension.MysticRealmListener;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
import net.thetowncraft.townbot.listeners.accountlink.LinkAccount;
import net.thetowncraft.townbot.listeners.patches.Vanish;
import net.thetowncraft.townbot.util.Constants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class WorldChange implements Listener {
    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        World world = player.getLocation().getWorld();

        if(Vanish.isVanished(player)) return;

        if(!world.getName().equals(AccountManager.UNLINKED_DIMENSION.getWorld().getName())) {
            if(!AccountManager.getInstance().isLinked(player)) {
                player.teleport(AccountManager.UNLINKED_DIMENSION);
                return;
            }
        }

        if(world.getName().contains("nether")) {
            Bot.jda.getTextChannelById(Constants.MC_CHAT).sendMessage(">>> <:netherrack:818241306198409237> **" + name + "** entered the nether").queue();
            Bukkit.getServer().broadcastMessage(name + " entered the nether");
        }
        else if(world.getName().contains("end")) {
            Bot.jda.getTextChannelById(Constants.MC_CHAT).sendMessage(">>> <:enderman:818241348439113748> **" + name + "** entered the end").queue();
            Bukkit.getServer().broadcastMessage(name + " entered the end");
        }
        else if(world.getName().equals(Plugin.OVERWORLD_NAME)){
            if(event.getFrom().getName().equals(AccountManager.UNLINKED_DIMENSION.getWorld().getName())) return;
            Bot.jda.getTextChannelById(Constants.MC_CHAT).sendMessage(">>> <:mc_cow:818243740569305118> **" + name + "** returned to the overworld").queue();
            Bukkit.getServer().broadcastMessage(name + " returned to the overworld");
        }
        else if(world.getName().equals(MysticRealmListener.MYSTIC_REALM)) {
            Bot.jda.getTextChannelById(Constants.MC_CHAT).sendMessage(">>> <:mystic_artifact:990709236231188550> **" + name + "** entered the **Mystic Realm**!").queue();
            Bukkit.getServer().broadcastMessage(name + " entered the " + ChatColor.BOLD + ChatColor.DARK_PURPLE + "Mystic Realm");
        }
        else if(world.getName().equals(CelestialKingdomListener.CELESTIAL_KINGDOM)) {
            Bot.jda.getTextChannelById(Constants.MC_CHAT).sendMessage(">>> <:blaze_powder:1002487203940806726> **" + name + "** entered the **Celestial Kingdom**!").queue();
            Bukkit.getServer().broadcastMessage(name + " entered the " + ChatColor.BOLD + ChatColor.YELLOW + "Celestial Kingdom");
        }
    }
}
