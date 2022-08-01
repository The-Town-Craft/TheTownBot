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

import java.awt.*;

public class CelestialPhantom extends BossEventListener {

    @Override
    public String getBossName() {
        return "Celestial Phantom";
    }

    @Override
    public String getBossDescription() {
        return "A chaotic beast unleashed.";
    }

    @Override
    public String getBossEmoji() {
        return "<:celestial_membrane:1003465278589513799>";
    }

    @Override
    public EntityType getBaseEntity() {
        return EntityType.PHANTOM;
    }

    @Override
    public double getBossHealth() {
        return 500;
    }

    @Override
    public ChatColor getBossTitleColor() {
        return ChatColor.BLUE;
    }

    @Override
    public ChatColor getBossDescColor() {
        return ChatColor.DARK_BLUE;
    }

    @Override
    public BarColor getBarColor() {
        return BarColor.BLUE;
    }

    @Override
    public CustomItem getBossItem() {
        return CustomItems.CELESTINE_ELYTRA;
    }

    @Override
    public String getBossRoleId() {
        return "1003464368647835669";
    }

    @Override
    public Sound getBossMusic() {
        return Sound.MUSIC_DISC_PIGSTEP;
    }

    @Override
    public String getDeathMessage() {
        return "was eaten by";
    }

    @Override
    public Location getBossSpawnLocation() {
        return new Location(Bukkit.getWorld(Plugin.OVERWORLD_NAME + "_thetown_phantom"), 0, 100, 0);
    }

    @Override
    public Location getPlayerSpawnLocation() {
        return new Location(Bukkit.getWorld(Plugin.OVERWORLD_NAME + "_thetown_phantom"), 20, 100, 0, 180, 0);
    }
}
