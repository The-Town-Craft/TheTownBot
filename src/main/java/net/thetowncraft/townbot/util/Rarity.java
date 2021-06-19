package net.thetowncraft.townbot.util;

import org.bukkit.ChatColor;

public enum Rarity {

    COMMON(ChatColor.WHITE),
    RARE(ChatColor.AQUA),
    EPIC(ChatColor.DARK_PURPLE),
    LEGENDARY(ChatColor.YELLOW);

    private final ChatColor color;

    Rarity(ChatColor color) {
        this.color = color;
    }

    public ChatColor getColor() {
        return color;
    }
}
