package net.thetowncraft.townbot.custom_bosses.bosses.celestial_kingdom;

import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.custom_bosses.BossEventListener;
import net.thetowncraft.townbot.custom_bosses.bosses.mystic_realm.BlazingWitherBoss;
import net.thetowncraft.townbot.custom_items.CustomItem;
import net.thetowncraft.townbot.custom_items.CustomItems;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Ghast;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class HellfireGhast extends BlazingWitherBoss {

    @Override
    public void onFlintAndSteel(PlayerInteractEvent event) {

    }

    @EventHandler
    public void entityDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if(!entity.getWorld().getName().equals(world.getName())) return;

        if(entity instanceof Ghast && event.getDamager() instanceof Fireball) {
            event.setCancelled(true);
            ((Ghast) entity).damage(10, player);
        }
    }

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
        return ChatColor.GOLD;
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
        return new Location(Bukkit.getWorld(Plugin.OVERWORLD_NAME + "_thetown_ghast"), 0, 100, 0);
    }

    @Override
    public Location getPlayerSpawnLocation() {
        return new Location(Bukkit.getWorld(Plugin.OVERWORLD_NAME + "_thetown_ghast"), 10, 100, 0, 180, 0);
    }
}
