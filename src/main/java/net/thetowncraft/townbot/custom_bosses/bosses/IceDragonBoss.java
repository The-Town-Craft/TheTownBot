package net.thetowncraft.townbot.custom_bosses.bosses;

import net.thetowncraft.townbot.custom_bosses.BossEventListener;
import net.thetowncraft.townbot.items.CustomItem;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.EntityType;

public class IceDragonBoss extends BossEventListener {

    @Override
    public String getBossName() {
        return null;
    }

    @Override
    public String getBossDescription() {
        return null;
    }

    @Override
    public EntityType getBaseEntity() {
        return null;
    }

    @Override
    public double getBossHealth() {
        return 0;
    }

    @Override
    public ChatColor getBossTitleColor() {
        return null;
    }

    @Override
    public ChatColor getBossDescColor() {
        return null;
    }

    @Override
    public BarColor getBarColor() {
        return null;
    }

    @Override
    public CustomItem getBossItem() {
        return null;
    }

    @Override
    public Sound getBossMusic() {
        return null;
    }

    @Override
    public Location getBossSpawnLocation() {
        return null;
    }

    @Override
    public Location getPlayerSpawnLocation() {
        return null;
    }
}
