package net.thetowncraft.townbot.listeners.minecraft.chat;

import net.thetowncraft.townbot.Plugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class RuleReminders implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if(event.getBlock().getType() == Material.DIAMOND_ORE) {
            if(!hasBeenRemindedAboutXray(player)) remindPlayerAboutXray(player);
        }
    }

    private boolean hasBeenRemindedAboutXray(Player player) {
        String uuid = player.getUniqueId().toString();
        File dir = new File(Plugin.get().getDataFolder().getPath(), "ruleReminders/xray/");
        if(dir.mkdirs()) System.out.println("Created directory " + dir.getPath());
        List<String> list = Arrays.asList(dir.list());
        return list.contains(uuid + ".txt");
    }

    private void remindPlayerAboutXray(Player player) {
        player.sendMessage(ChatColor.AQUA + "Just a friendly reminder: X-ray is not allowed on this server! If you are caught using it your inventory will be wiped and you will be tempbanned!");
        String uuid = player.getUniqueId().toString();
        File dir = new File(Plugin.get().getDataFolder().getPath(), "ruleReminders/xray/");
        File file = new File(dir, uuid + ".txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
