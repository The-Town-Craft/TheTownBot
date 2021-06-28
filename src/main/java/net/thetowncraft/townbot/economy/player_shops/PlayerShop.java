package net.thetowncraft.townbot.economy.player_shops;

import javafx.scene.paint.Material;

public class PlayerShop {

    private final String playerUUID;
    private final Material item;
    private int pricePerItem;

    public PlayerShop(String playerUUID, Material item, int pricePerItem) {
        this.playerUUID = playerUUID;
        this.item = item;
        this.pricePerItem = pricePerItem;
    }

    public String getPlayerUUID() {
        return playerUUID;
    }

    public Material getItem() {
        return item;
    }

    public int getPricePerItem() {
        return pricePerItem;
    }

    public void setPricePerItem(int pricePerItem) {
        this.pricePerItem = pricePerItem;
    }
}
