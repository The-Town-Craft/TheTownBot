package net.thetowncraft.townbot.economy.player_shops;

import javafx.scene.paint.Material;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;

public class PlayerShopManager {

    private static final List<PlayerShop> SHOPS = new ArrayList<>();

    public void addShop(OfflinePlayer player, Material material, int pricePerItem) {
        addShop(new PlayerShop(player.getUniqueId().toString(), material, pricePerItem));
    }

    public void addShop(PlayerShop shop) {
        SHOPS.add(shop);
    }

    public void removeShop(PlayerShop shop) {
        SHOPS.remove(shop);
    }

    public static List<PlayerShop> getShops() {
        return SHOPS;
    }
}
