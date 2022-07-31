package net.thetowncraft.townbot.dimension;

import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.custom_items.CustomItems;
import org.bukkit.*;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

//SPAWN: 726 71 -1551
public class CelestialKingdomListener implements Listener {

    public static final String CELESTIAL_KINGDOM = "world_1597802541_thetown_celestial_kingdom";
    public static final Map<String, Location> PREV_PLAYER_POSITIONS = new HashMap<>();

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Location location = event.getLocation();
        Entity entity = event.getEntity();
        EntityType type = entity.getType();
        World world = location.getWorld();

        if(world == null) return;
        if(!world.getName().equals(CELESTIAL_KINGDOM)) return;

        if(type == EntityType.ZOMBIE) {
            event.setCancelled(true);
            spawnAcidicSlime(entity.getLocation());
        }
        if(type == EntityType.SLIME) {
            Slime slime = (Slime) entity;
            if(slime.getSize() != 4) {
                event.setCancelled(true);
            }
        }
        if(type == EntityType.WITHER_SKELETON) {
            if(new Random().nextInt(50) == 1) {
                event.setCancelled(true);
                Wither wither = (Wither) world.spawnEntity(location, EntityType.WITHER);
                BossBar bossBar = wither.getBossBar();
                if(bossBar != null) bossBar.removeAll();
            }
        }
    }

    public static Slime spawnAcidicSlime(Location pos) {
        World world = pos.getWorld();
        if(world == null) return null;
        Slime slime = (Slime) world.spawnEntity(pos, EntityType.SLIME);
        slime.setCustomName("Acidic Slime");
        return slime;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        EntityType type = entity.getType();
        World world = entity.getWorld();

        if(!world.getName().equals(CELESTIAL_KINGDOM)) return;

        if(type == EntityType.SLIME) {
            event.getDrops().clear();
            if(new Random().nextInt(15) == 1) {
                event.getDrops().add(CustomItems.ACIDIC_ARTIFACT.createItemStack(1));
            }
        }
        if(type == EntityType.GHAST) {
            event.getDrops().clear();
            event.getDrops().add(CustomItems.HELLFIRE_GHAST_TEAR.createItemStack(1));
        }
    }

    //Teleport
    @EventHandler
    public void onFlintAndSteel(PlayerInteractEvent event) {
        if(!Portal.isPortal(event, Material.MAGMA_BLOCK, Material.OBSIDIAN)) return;
        Player player = event.getPlayer();

        if(!player.isOp()) return;

        if(player.getWorld().getName().equals(CELESTIAL_KINGDOM)) {
            Location location;

            Location prevLocation = PREV_PLAYER_POSITIONS.get(player.getUniqueId().toString());
            if(prevLocation == null) {
                Location bedSpawnLocation = player.getBedSpawnLocation();
                if(bedSpawnLocation != null) location = bedSpawnLocation;
                else location = Plugin.SPAWN_LOCATION;
            }
            else {
                location = prevLocation;
            }

            Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.get(), () -> {
                player.teleport(location);
            }, 10);

            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 5, false, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 1, false, false, false));
        }
        else if(player.getWorld().getName().equals(MysticRealmListener.MYSTIC_REALM)){
            PREV_PLAYER_POSITIONS.put(player.getUniqueId().toString(), player.getLocation());
            World celestialKingdom = Bukkit.getWorld(CELESTIAL_KINGDOM);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.get(), () -> {
                player.teleport(new Location(celestialKingdom, 726, 71, -1551, 0, 0));
                player.playSound(player.getLocation(), Sound.AMBIENT_SOUL_SAND_VALLEY_MOOD, 100, 1);
            }, 10);

            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 5, false, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 1, false, false, false));
            player.sendTitle(ChatColor.GOLD + "" + ChatColor.BOLD + "Celestial Kingdom", ChatColor.DARK_RED + "Land of the blood lord...", 10, 70, 20);
        }
        else {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You can only use this altar in the Mystic Realm!");
        }
    }
}