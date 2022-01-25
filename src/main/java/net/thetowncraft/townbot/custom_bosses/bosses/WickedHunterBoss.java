package net.thetowncraft.townbot.custom_bosses.bosses;

import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.custom_bosses.BossEventListener;
import net.thetowncraft.townbot.items.CustomItem;
import net.thetowncraft.townbot.items.CustomItems;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.data.type.TNT;
import org.bukkit.boss.BarColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

import java.util.List;

public class WickedHunterBoss extends BossEventListener {

    private static final ItemStack BOW = new ItemStack(Material.BOW);
    private static final ItemStack SWORD = CustomItems.HUNTER_SWORD.createItemStack(1);

    private int secondsInvulnerable;


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemMeta meta = event.getItemInHand().getItemMeta();
        if(!meta.hasLore()) return;

        if(event.getItemInHand().getType() == Material.SKELETON_SKULL) {
            if(this.initBossFight(event.getPlayer())) {
                secondsInvulnerable = 0;
                event.getPlayer().setGameMode(GameMode.ADVENTURE);
                Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.get(), () -> event.getBlock().setType(Material.AIR), 20);
            }
            else event.setCancelled(true);
        }
    }

    @Override
    public void onBossSpawn(LivingEntity boss, Player player) {
        boss.setInvisible(true);
        boss.setInvulnerable(true);
        boss.setAI(false);
        boss.getEquipment().setItemInMainHand(SWORD);
    }

    @Override
    public void onBossHalfHealth() {
        boss.setFireTicks(9999999);
        boss.playEffect(EntityEffect.TOTEM_RESURRECT);
        world.playSound(boss.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 10, 1);
    }

    @Override
    public void initAttacks() {
        this.addAttack(this::lightningIntro, 0, 20);
        this.addAttack(this::switchWeapon, 100, 100);
        this.addAttack(this::slam, 400, 400);
        this.addAttack(this::levitate, 500, 200);
    }

    public void lightningIntro() {
        if(boss.isInvulnerable()) {
            if(secondsInvulnerable >= 10) {
                boss.setInvisible(false);
            }
            if(secondsInvulnerable >= 13) {
                boss.setAI(true);
                TNTPrimed tnt = (TNTPrimed) world.spawnEntity(boss.getLocation(), EntityType.PRIMED_TNT);
                tnt.setFuseTicks(0);
                Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.get(), () -> boss.setInvulnerable(false), 10);
                secondsInvulnerable = 0;
            }
            world.spawnEntity(boss.getLocation(), EntityType.LIGHTNING);
            secondsInvulnerable++;
        }
    }

    public void switchWeapon() {
        boss.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 100, 0, true, false, false));
        if(boss.getNearbyEntities(6, 6, 6).size() == 0) {
            if(boss.getEquipment().getItemInMainHand().getType() == Material.NETHERITE_SWORD) {
                boss.getEquipment().setItemInMainHand(BOW);
            }
        }
        else {
            if(boss.getEquipment().getItemInMainHand().getType() == Material.BOW) {
                boss.getEquipment().setItemInMainHand(SWORD);
            }
            boss.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1, true, false, false));
            boss.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 2, true, false, false));
        }
    }

    public void slam() {
        if(boss.isInvulnerable()) return;
        boss.setVelocity(new Vector(0, 2, 0));
        Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.get(), () -> boss.teleport(new Location(world, player.getLocation().getX(), boss.getLocation().getY(), player.getLocation().getZ())), 15);
    }

    public void levitate() {
        if(boss.isInvulnerable()) return;
        boss.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 30, 1, true, false, false));
        Location location = player.getLocation();
        Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.get(), () -> {
            boss.teleport(location);
            for(Entity entity : boss.getNearbyEntities(3, 3, 3)) {
                if(entity instanceof Player) {
                    Player player = (Player) entity;
                    player.setVelocity(new Vector(0, 1, 0));
                    player.damage(30, boss);
                }
            }
            world.playSound(boss.getLocation(), Sound.ENTITY_WITHER_BREAK_BLOCK, 5, 1);
            world.playSound(boss.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 5, 1);
            world.playSound(boss.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 5, 1);
        }, 30);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        Entity damaged = event.getEntity();
        if(!damaged.getWorld().getName().equals(world.getName())) return;

        if(boss == null) return;

        if(event.getCause() == EntityDamageEvent.DamageCause.FALL && event.getEntity().getType() == EntityType.STRAY) {
            event.setCancelled(true);
            List<Entity> entities = damaged.getNearbyEntities(4, 4, 4);
            for(Entity entity : entities) {
                if(entity instanceof Player) {
                    Player player = (Player) entity;
                    player.setHealth(1);
                    player.setVelocity(new Vector(0, 3, 0));
                }
            }
            world.playSound(boss.getLocation(), Sound.ENTITY_WITHER_BREAK_BLOCK, 5, 1);
            world.playSound(boss.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 5, 1);
        }
        if(event.getCause() == EntityDamageEvent.DamageCause.LIGHTNING) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity entity = event.getEntity();

        if(!entity.getWorld().getName().equals(world.getName())) return;

        if(damager instanceof Arrow && entity instanceof Player) {
            Player player = (Player) entity;
            player.damage(20);
            player.setFireTicks(40);
        }
        if(damager instanceof Fireball && entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            TNTPrimed tnt = (TNTPrimed) world.spawnEntity(livingEntity.getLocation(), EntityType.PRIMED_TNT);
            world.spawnEntity(livingEntity.getLocation(), EntityType.LIGHTNING);
            tnt.setFuseTicks(0);
            livingEntity.setVelocity(new Vector(player.getVelocity().getX(), 1, player.getVelocity().getY()));
        }
    }

    @EventHandler
    public void onArrowFire(ProjectileLaunchEvent event) {
        Projectile projectile = event.getEntity();
        if(!projectile.getWorld().getName().equals(world.getName())) return;

        if(bossHalfHealth) {
            ProjectileSource shooter = projectile.getShooter();
            if(shooter instanceof Stray) {
                Entity fireball = world.spawnEntity(projectile.getLocation(), EntityType.FIREBALL);
                fireball.setVelocity(projectile.getVelocity());
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void initEquipment() {
        BOW.addUnsafeEnchantment(Enchantment.KNOCKBACK, 5);
        BOW.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 3);
        BOW.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 20);
    }

    @Override
    public String getBossName() {
        return "The Wicked Hunter";
    }

    @Override
    public String getBossDescription() {
        return "Trained swordsman, ruthless fighter.";
    }

    @Override
    public EntityType getBaseEntity() {
        return EntityType.STRAY;
    }

    @Override
    public double getBossHealth() {
        return 120;
    }

    @Override
    public ChatColor getBossTitleColor() {
        return ChatColor.RED;
    }

    @Override
    public ChatColor getBossDescColor() {
        return ChatColor.DARK_RED;
    }

    @Override
    public BarColor getBarColor() {
        return BarColor.RED;
    }

    @Override
    public CustomItem getBossItem() {
        return CustomItems.HUNTER_SWORD;
    }

    @Override
    public Sound getBossMusic() {
        return Sound.MUSIC_NETHER_SOUL_SAND_VALLEY;
    }

    @Override
    public Location getBossSpawnLocation() {
        return new Location(Bukkit.getWorld(Plugin.OVERWORLD_NAME + "_thetown_wicked_hunter"), 43, 101, -320);
    }

    @Override
    public Location getPlayerSpawnLocation() {
        return new Location(Bukkit.getWorld(Plugin.OVERWORLD_NAME + "_thetown_wicked_hunter"), 43, 101, -267, 180, 0);
    }
}
