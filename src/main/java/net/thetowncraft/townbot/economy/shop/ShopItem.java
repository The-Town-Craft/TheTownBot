package net.thetowncraft.townbot.economy.shop;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.thetowncraft.townbot.Bot;

import java.awt.*;

public abstract class ShopItem {

    public EmbedBuilder getEmbed() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(getName());
        embed.setDescription("**Description**: " + getDesc());
        embed.appendDescription("\n" + "**Price**: " + getPrice() + " Coins");
        embed.setImage(getImage());
        embed.setColor(getColor());
        return embed;
    }

    public boolean possessedBy(Member member) {
        return member.getRoles().contains(getRole());
    }

    public abstract String getName();

    public abstract String getDesc();

    public abstract String getImage();

    public abstract Color getColor();

    public abstract int getPrice();

    public Role getRole() {
        return Bot.jda.getRoleById(getRoleId());
    }

    public abstract String getRoleId();
}
