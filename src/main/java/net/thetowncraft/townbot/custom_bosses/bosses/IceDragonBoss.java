package net.thetowncraft.townbot.custom_bosses.bosses;

import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.custom_bosses.BossEventListener;
import net.thetowncraft.townbot.items.CustomItem;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

public class IceDragonBoss extends BossEventListener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(event.getBlock().getType() == Material.COMMAND_BLOCK) {
            initBossFight(event.getPlayer());
            event.setCancelled(true);
        }
    }

    @Override
    public String getBossName() {
        return "Ice Dragon";
    }

    @Override
    public String getBossDescription() {
        return "A beast trapped in a prison of ice.";
    }

    @Override
    public EntityType getBaseEntity() {
        return EntityType.ENDER_DRAGON;
    }

    @Override
    public double getBossHealth() {
        return 300;
    }

    @Override
    public ChatColor getBossTitleColor() {
        return ChatColor.AQUA;
    }

    @Override
    public ChatColor getBossDescColor() {
        return ChatColor.DARK_AQUA;
    }

    @Override
    public BarColor getBarColor() {
        return BarColor.BLUE;
    }

    @Override
    public CustomItem getBossItem() {
        return null;
    }

    @Override
    public Sound getBossMusic() {
        return Sound.MUSIC_DISC_PIGSTEP;
    }

    @Override
    public Location getBossSpawnLocation() {
        return new Location(Bukkit.getWorld(Plugin.OVERWORLD_NAME + "_thetown_ice_dragon"), 0, 66, 0, 180, 0);
    }

    @Override
    public Location getPlayerSpawnLocation() {
        return new Location(Bukkit.getWorld(Plugin.OVERWORLD_NAME + "_thetown_ice_dragon"), 0, 62, -55, 0, 0);
    }
}
