package net.thetowncraft.townbot.dimension;

import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.bosses.BlazingWitherEventListener;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Random;

public class DimensionEventListener implements Listener {

    public static final String MYSTIC_REALM = "world_1597802541_thetown_mystic_realm";

    //Teleport
    @EventHandler
    public void onFlintAndSteel(PlayerInteractEvent event) {

        Material centerBlock = Material.OBSIDIAN;
        Material surroundingBlock = Material.SLIME_BLOCK;
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();

        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(block == null || block.getType() != centerBlock) return;
        if(player.getInventory().getItemInMainHand().getType() != Material.FLINT_AND_STEEL) return;

        World world = block.getWorld();
        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();

        if(world.getBlockAt(x + 1, y, z).getType() != surroundingBlock) return;
        if(world.getBlockAt(x - 1, y, z).getType() != surroundingBlock) return;


        if(world.getBlockAt(x, y, z + 1).getType() != surroundingBlock) return;
        if(world.getBlockAt(x, y, z - 1).getType() != surroundingBlock) return;

        World mysticRealm = Bukkit.getWorld(MYSTIC_REALM);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.get(), () -> player.teleport(new Location(mysticRealm, 116, 63, -143, 180, 0)), 10);

        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 5, false, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 1, false, false, false));
        player.sendTitle(ChatColor.AQUA + "" + ChatColor.BOLD + "The Mystic Realm", ChatColor.DARK_BLUE + "A forgotten world, lost in time...", 10, 70, 20);
        player.playSound(player.getLocation(), Sound.AMBIENT_SOUL_SAND_VALLEY_MOOD, 100, 1);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location pos = player.getLocation();
        if(!player.getWorld().getName().equals(MYSTIC_REALM)) return;
        if((int) pos.getX() == 100 && (int) pos.getY() == 50 && (int) pos.getZ() == 0) {
            Location spawn = player.getBedSpawnLocation();
            if(spawn == null) {
                player.teleport(new Location(Bukkit.getWorld("world_1597802541"),-161, 64, 230));
            }
            else {
                player.teleport(spawn);
            }
        }
    }

    //Entity Spawns
    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Location location = event.getLocation();
        Entity entity = event.getEntity();
        World world = location.getWorld();

        if(world.getName().equals(MYSTIC_REALM)) {
            if(entity.getType() == EntityType.ZOMBIE) {
                Random random = new Random();
                if(random.nextInt(100) == 1) {
                    world.spawnEntity(location, EntityType.CHICKEN);
                }
                else {
                    if(random.nextInt(5) == 1) {
                        world.spawnEntity(location, EntityType.SLIME);
                    }
                }
                event.setCancelled(true);
            }
        }
    }

    public static String getBiomeName(Location location) {
        Biome biome = location.getWorld().getBiome((int) location.getX(), (int) location.getY(), (int) location.getZ());
        return biome.getKey().getKey();
    }
}
