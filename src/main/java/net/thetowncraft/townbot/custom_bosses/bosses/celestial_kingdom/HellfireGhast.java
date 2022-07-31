package net.thetowncraft.townbot.custom_bosses.bosses.celestial_kingdom;

import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.custom_bosses.BossEventListener;
import net.thetowncraft.townbot.custom_items.CustomItem;
import net.thetowncraft.townbot.custom_items.CustomItems;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.EntityType;

public class HellfireGhast extends BossEventListener {

    @Override
    public String getBossName() {
        return "Hellfire Ghast";
    }

    @Override
    public String getBossDescription() {
        return "Queen of Magma";
    }

    @Override
    public String getBossEmoji() {
        return "<:datsfire:758769290748625019>";
    }

    @Override
    public EntityType getBaseEntity() {
        return EntityType.GHAST;
    }

    @Override
    public double getBossHealth() {
        return 400;
    }

    @Override
    public ChatColor getBossTitleColor() {
        return ChatColor.RED;
    }

    @Override
    public ChatColor getBossDescColor() {
        return ChatColor.YELLOW;
    }

    @Override
    public BarColor getBarColor() {
        return BarColor.RED;
    }

    @Override
    public CustomItem getBossItem() {
        return CustomItems.SATANIC_MAGMABALL;
    }

    @Override
    public String getBossRoleId() {
        return "1003068913485099089";
    }

    @Override
    public Sound getBossMusic() {
        return Sound.MUSIC_DISC_PIGSTEP;
    }

    @Override
    public String getDeathMessage() {
        return "was melted by";
    }

    @Override
    public Location getBossSpawnLocation() {
        return new Location(Bukkit.getWorld(Plugin.OVERWORLD_NAME + "_thetown_ghast"), 0, 135, 0);
    }

    @Override
    public Location getPlayerSpawnLocation() {
        return new Location(Bukkit.getWorld(Plugin.OVERWORLD_NAME + "_thetown_ghast"), 50, 100, 0, 180, 0);
    }
}
