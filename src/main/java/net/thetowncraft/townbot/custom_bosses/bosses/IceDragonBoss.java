package net.thetowncraft.townbot.custom_bosses.bosses;

import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.custom_bosses.BossEventListener;
import net.thetowncraft.townbot.items.CustomItem;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LightningStrike;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.weather.LightningStrikeEvent;

public class IceDragonBoss extends BossEventListener {

    private int lightningPos;

    @Override
    public void initAttacks() {
        this.addAttack(this::tryTeleport, 20, 20);
    }

    public void tryTeleport() {
        Location location = boss.getLocation();
        if(location.getY() > 90) {
            world.strikeLightningEffect(location);
            world.playSound(location, Sound.ENTITY_WITHER_BREAK_BLOCK, 10, 1);
            world.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 10, 1);
            boss.playEffect(EntityEffect.TOTEM_RESURRECT);
            boss.teleport(new Location(location.getWorld(), 0, 60, 0));
            lightningPos = 0;
            lightningAttack();
        }
    }

    public void lightningAttack() {
        spawnLightning(0, 0);

        if(lightningPos > 50 || lightningPos == -1) {
            lightningPos = -1;
            return;
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.get(), () -> {
            spawnLightning(lightningPos, 0);
            spawnLightning(0, lightningPos);
            spawnLightning(-lightningPos, 0);
            spawnLightning(0, -lightningPos);
            lightningPos++;
            lightningAttack();
        }, 2);
    }

    public void spawnLightning(double x, double z) {
        world.strikeLightning(new Location(world, x, 62, z));
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(event.getBlock().getType() == Material.COMMAND_BLOCK) {
            initBossFight(event.getPlayer());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLightningStrike(LightningStrikeEvent event) {
        if(!event.getWorld().getName().equals(world.getName())) return;

        if(lightningPos == -1) return;
        if(boss == null) return;

        Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.get(), () -> {
            if(boss == null) return;
            if(lightningPos == -1 || lightningPos > 50) return;
            LightningStrike lightningStrike = world.strikeLightning(event.getLightning().getLocation());
            lightningStrike.setSilent(true);
        }, 10);
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
