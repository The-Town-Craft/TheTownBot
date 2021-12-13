package net.thetowncraft.townbot.economy.player_shops;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerShop {

    private final String playerUUID;
    private final Vector pos;
    private final int price;
    private final boolean perStack;

    public PlayerShop(String playerUUID, Vector pos, int price, boolean perStack) {
        this.playerUUID = playerUUID;
        this.pos = pos;
        this.price = price;
        this.perStack = perStack;
    }

    public String getPlayerUUID() {
        return playerUUID;
    }

    public Vector getPos() {
        return pos;
    }

    public int getPrice() {
        return price;
    }

    public boolean isPerStack() {
        return perStack;
    }

    public OfflinePlayer getOwner() {
        return Bukkit.getOfflinePlayer(UUID.fromString(playerUUID));
    }
}
