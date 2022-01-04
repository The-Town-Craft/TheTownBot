package net.thetowncraft.townbot.bosses;

import net.thetowncraft.townbot.Plugin;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

public class ChickenBossEventListener implements Listener {

    private static final double BOSS_HEALTH = 130;
    private static final World world = Bukkit.getWorld("world_1597802541_thetown_chicken");
    private static final Location playerStartPos = new Location(world, -3, 111, -25, 90, 0);
    private static final Location bossSpawn = new Location(world, -40, 111, -26, 90, 0);
    private static boolean bossBeingChallenged = false;
    public static Chicken chicken = null;
    public static BossBar bossBar;
    public static boolean canMirage = true;

    private static boolean isVoidDimension(World world) {
        return world.getName().equals(Plugin.VOID_DIMENSION_NAME);
    }

    @EventHandler
    public void onChickenDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if(entity.getType() == EntityType.CHICKEN) {
            Chicken chicken = (Chicken) entity;
            Player player = chicken.getKiller();
            if(player == null) return;
            if(!player.isOp()) return;
            if(!player.getWorld().getName().equals("world_1597802541")) return;

            if(bossBeingChallenged) {
                player.sendMessage("<" + ChatColor.YELLOW + "" + ChatColor.BOLD + "The Noxious Chicken" + ChatColor.RESET+  "> " + ChatColor.RED + "You will later regret this...");
                return;
            }

            player.sendMessage("<" + ChatColor.YELLOW + "The Noxious Chicken" + ChatColor.RESET+  "> " + ChatColor.RED + "You will regret this...");

            Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.get(), () -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 5, false, false, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 1, false, false, false));
                player.sendTitle(ChatColor.YELLOW + "" + ChatColor.BOLD + "The Noxious Chicken", ChatColor.YELLOW + "Guardian of the Poultry", 10, 70, 20);
                player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 100, 1);
            }, 30);

            Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.get(), () -> {
                player.teleport(playerStartPos);
                player.playSound(new Location(world, 43, 168, 0), Sound.MUSIC_DISC_PIGSTEP, 100, 1);
                if(player.getGameMode() == GameMode.SURVIVAL) player.setGameMode(GameMode.ADVENTURE);
                bossBar = Bukkit.getServer().createBossBar("The Noxious Chicken", BarColor.GREEN, BarStyle.SOLID);
                bossBar.addPlayer(player);
            }, 70);

            for(Entity entityInWorld : world.getEntities()) {
                if(!(entityInWorld instanceof Player)) {
                    entityInWorld.remove();
                }
            }
            world.setGameRule(GameRule.MOB_GRIEFING, false);

            Entity bossEntity = world.spawnEntity(bossSpawn, EntityType.CHICKEN);
            Chicken chickenBoss = (Chicken) bossEntity;
            chickenBoss.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(BOSS_HEALTH);
            chickenBoss.setHealth(BOSS_HEALTH);
            chickenBoss.setCustomName("The Noxious Chicken");
            chickenBoss.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 99999, 0));
            chicken.setCustomNameVisible(false);
            bossBeingChallenged = true;
            canMirage = true;
            ChickenBossEventListener.chicken = chickenBoss;
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        World world = entity.getWorld();
        if(!world.equals(ChickenBossEventListener.world)) return;
        event.getDrops().clear();

        if(entity.getType() == EntityType.CHICKEN && entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() == BOSS_HEALTH) {
            event.getDrops().clear();
            //world.dropItem(new Location(world, 43, 121, 0), CustomItems.BLAZING_THUNDERSTAR.createItemStack(1));
            event.setDroppedExp(1200);
            chicken = null;
            bossBar.removeAll();
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if(!bossBeingChallenged) return;

        Player player = event.getPlayer();

        if(!world.equals(player.getWorld())) return;

        if(player.getLocation().getX() <= -49) {
            if(chicken == null && player.getGameMode() == GameMode.ADVENTURE) {
                player.addPotionEffect(PotionEffectType.BLINDNESS.createEffect(30, 1));
                Location bedLocation = player.getBedSpawnLocation();
                if(bedLocation == null) {
                    player.teleport(new Location(Bukkit.getWorld("world_1597802541"),-161, 64, 230));
                    bossBeingChallenged = false;
                    return;
                }
                player.teleport(bedLocation);
                player.setGameMode(GameMode.SURVIVAL);
                bossBeingChallenged = false;
                return;
            }
        }
        if(chicken == null) return;
        chicken.setTarget(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if(world.equals(event.getPlayer().getWorld())) {
            if(event.getPlayer().getGameMode() != GameMode.ADVENTURE) return;
            event.getPlayer().setHealth(0);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if(event.getEntity().getWorld().equals(world)) {
            event.blockList().clear();
        }
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        if(event.getBlock().getWorld().equals(world)) {
            event.blockList().clear();
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if(!player.getWorld().equals(world)) return;
        event.setKeepInventory(true);
        event.getDrops().clear();

        player.setGameMode(GameMode.SURVIVAL);
        Location bedLocation = player.getBedSpawnLocation();
        if(bedLocation == null) {
            player.teleport(new Location(Bukkit.getWorld("world_1597802541"),-161, 64, 230));
            bossBeingChallenged = false;
            chicken = null;
            bossBar.removeAll();
            return;
        }

        player.teleport(bedLocation);
        bossBeingChallenged = false;
        chicken = null;
        bossBar.removeAll();
        return;
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        if(!event.getBlock().getWorld().equals(world)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity damager = event.getDamager();


        if(entity.getType() != EntityType.CHICKEN) return;

        Chicken chicken = (Chicken) entity;
        if(!chicken.getWorld().equals(world)) return;

        if(damager.getType() == EntityType.ARROW) {
            for(Player player : world.getPlayers()) {
                player.sendMessage("<" + ChatColor.YELLOW + "The Noxious Chicken" + ChatColor.RESET+  "> " + ChatColor.RED + "You cannot damage me with arrows...");
            }
            event.setCancelled(true);
            return;
        }

        if(damager.getType() != EntityType.PLAYER) return;

        Player player = (Player) damager;

        if(!chicken.getWorld().equals(world)) return;

        chicken.setTarget(player);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity nonLiving = event.getEntity();
        if(!(nonLiving instanceof LivingEntity)) return;
        LivingEntity entity = (LivingEntity) nonLiving;
        if(entity.getWorld().getName().equals(world.getName())) {
            if(event.getCause() == EntityDamageEvent.DamageCause.CRAMMING) {
                event.setCancelled(true);
                return;
            }
            if(entity.getType() == EntityType.CHICKEN && entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() == BOSS_HEALTH) {
                if(event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
                    event.setCancelled(true);
                    return;
                }
                double progress = chicken.getHealth() / chicken.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
                bossBar.setProgress(progress);
                if(progress <= 0.2) {
                    mirageAttack();
                }
            }
            if(entity.getType() == EntityType.PLAYER && event.getCause() != EntityDamageEvent.DamageCause.POISON) {
                entity.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 60, 0));
                entity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 0));
                entity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 500, 0));
            }
        }
    }

    @EventHandler
    public void onTargetChange(EntityTargetEvent event) {
        if(chicken == null) return;
        if(event.getEntity() instanceof LivingEntity) {
            if(event.getEntity().getWorld().getName().equals(world.getName())) {
                if(event.getEntity().getType() == EntityType.WOLF) {
                    Wolf entity = (Wolf) event.getEntity();
                    if(chicken.getTarget() == null) return;
                    if(entity.getTarget() == chicken.getTarget()) return;
                    if(chicken.getTarget().getType() != EntityType.PLAYER) return;
                    entity.setTarget(chicken.getTarget());
                }
            }
        }
    }

    @EventHandler
    public void onFangsAttack(EntityDamageByEntityEvent event) {
        if(event.getDamager().getType() == EntityType.EVOKER_FANGS && event.getEntity().getWorld().equals(world)) {
            if(event.getEntity() instanceof LivingEntity) {
                LivingEntity entity = (LivingEntity) event.getEntity();
                entity.setHealth(5);
            }
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if(!event.getPlayer().getWorld().equals(world)) return;
        event.setCancelled(true);
    }

    public static void launchTNT() {
        if(chicken == null) return;
        for(int i = 1; i < 20; i++) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.get(), () -> {
                TNTPrimed tnt = world.spawn(chicken.getLocation(), TNTPrimed.class);
                tnt.setFuseTicks(50);
                tnt.setSource(chicken);
                tnt.setIsIncendiary(false);
            }, i * 5);
        }
    }

    public static void useFangs() {
        if(chicken == null) return;
        if(chicken.getTarget() == null) return;
        EvokerFangs fangs = (EvokerFangs) world.spawnEntity(chicken.getTarget().getLocation(), EntityType.EVOKER_FANGS);
        fangs.setOwner(chicken);
    }

    public static void summonWolves() {
        if(chicken == null) return;
        Wolf wolf1 = (Wolf) world.spawnEntity(chicken.getLocation(), EntityType.WOLF);
        Wolf wolf2 = (Wolf) world.spawnEntity(chicken.getLocation(), EntityType.WOLF);

        wolf1.setTarget(chicken.getTarget());
        wolf2.setTarget(chicken.getTarget());

        wolf1.setAngry(true);
        wolf2.setAngry(true);

        int health = 40;
        int damage = 40;

        wolf1.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
        wolf2.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);

        wolf1.setHealth(health);
        wolf2.setHealth(health);

        wolf1.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(damage);
        wolf2.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(damage);
    }

    public static void mirageAttack() {
        canMirage = false;
        LivingEntity target = chicken.getTarget();
        if(target != null) {
            target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 0));
        }

        for(int i = 1; i < 20; i++) {
            chicken.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 200, 0));
            world.spawnEntity(chicken.getLocation(), EntityType.CHICKEN);
        }
    }
}
