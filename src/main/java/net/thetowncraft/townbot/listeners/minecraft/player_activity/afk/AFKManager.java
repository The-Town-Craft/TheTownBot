package net.thetowncraft.townbot.listeners.minecraft.player_activity.afk;

import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.util.Constants;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AFKManager {

    public static final Map<Player, Long> AFK_PLAYER_TICKS = new HashMap<>();
    public static final int TIME_UNTIL_AFK = 5;



    public static void setAFK(Player player, boolean afk) {

        File dataFolder = Plugin.get().getDataFolder();

        File dir = new File(dataFolder, "afk_players/");

        if(dir.mkdirs()) System.out.println(dir.getPath() + " was created!");

        File file = new File(dir, player.getUniqueId() + ".txt");

        try {
            if (afk) {
                if(isAFK(player)) return;
                if (file.createNewFile()) System.out.println(file.getPath() + " was successfully created!");
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tab player " + player.getName() + " tabprefix  &c[AFK] &7");
                Bukkit.getServer().broadcastMessage(player.getName() + " is now AFK");
                Bot.jda.getTextChannelById(Constants.MC_CHAT).sendMessage(">>> :zzz: **" + player.getName() + "** is now AFK").queue();
            }

            if (!afk) {
                if(!isAFK(player)) return;
                if(file.delete()) System.out.println(file.getPath() + " was successfully deleted!");
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tab player " + player.getName() + " tabprefix");
                Bukkit.getServer().broadcastMessage(player.getName() + " returned from AFK");
                Bot.jda.getTextChannelById(Constants.MC_CHAT).sendMessage(">>> :zzz: **" + player.getName() + "** returned from AFK").queue();
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static boolean isAFK(Player player) {
        File dataFolder = Plugin.get().getDataFolder();

        File dir = new File(dataFolder, "afk_players/");
        File file = new File(dir, player.getUniqueId().toString() + ".txt");

        return file.exists();
    }
}

