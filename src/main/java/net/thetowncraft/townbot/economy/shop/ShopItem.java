package net.thetowncraft.townbot.economy.shop;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.thetowncraft.townbot.Bot;

import java.awt.*;

public class ShopItem {

    private final String name;
    private final String desc;
    private final String image;
    private final Color color;
    private final int price;
    private final String roleId;


    public ShopItem(String name, String desc, String image, Color color, int price, String roleId) {
        this.name = name;
        this.desc = desc;
        this.image = image;
        this.color = color;
        this.price = price;
        this.roleId = roleId;
    }

    public EmbedBuilder getEmbed() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(name);
        embed.setDescription("**Description**: " + desc);
        embed.appendDescription("\n" + "**Price**: " + price + " Coins");
        embed.setImage(image);
        embed.setColor(color);
        return embed;
    }

    public boolean possessedBy(Member member) {
        return member.getRoles().contains(getRole());
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getImage() {
        return image;
    }

    public Color getColor() {
        return color;
    }

    public int getPrice() {
        return price;
    }

    public Role getRole() {
        return Bot.jda.getRoleById(getRoleId());
    }

    public String getRoleId() {
        return roleId;
    }
}
