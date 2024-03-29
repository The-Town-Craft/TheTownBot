package net.thetowncraft.townbot.listeners.minecraft.player_activity.active;

import net.dv8tion.jda.api.entities.Member;
import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
import net.thetowncraft.townbot.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

public class ActivityManager {

    public static final Map<String, Long> PLAYER_ACTIVITY_MAP = new HashMap<>();

    public static int getActivityPoints(OfflinePlayer player) {
        Long activityLong = PLAYER_ACTIVITY_MAP.get(player.getUniqueId().toString());
        if(activityLong == null) return 0;

        return activityLong.intValue();
    }

    /**
     * Loads the activity points of a player, from their file
     */
    public static void loadActivityPoints() {
        File dataFolder = Plugin.get().getDataFolder();

        File dir = new File(dataFolder, "activity/");

        if(dir.mkdirs()) System.out.println(dir.getPath() + " was successfully created!");

        for(String fileName : dir.list()) {
            if(fileName.equalsIgnoreCase(".DS_Store")) continue;

            OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(fileName.replace(".txt", "")));

            File file = new File(dir, fileName);
            try {
                Scanner scan = new Scanner(file);
                PLAYER_ACTIVITY_MAP.put(player.getUniqueId().toString(), scan.nextLong());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static Map<String, Long> sortedPlayerActivityMap() {
        return Utils.sortByLongValue(PLAYER_ACTIVITY_MAP);
    }

    public static Map<String, Long> get3MostActivePlayers() {
        Map<String, Long> players = new HashMap<>();

        int i = 1;
        for (Map.Entry<String, Long> entry : sortedPlayerActivityMap().entrySet()) {
            if(i > 3) break;

            Member member = AccountManager.getInstance().getDiscordMember(Bukkit.getOfflinePlayer(UUID.fromString(entry.getKey())));
            if(member == null) continue;
            players.put(entry.getKey(), entry.getValue());
            i++;
        }
        return players;
    }
}