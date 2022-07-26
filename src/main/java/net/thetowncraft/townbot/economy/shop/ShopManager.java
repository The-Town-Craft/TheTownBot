package net.thetowncraft.townbot.economy.shop;

import net.dv8tion.jda.api.EmbedBuilder;
import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.util.Constants;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ShopManager {

    private static final List<ShopItem> ITEMS = new ArrayList<>();

    public static void initShop() {
        registerItem(new ShopItem("Test", "Does literally nothing",
                "https://cdn.discordapp.com/attachments/997986798254948392/997988512534110269/unknown.png",
                10, "1001355803313315921"));
    }

    public static EmbedBuilder getShopEmbed() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.YELLOW);
        embed.setAuthor("\uD83D\uDCB0 **The Town Shop**", Constants.THE_TOWN.getIconUrl());
        embed.setDescription("For roles, cosmetics, and more!\nType \"*" + Bot.prefix + "shop <item name>*\" for more details.");
        for(ShopItem item : ITEMS) {
            embed.appendDescription("\n" + item.getName() + " (" + item.getPrice() + " coins)");
        }
        return embed;
    }

    public static void registerItem(ShopItem item) {
        ITEMS.add(item);
    }

    public static List<ShopItem> getItems() {
        return ITEMS;
    }
}
