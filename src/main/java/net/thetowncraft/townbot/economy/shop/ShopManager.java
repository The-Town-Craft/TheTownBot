package net.thetowncraft.townbot.economy.shop;

import net.dv8tion.jda.api.EmbedBuilder;
import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.util.Constants;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ShopManager {

    private static final List<ShopItem> ITEMS = new ArrayList<>();

    public static ShopItem getItemByName(String name) {
        for(ShopItem item : ITEMS) {
            if(item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    public static EmbedBuilder getShopEmbed() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.GREEN);
        embed.setAuthor("\uD83D\uDCB0 The Town Shop", Constants.THE_TOWN.getIconUrl());
        embed.setDescription("For roles, cosmetics, and more!\nType `" + Bot.prefix + "shop <item name>` for more details.\n----------------");
        for(ShopItem item : ITEMS) {
            embed.appendDescription("\n**" + item.getName() + "** (" + item.getPrice() + " coins)");
        }
        return embed;
    }

    public static ShopItem registerItem(ShopItem item) {
        ITEMS.add(item);
        return item;
    }

    public static List<ShopItem> getItems() {
        return ITEMS;
    }
}
