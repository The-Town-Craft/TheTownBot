package net.thetowncraft.townbot.dimension;

import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.custom_items.CustomItems;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DimensionEventListener implements Listener {

    public static final String MYSTIC_REALM = "world_1597802541_thetown_mystic_realm";
    public static final Map<String, Location> PREV_PLAYER_POSITIONS = new HashMap<>();

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

        if(player.getGameMode() == GameMode.ADVENTURE) return;

        if(player.getWorld().getName().equals(MYSTIC_REALM)) {
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
            player.sendTitle(null, ChatColor.AQUA + "Returning to the overworld...", 10, 70, 20);
        }
        else {
            PREV_PLAYER_POSITIONS.put(player.getUniqueId().toString(), player.getLocation());
            World mysticRealm = Bukkit.getWorld(MYSTIC_REALM);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.get(), () -> {
                player.teleport(new Location(mysticRealm, 1108, 69, 1924, 0, 0));
                player.playSound(player.getLocation(), Sound.AMBIENT_SOUL_SAND_VALLEY_MOOD, 100, 1);
            }, 10);

            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 5, false, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 1, false, false, false));
            player.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "The Mystic Realm", ChatColor.DARK_GREEN + "A forgotten world, lost in time...", 10, 70, 20);
        }
    }

    @EventHandler
    public void onGen(ChunkLoadEvent event) {
//        if(event.isNewChunk()) {
//            BlockState[] tileEntities = event.getChunk().getTileEntities();
//            for(BlockState state : tileEntities) {
//                if(state.getType() == Material.GRASS_BLOCK) {
//                    state.setType(Material.OBSIDIAN);
//                }
//            }
//        }
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        Block block = event.getBlock();
        Location location = block.getLocation();
        World world = block.getWorld();
        if(world.getName().equals(MYSTIC_REALM)) {
            if(getBiomeName(location).equalsIgnoreCase("ice_spikes")) {
                Block fireBlock = location.getBlock();
                if(fireBlock.getType() != Material.SOUL_FIRE) {
                    fireBlock.setType(Material.SOUL_FIRE);
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location pos = player.getLocation();
        if(!player.getWorld().getName().equals(MYSTIC_REALM)) return;
        if((int) pos.getX() == 100 && (int) pos.getY() == 50 && (int) pos.getZ() == 0) {
            Location spawn = player.getBedSpawnLocation();
            if(spawn == null) {
                player.teleport(Plugin.SPAWN_LOCATION);
            }
            else {
                player.teleport(spawn);
            }
        }
    }

    public static void checkBiomeEffects() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(player.getWorld().getName().equals(MYSTIC_REALM)) {
                if(!getBiomeName(player.getLocation()).equalsIgnoreCase("ice_spikes")) {
                    if(player.isInWater()) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60, 0, true, false, false));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 60, 0, true, false, false));
                    }
                }
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

            if(getBiomeName(location).equalsIgnoreCase("ice_spikes")) {
                if(entity.getType() == EntityType.GHAST || entity.getType() == EntityType.WITHER_SKELETON) return;
                if(!(entity instanceof LivingEntity)) return;

                event.setCancelled(true);

                if(new Random().nextInt(20) == 1) {
                    if(location.getY() < 50) return;
                    world.spawnEntity(new Location(world, location.getX(), location.getY() + 50, location.getZ()), EntityType.GHAST);
                }
                else {
                    Entity skeleton = world.spawnEntity(location, EntityType.WITHER_SKELETON);
                    skeleton.setCustomName("Glacial Skeleton");
                }
                return;
            }

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
            if(entity.getType() == EntityType.ENDERMAN) {
                if(new Random().nextInt(6) == 1) {
                    event.setCancelled(true);
                    spawnMysticCreeper(location);
                }
                else {
                    Enderman enderman = (Enderman) entity;
                    enderman.setCustomName("Mystic Enderman");
                    AttributeInstance health = enderman.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                    health.setBaseValue(health.getBaseValue() / 2);

                    AttributeInstance damage = enderman.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
                    damage.setBaseValue(damage.getBaseValue() * 2);
                }
            }
            if(entity.getType() == EntityType.WITHER_SKELETON) {
                if(new Random().nextInt(30) == 1) {
                    world.spawnEntity(entity.getLocation(), EntityType.LIGHTNING);
                    event.setCancelled(true);
                }
            }
        }
    }

    public static Creeper spawnMysticCreeper(Location location) {
        Creeper creeper = (Creeper) location.getWorld().spawnEntity(location, EntityType.CREEPER);
        creeper.setCustomName("Mystic Creeper");
        creeper.setPowered(true);
        creeper.setCustomNameVisible(false);
        creeper.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(1);
        creeper.setHealth(1);
        return creeper;
    }

    //Drops
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        Location location = entity.getLocation();
        World world = location.getWorld();
        Random random = new Random();

        if(world.getName().equals(MYSTIC_REALM)) {
            if(entity.getType() == EntityType.ENDERMAN) {
                event.getDrops().clear();
                if(random.nextInt(25) == 1) {
                    event.getDrops().add(CustomItems.SHAPED_GLASS.createItemStack(1));
                }
                else {
                    event.getDrops().add(CustomItems.MYSTIC_PEARL.createItemStack(1));
                }
            }
            if(entity.getType() == EntityType.CREEPER) {
                event.getDrops().clear();
                event.getDrops().add(CustomItems.MYSTIC_ARTIFACT.createItemStack(1));
            }
            if(entity.getType() == EntityType.WITHER_SKELETON) {
                String name = entity.getCustomName();
                if("Glacial Skeleton".equals(name)) {
                    event.getDrops().clear();
                    if(new Random().nextInt(15) == 1) {
                        event.getDrops().add(CustomItems.GLACIAL_SHARD.createItemStack(1));
                    }
                }
                else {
                    if(new Random().nextInt(9) == 1) {
                        event.getDrops().add(CustomItems.HUNTER_SKULL.createItemStack(1));
                    }
                }
            }
        }
    }

    public static String getBiomeName(Location location) {
        Biome biome = location.getWorld().getBiome(location);
        return biome.getKey().getKey();
    }
}
