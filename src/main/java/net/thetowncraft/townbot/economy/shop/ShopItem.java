package net.thetowncraft.townbot.economy.shop;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
import net.thetowncraft.townbot.util.data.Data;
import org.bukkit.OfflinePlayer;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public abstract class ShopItem {

    public EmbedBuilder getEmbed() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(getName());
        embed.setDescription("**Description**: " + getDesc());
        embed.appendDescription("\n" + "**Price**: " + getPrice() + " Coins");
        embed.appendDescription("\nType `" + Bot.prefix + "buy " + getName() + "` to purchase this item.");
        embed.setImage(getImage());
        embed.setColor(getColor());
        return embed;
    }

    public void turnOff(Member member) {
        OfflinePlayer player = AccountManager.getInstance().getMinecraftPlayer(member);
        if(player == null) return;
        turnOff(player);
    }

    public void turnOff(OfflinePlayer player) {
        File file = new File(getToggleDir(), player.getUniqueId() + ".txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void turnOn(Member member) {
        OfflinePlayer player = AccountManager.getInstance().getMinecraftPlayer(member);
        if(player == null) return;
        turnOn(player);
    }

    public void turnOn(OfflinePlayer player) {
        File file = new File(getToggleDir(), player.getUniqueId() + ".txt");
        file.delete();
    }

    public boolean isOff(Member member) {
        OfflinePlayer player = AccountManager.getInstance().getMinecraftPlayer(member);
        if(player == null) return true;

        return isOff(player);
    }

    public boolean isOff(OfflinePlayer player) {
        File file = new File(getToggleDir(), player.getUniqueId() + ".txt");
        return file.exists();
    }

    public File getToggleDir() {
        return Data.getFile("economy/shop/" + getName() + "/");
    }

    public boolean possessedBy(OfflinePlayer player) {
        Member member = AccountManager.getInstance().getDiscordMember(player);
        if(member == null) return false;

        return possessedBy(member);
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
