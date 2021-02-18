package com.cadenkoehl.minecordbot.listeners.util;

import org.bukkit.OfflinePlayer;

public class SkinRender {
    public static String renderHead(OfflinePlayer player) {
        return "https://visage.surgeplay.com/head/500/" + player.getUniqueId().toString() + ".png";
    }
    public static String renderSkin(OfflinePlayer player) {
        return "https://visage.surgeplay.com/full/2000/" + player.getUniqueId().toString() + ".png";
    }
    public static String renderFace(OfflinePlayer player) {
        return "https://visage.surgeplay.com/head/500/" + player.getUniqueId().toString() + ".png";
    }
}