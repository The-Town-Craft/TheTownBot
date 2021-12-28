package net.thetowncraft.townbot.bosses;

import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.items.CustomItems;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class BlazingWitherEventListener implements Listener {

    private static boolean bossBeingChallenged = false;
    public static Wither wither = null;
    private static final long lightningInaccuracy = 30;
    public static final String bossWorldName = "world_1597802541_thetown_void";

    //enter void world
    @EventHandler
    public void onFlintAndSteel(PlayerInteractEvent event) {

        Material centerBlock = Material.CRYING_OBSIDIAN;
        Material surroundingBlock = Material.NETHERRACK;
        World bossWorld = Bukkit.getWorld(bossWorldName);
        Location playerSpawn = new Location(bossWorld, 27, 122, 0, -90.0f, 0.0f);
        Location bossSpawn = new Location(bossWorld, 54, 140, 0, 90.0f, 0.0f);

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

        if(bossBeingChallenged) {
            event.getPlayer().sendMessage(ChatColor.RED + "This boss is already being challenged");
            event.setCancelled(true);
            return;
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 5, false, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 1, false, false, false));
        player.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "The Blazing Wither", ChatColor.RED + "King of the NetherVoid", 10, 70, 20);
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 100, 1);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.get(), () -> {
            player.setVelocity(new Vector(0, 10, 0));
        }, 20);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.get(), () -> {
            player.teleport(playerSpawn);
            player.playSound(new Location(world, 43, 168, 0), Sound.MUSIC_DISC_PIGSTEP, 100, 1);
            if(player.getGameMode() == GameMode.SURVIVAL) player.setGameMode(GameMode.ADVENTURE);
                }, 50);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.get(), () -> {
            for(Entity entity : Bukkit.getWorld(bossWorldName).getEntities()) {
                if(!(entity instanceof Player)) {
                    entity.remove();
                }
            }
            bossWorld.setGameRule(GameRule.MOB_GRIEFING, false);
            Entity bossEntity = bossWorld.spawnEntity(bossSpawn, EntityType.WITHER);
            bossEntity.setCustomName("The Blazing Wither");
            bossEntity.setCustomNameVisible(false);
            Wither wither = (Wither) bossEntity;
            wither.getBossBar().setColor(BarColor.RED);
            bossBeingChallenged = true;
            BlazingWitherEventListener.wither = wither;
        });
    }

    //void world

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        World world = entity.getWorld();
        EntityType type = entity.getType();
        if(!world.getName().equals(bossWorldName)) return;
        if(!(type == EntityType.WITHER_SKELETON ||
                type == EntityType.WITHER ||
                type == EntityType.DROPPED_ITEM ||
                type == EntityType.WITHER_SKULL ||
                type == EntityType.ARROW ||
                type == EntityType.DRAGON_FIREBALL)) event.setCancelled(true);

        if(type == EntityType.WITHER_SKULL) {
            if(wither == null) return;
            wither.launchProjectile(LargeFireball.class).setDisplayItem(new ItemStack(Material.NETHER_STAR));
        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        if(!event.getBlock().getWorld().getName().equals(bossWorldName)) return;
        event.setCancelled(true);
    }


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if(!player.getWorld().getName().equals(bossWorldName)) return;
        event.setKeepInventory(true);
        event.getDrops().clear();

        player.setGameMode(GameMode.SURVIVAL);
        Location bedLocation = player.getBedSpawnLocation();
        if(bedLocation == null) {
            player.teleport(new Location(Bukkit.getWorld("world_1597802541"),-161, 64, 230));
            bossBeingChallenged = false;
            wither = null;
            return;
        }

        player.teleport(bedLocation);
        bossBeingChallenged = false;
        wither = null;
        return;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        World world = entity.getWorld();
        if(!world.getName().equals(bossWorldName)) return;

        if(entity.getType() == EntityType.WITHER_SKELETON) {
            event.getDrops().clear();
        }
        if(entity.getType() == EntityType.WITHER) {
            event.getDrops().clear();
            world.dropItem(new Location(world, 43, 121, 0), CustomItems.BLAZING_THUNDERSTAR.createItemStack(1));
            event.setDroppedExp(1200);
            wither = null;
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if(!event.getPlayer().getWorld().getName().equals(bossWorldName)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if(!bossBeingChallenged) return;

        Player player = event.getPlayer();

        if(!bossWorldName.equals(player.getWorld().getName())) return;

        if(player.getLocation().getX() >= 61) {
            if(wither != null && player.getGameMode() == GameMode.ADVENTURE) {
                return;
            }
            player.addPotionEffect(PotionEffectType.BLINDNESS.createEffect(30, 1));

            Location bedLocation = player.getBedSpawnLocation();
            if(bedLocation == null) {
                player.teleport(new Location(Bukkit.getWorld("world_1597802541"),-161, 64, 230));
                bossBeingChallenged = false;
                return;
            }
            player.teleport(bedLocation);
            bossBeingChallenged = false;
        }
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        Material item = event.getItem().getType();

        if(item == Material.CHORUS_FRUIT) {
            event.setCancelled(true);
            return;
        }
        if(item == Material.MILK_BUCKET) {
            event.setCancelled(true);
            return;
        }
        if(item == Material.POTION) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if(Bukkit.getWorld(bossWorldName).getName().equals(event.getPlayer().getWorld().getName())) {
            if(event.getPlayer().getGameMode() != GameMode.ADVENTURE) return;
            event.getPlayer().damage(1000, wither);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if(!entity.getWorld().getName().equals(bossWorldName)) return;

        if(entity instanceof Player) {

            if(event.getCause() != EntityDamageEvent.DamageCause.FIRE_TICK) entity.setFireTicks(100);

            if(event.getCause() == EntityDamageEvent.DamageCause.LIGHTNING) {
                ((Player) entity).setHealth(2);
                entity.setVelocity(new Vector(entity.getVelocity().getX(), 1, entity.getVelocity().getZ()));
                ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200,1, false, false, false));
                ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100,1));
            }
            if(event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100,1));
            }
        }
    }

    @EventHandler
    public void targetChange(EntityTargetEvent event) {
        Entity entity = event.getEntity();
        Entity target = event.getTarget();
        if(entity.getType() != EntityType.WITHER_SKELETON) return;
        if(target == null) return;

        if(entity.getWorld().getName().equals(bossWorldName)) {
            if(target.getType() == EntityType.WITHER) {
                event.setCancelled(true);
            }
        }
    }

    public static void strikeTargetWithLightning() {
        if(wither == null) return;

        World world = wither.getWorld();

        LivingEntity target = wither.getTarget();
        if(target == null) return;

        Location targetPos = target.getLocation();

        Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.get(), () -> {
            world.spawnEntity(targetPos, EntityType.LIGHTNING);
        }, lightningInaccuracy);
    }

    public static void summonWitherSkeletons() {
        if(wither == null) return;

        World world = wither.getWorld();
        WitherSkeleton skeleton1 = ((WitherSkeleton) world.spawnEntity(new Location(world, 43, 121, -0), EntityType.WITHER_SKELETON));
        WitherSkeleton skeleton2 = ((WitherSkeleton) world.spawnEntity(new Location(world, 43, 121, 1), EntityType.WITHER_SKELETON));

        ItemStack sword = new ItemStack(Material.NETHERITE_SWORD);
        sword.addEnchantment(Enchantment.KNOCKBACK, 2);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 5);

        ItemStack axe = new ItemStack(Material.NETHERITE_AXE);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 5);

        skeleton1.getEquipment().setItemInMainHand(sword);

        skeleton1.getEquipment().setHelmet(new ItemStack(Material.NETHERITE_HELMET));
        skeleton1.getEquipment().setChestplate(new ItemStack(Material.NETHERITE_CHESTPLATE));
        skeleton1.getEquipment().setLeggings(new ItemStack(Material.NETHERITE_LEGGINGS));
        skeleton1.getEquipment().setBoots(new ItemStack(Material.NETHERITE_BOOTS));

        skeleton2.getEquipment().setItemInMainHand(axe);

        skeleton2.getEquipment().setHelmet(new ItemStack(Material.NETHERITE_HELMET));
        skeleton2.getEquipment().setChestplate(new ItemStack(Material.NETHERITE_CHESTPLATE));
        skeleton2.getEquipment().setLeggings(new ItemStack(Material.NETHERITE_LEGGINGS));
        skeleton2.getEquipment().setBoots(new ItemStack(Material.NETHERITE_BOOTS));

        skeleton1.addPotionEffect(PotionEffectType.SPEED.createEffect(10000, 0));
        skeleton2.addPotionEffect(PotionEffectType.SPEED.createEffect(10000, 0));
    }
}
