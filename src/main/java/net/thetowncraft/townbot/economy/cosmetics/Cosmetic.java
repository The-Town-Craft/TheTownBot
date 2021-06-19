package net.thetowncraft.townbot.economy.cosmetics;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.entity.Player;

public abstract class Cosmetic {

    String id;

    public EmbedBuilder getShopMessageEmbedBuilder() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(getName());
        embed.setDescription(getEmote() + " " + getDescription() + "\n \nReact with :moneybag: to purchase!");
        embed.addField("Price", String.valueOf(getPrice()), true);
        embed.addField("Rarity", getRarity().name(), true);
        embed.setImage(getImagePath());
        return embed;
    }


    public abstract String getName();
    public abstract String getDescription();
    public abstract int getPrice();
    public abstract Rarity getRarity();
    public abstract String getEmote();
    public abstract String getImagePath();

    public void onPurchase(Member member) {}
    public void init(Member member) {}
    public void update(Player player) {}

}