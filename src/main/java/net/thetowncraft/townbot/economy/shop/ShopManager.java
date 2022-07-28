package net.thetowncraft.townbot.economy.shop;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.economy.shop.items.BlazingSpiritShopItem;
import net.thetowncraft.townbot.economy.shop.items.MysticParticleShopItem;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
import net.thetowncraft.townbot.listeners.discord.fun.Skin;
import net.thetowncraft.townbot.util.Constants;
import net.thetowncraft.townbot.util.SkinRender;
import org.bukkit.OfflinePlayer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ShopManager {

    public static ShopItem BLAZING_SPIRIT;
    public static ShopItem MYSTIC_PARTICLES;

    private static final List<ShopItem> ITEMS = new ArrayList<>();

    public static void initShop() {
        BLAZING_SPIRIT = registerItem(new BlazingSpiritShopItem());
        MYSTIC_PARTICLES = registerItem(new MysticParticleShopItem());
    }

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
        embed.setDescription("For roles, cosmetics, and more!" +
                "\nType `" + Bot.prefix + "shop <item name>` for more details." +
                "\nType `" + Bot.prefix + "buy <item name>` to purchase an item." +
                "\nType `" + Bot.prefix + "purchases` to view your items." +
                "\nType `" + Bot.prefix + "toggle <item name>` to toggle an item." +
                "\n----------------");
        for(ShopItem item : ITEMS) {
            embed.appendDescription("\n**" + item.getName() + "** (" + item.getPrice() + " coins)");
        }
        return embed;
    }

    public static List<ShopItem> getPurchases(Member member) {
        List<ShopItem> items = new ArrayList<>();
        for(ShopItem item : ITEMS) {
            if(item.possessedBy(member)) items.add(item);
        }
        return items;
    }

    public static EmbedBuilder getPurchasesEmbed(Member member) {
        String url;
        OfflinePlayer player = AccountManager.getInstance().getMinecraftPlayer(member);

        if(player == null) url = member.getUser().getEffectiveAvatarUrl();
        else url = SkinRender.renderFace(player);

        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor(member.getEffectiveName() + "'s Purchases", url);
        for(ShopItem item : getPurchases(member)) {
            if(item.isOff(member)) embed.appendDescription("\n*" + item.getName() + "* (Disabled)");
            else embed.appendDescription("\n**" + item.getName() + "**");
        }
        embed.appendDescription("Type `" + Bot.prefix + "toggle <item name>` to toggle items on and off.");
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
