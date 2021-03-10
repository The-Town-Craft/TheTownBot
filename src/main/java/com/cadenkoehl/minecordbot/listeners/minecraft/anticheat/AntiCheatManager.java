package com.cadenkoehl.minecordbot.listeners.minecraft.anticheat;

import com.cadenkoehl.minecordbot.Bot;
import com.cadenkoehl.minecordbot.Plugin;
import com.cadenkoehl.minecordbot.listeners.util.Constants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.block.Block;

import java.io.File;
import java.util.*;

public class AntiCheatManager {

    private static final Map<Player, Integer> playerXrayMap = new HashMap<>();

    private AntiCheatManager() {}

    public static final AntiCheatManager INSTANCE = new AntiCheatManager();

    public void blockBroke(Player player, Block block) {
        File dataFolder = Plugin.get().getDataFolder();

        int blocksMinedBeforeAlarm;

        if(block.getType() == Material.DIAMOND_ORE) blocksMinedBeforeAlarm = 10;
        else if(block.getType() == Material.ANCIENT_DEBRIS) blocksMinedBeforeAlarm = 5;
        else throw new IllegalArgumentException("Block type must be either Ancient Debris or Diamond Ore!");

        File dir = new File(dataFolder, "xray_detection/");

        if (dir.mkdirs()) System.out.println(dir.getPath() + " was created!");

        File file = new File(dir, player.getUniqueId().toString() + ".txt");

        Timer timer = new Timer();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Integer blocksMined = playerXrayMap.get(player);
                if(blocksMined == null) {
                    playerXrayMap.put(player, 1);
                    return;
                }
                if(blocksMined == blocksMinedBeforeAlarm) {
                    Constants.MOD_CHAT.sendMessage("<@&793254607526428673> " + player.getName() + " may be X-raying! They mined " + blocksMinedBeforeAlarm + " " + block.getType().name().replace("_", " ") + " in only 10 minutes!").queue();
                    Bukkit.getServer().broadcastMessage(ChatColor.AQUA + "[SERVER] " + ChatColor.RESET + "Just a friendly reminder: X-raying is not allowed on this server, and will be punished with an inventory wipe & tempban!");
                }
            }
        };

        timer.schedule(timerTask, 5 * 1000 * 60);
    }
}
