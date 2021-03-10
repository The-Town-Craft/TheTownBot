package com.cadenkoehl.minecordbot.listeners.minecraft.player_activity.active;

import com.cadenkoehl.minecordbot.Plugin;
import com.cadenkoehl.minecordbot.listeners.util.Util;
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
        return Util.sortByValue(PLAYER_ACTIVITY_MAP);
    }

    public static Map<String, Long> get3MostActivePlayers() {
        Map<String, Long> players = new HashMap<>();

        int i = 1;
        for (Map.Entry<String, Long> entry : sortedPlayerActivityMap().entrySet()) {
            if(i > 3) break;
            players.put(entry.getKey(), entry.getValue());
            i++;
        }
        return players;
    }
}