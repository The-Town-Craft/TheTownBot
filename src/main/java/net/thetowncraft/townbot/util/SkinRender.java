package net.thetowncraft.townbot.util;

import org.bukkit.OfflinePlayer;

public class SkinRender {

    /**
     * @param player The player who's head you want to render
     * @return An image URL of the rendered head
     */
    public static String renderHead(OfflinePlayer player) {
        return "https://visage.surgeplay.com/head/500/" + player.getUniqueId().toString() + ".png";
    }

    /**
     * @param player The player who's skin you want to render
     * @return An image URL of the rendered skin
     */
    public static String renderSkin(OfflinePlayer player) {
        return "https://visage.surgeplay.com/full/2000/" + player.getUniqueId().toString() + ".png";
    }

    /**
     * @param player The player who's face you want to render
     * @return An image URL of the rendered face
     */
    public static String renderFace(OfflinePlayer player) {
        return "https://visage.surgeplay.com/face/500/" + player.getUniqueId().toString() + ".png";
    }
}