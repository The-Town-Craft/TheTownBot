package com.cadenkoehl.minecordbot.listeners.chatmute;

import com.cadenkoehl.minecordbot.MinecordBot;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class MuteManager {
    public boolean mutePlayer(@NotNull OfflinePlayer player) {
        MinecordBot plugin = MinecordBot.getPlugin(MinecordBot.class);
        File dir = new File(plugin.getDataFolder() + "/muted-players/");
        if(dir.mkdirs()) {
            System.out.println(dir.getPath() + " was successfully created!");
        }
        File file = new File(dir, player.getUniqueId().toString());
        if(file.exists()) {
            return false;
        }
        try {
            if(file.createNewFile()) {
                System.out.println("Successfully created file " + file.getPath());
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean unmutePlayer(@NotNull OfflinePlayer player) {
        MinecordBot plugin = MinecordBot.getPlugin(MinecordBot.class);
        File dir = new File(plugin.getDataFolder() + "/muted-players/");
        if(dir.mkdirs()) {
            System.out.println(dir.getPath() + " was successfully created!");
        }
        File file = new File(dir, player.getUniqueId().toString());
        if(!file.exists()) {
            return false;
        }
        if(file.delete()) {
            System.out.println("Successfully deleted file " + file.getPath());
        }
        return true;
    }
    public boolean isMuted(@NotNull Player player) {
        MinecordBot plugin = MinecordBot.getPlugin(MinecordBot.class);
        File dir = new File(plugin.getDataFolder() + "/muted-players/");
        if(dir.mkdirs()) {
            System.out.println(dir.getPath() + " was successfully created!");
        }
        File file = new File(dir, player.getUniqueId().toString());
        return file.exists();
    }
}