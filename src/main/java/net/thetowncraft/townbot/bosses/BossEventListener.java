package net.thetowncraft.townbot.bosses;

import net.thetowncraft.townbot.bosses.portal.CustomPortal;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import javax.sound.sampled.Port;
import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;

public abstract class BossEventListener implements Listener {

    private static final List<BossEventListener> LISTENERS = new ArrayList<>();

    public BossEventListener() {
        LISTENERS.add(this);
        initAttacks();
    }

    public void initBossFight() {

    }

    public abstract void initAttacks();
    public abstract String getBossName();
    public abstract String getBossDescription();
    public abstract EntityType getBaseEntity();
    public abstract double getBossHealth();
    public abstract BarColor getBarColor();

    public abstract Location getBossSpawnLocation();
    public abstract Location getPlayerSpawnLocation();
    public abstract CustomPortal getPortal();
}
