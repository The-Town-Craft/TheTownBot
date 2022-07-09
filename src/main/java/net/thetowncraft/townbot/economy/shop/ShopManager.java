package net.thetowncraft.townbot.economy.shop;

import java.util.ArrayList;
import java.util.List;

public class ShopManager {

    private static final List<ShopItem> ITEMS = new ArrayList<>();

    public static void initShop() {

    }

    public static void registerItem(ShopItem item) {
        ITEMS.add(item);
    }

    public static List<ShopItem> getItems() {
        return ITEMS;
    }
}
