package net.thetowncraft.townbot.custom_bosses.bosses;

import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.custom_bosses.BossEventListener;
import net.thetowncraft.townbot.dimension.DimensionEventListener;
import net.thetowncraft.townbot.items.CustomItem;
import net.thetowncraft.townbot.items.CustomItems;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;

public class IllusionerBoss extends BossEventListener {

    /*
    ATTACK IDEAS:
    Arrow rain
    Lightning wall
    Dodge

    SOUNDS:
    Beacon,
    Explosion,
    Lightning,
    Totem,
    Iron Golem Death
    
     */

    @Override
    public void initAttacks() {
        addAttack(this::dodge, 100, 100);
        addAttack(this::slam, 50, 200);
        addAttack(this::levitate, 0, 200);
    }

    @Override
    public void dodge() {
        dodge(1);
        boss.removePotionEffect(PotionEffectType.INVISIBILITY);
        player.removePotionEffect(PotionEffectType.BLINDNESS);
    }

    public void slam() {
        boss.setVelocity(new Vector(0, 2, 0));
        Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.get(), () -> {
            boss.teleport(new Location(world, player.getLocation().getX(), boss.getLocation().getY(), player.getLocation().getZ()));
        }, 15);
    }

    public void shootCreepers(Location location) {
        Creeper creeper1 = spawnCreeper(location);
        Creeper creeper2 = spawnCreeper(location);
        Creeper creeper3 = spawnCreeper(location);
        Creeper creeper4 = spawnCreeper(location);

        creeper1.setVelocity(new Vector(2, 0, 0));
        creeper2.setVelocity(new Vector(-2, 0, 0));
        creeper3.setVelocity(new Vector(0, 0, 2));
        creeper4.setVelocity(new Vector(0, 0, -2));
    }

    private Creeper spawnCreeper(Location location) {
        Creeper creeper = (Creeper) world.spawnEntity(location, EntityType.CREEPER);
        creeper.setCustomName("Mystic Creeper");
        creeper.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(1);
        creeper.setHealth(1);
        creeper.setPowered(true);
        return creeper;
    }

    public void levitate() {
        boss.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 30, 1, true, false, false));
        world.playSound(boss.getLocation(), Sound.ENTITY_EVOKER_PREPARE_SUMMON, 10, 1);
        Location location = player.getLocation();
        Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.get(), () -> {

            double dx = boss.getLocation().getX() - location.getX();
            double dz = boss.getLocation().getZ() - location.getZ();
            if(dx < -60 || dx > 60 || dz < -60 || dz > 60) return;

            boss.teleport(location);
            for(Entity entity : boss.getNearbyEntities(3, 3, 3)) {
                if(entity instanceof Player) {
                    Player player = (Player) entity;
                    player.setVelocity(new Vector(0, 1, 0));
                    player.damage(20, boss);
                }
            }
            world.playSound(boss.getLocation(), Sound.ENTITY_WITHER_BREAK_BLOCK, 5, 1);
            world.playSound(boss.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 5, 1);
            world.playSound(boss.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 5, 1);
        }, 30);
    }

    public void playArenaChangeEffect() {
        world.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 100, 0);
        world.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 100, 0);
        world.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 100, 0);
        world.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_DEATH, 100, 0);
        player.playEffect(EntityEffect.TOTEM_RESURRECT);
    }

    @EventHandler
    public void onShootBow(EntityShootBowEvent event) {
        LivingEntity entity = event.getEntity();
        Entity projectile = event.getProjectile();
        Vector velocity = projectile.getVelocity();

        if(!entity.getWorld().getName().equals(world.getName())) return;
        if(entity.getType() != EntityType.ILLUSIONER) return;
        if(new Random().nextInt(4) != 1 && bossBar.getProgress() > 0.5) return;

        SizedFireball fireball = (SizedFireball) world.spawnEntity(projectile.getLocation(), EntityType.FIREBALL);
        fireball.setDisplayItem(CustomItems.ILLUSIONER_HEART.createItemStack(1));
        fireball.setVelocity(new Vector(velocity.getX(), 0, velocity.getZ()));
        fireball.setShooter(boss);
        fireball.setVisualFire(false);
        event.setProjectile(fireball);
        world.playSound(fireball.getLocation(), Sound.ENTITY_WITCH_CELEBRATE, 10, 1);
        world.playSound(fireball.getLocation(), Sound.ENTITY_ENDER_DRAGON_SHOOT, 10, 1);
    }

    @EventHandler
    public void onPlayerEat(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if(item.getType() == CustomItems.MYSTIC_ARTIFACT.getBaseItem() && item.getItemMeta() != null) {
            ItemMeta itemMeta = item.getItemMeta();
            if(itemMeta.hasCustomModelData() && itemMeta.getCustomModelData() == 1) {
                if(!player.getWorld().getName().equals(DimensionEventListener.MYSTIC_REALM)) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(ChatColor.RED + "This item can only be consumed in The Mystic Realm.");
                    return;
                }
                event.setCancelled(true);
                item.setAmount(item.getAmount() - 1);
                initBossFight(player);
            }
        }
    }

    @Override
    public void onEntityExplode(EntityExplodeEvent event) {
        super.onEntityExplode(event);

        Entity entity = event.getEntity();

        if(entity.getType() == EntityType.CREEPER && entity.getWorld().getName().equals(world.getName())) {
            shootCreepers(entity.getLocation());
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(!bossBeingChallenged) return;
        Entity entity = event.getEntity();
        if(entity instanceof Illusioner) {
            Illusioner illusioner = (Illusioner) entity;
            if(!illusioner.getWorld().getName().equals(world.getName())) return;
            if(bossBar == null) return;

            if(event.getCause() == EntityDamageEvent.DamageCause.FALL && event.getEntity().getType() == EntityType.ILLUSIONER) {
                event.setCancelled(true);
                List<Entity> entities = illusioner.getNearbyEntities(4, 4, 4);
                for(Entity nearby : entities) {
                    if(nearby instanceof Player) {
                        Player player = (Player) nearby;
                        player.damage(20, illusioner);
                        player.setVelocity(new Vector(0, 1, 0));
                        if(bossBar.getProgress() < 0.5) {
                            shootCreepers(boss.getLocation());
                        }
                    }
                }
                world.playSound(boss.getLocation(), Sound.ENTITY_WITHER_BREAK_BLOCK, 5, 1);
                world.playSound(boss.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 5, 1);
                return;
            }

            Location playerPos = player.getLocation();
            Location bossLocation = illusioner.getLocation();
            Vector velocity = player.getVelocity();

            if(playerPos.getZ() > 60) return;

            if(bossBar.getProgress() < 0.65 && playerPos.getX() > -60 && playerPos.getZ() > -60) {
                player.setVelocity(new Vector(velocity.getX(), 1.5, velocity.getZ()));
                world.playSound(playerPos, Sound.ENTITY_GENERIC_EXPLODE, 100, 1);
                boss.setInvulnerable(true);

                Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.get(), () -> {
                    player.teleport(new Location(playerPos.getWorld(), playerPos.getX() - 119, playerPos.getY(), playerPos.getZ(), playerPos.getYaw(), playerPos.getPitch()));
                    double health = boss.getHealth();
                    boss.remove();
                    boss = (LivingEntity) world.spawnEntity(new Location(bossLocation.getWorld(), -119, 110, 0, bossLocation.getYaw(), bossLocation.getPitch()), EntityType.ILLUSIONER);
                    ItemStack[] stacks = {};
                    boss.getEquipment().setArmorContents(stacks);
                    boss.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(this.getBossHealth());
                    boss.setHealth(health);
                    boss.setCustomName(this.getBossName());
                    boss.setCustomNameVisible(false);
                    playArenaChangeEffect();
                }, 35);
            }

            if(bossBar.getProgress() < 0.35 && playerPos.getX() < -60) {
                player.setVelocity(new Vector(velocity.getX(), 1.5, velocity.getZ()));
                world.playSound(playerPos, Sound.ENTITY_GENERIC_EXPLODE, 100, 1);
                boss.setInvulnerable(true);

                Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.get(), () -> {
                    player.teleport(new Location(playerPos.getWorld(), playerPos.getX() + 119, playerPos.getY() + 1, playerPos.getZ() + 120, playerPos.getYaw(), playerPos.getPitch()));
                    double health = boss.getHealth();
                    boss.remove();
                    boss = (LivingEntity) world.spawnEntity(new Location(bossLocation.getWorld(), 0, 110, 120, bossLocation.getYaw(), bossLocation.getPitch()), EntityType.ILLUSIONER);
                    ItemStack[] stacks = {};
                    boss.getEquipment().setArmorContents(stacks);
                    boss.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(this.getBossHealth());
                    boss.setHealth(health);
                    boss.setCustomName(this.getBossName());
                    boss.setCustomNameVisible(false);
                    playArenaChangeEffect();
                }, 35);
            }

        }
    }

    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity damager = event.getDamager();
        if(!entity.getWorld().getName().equals(world.getName())) return;

        if(damager instanceof SizedFireball) {
            if(entity instanceof Illusioner) {
                ((Illusioner) entity).damage(40);
            }
            if(entity instanceof Player) {
                ((Player) entity).damage(5);
            }
        }
        if(damager instanceof TNTPrimed && entity instanceof Illusioner) {
            event.setCancelled(true);
        }
        if(damager instanceof Player && entity instanceof SizedFireball) {
            ((Player) damager).setHealth(((Player) damager).getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
            damager.playEffect(EntityEffect.LOVE_HEARTS);
            damager.playEffect(EntityEffect.WITCH_MAGIC);
            damager.playEffect(EntityEffect.VILLAGER_HAPPY);
            ((Player) damager).addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 100, 0, true, false, false));
            ((Player) damager).playSound(damager.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 10, 1);
        }
    }

    @Override
    public String getBossName() {
        return "Mystic Illusioner";
    }

    @Override
    public String getBossDescription() {
        return "A mysterious magician, lost in time.";
    }

    @Override
    public EntityType getBaseEntity() {
        return EntityType.ILLUSIONER;
    }

    @Override
    public double getBossHealth() {
        return 200;
    }

    @Override
    public ChatColor getBossTitleColor() {
        return ChatColor.DARK_PURPLE;
    }

    @Override
    public ChatColor getBossDescColor() {
        return ChatColor.DARK_PURPLE;
    }

    @Override
    public BarColor getBarColor() {
        return BarColor.PURPLE;
    }

    @Override
    public CustomItem getBossItem() {
        return CustomItems.ILLUSIONER_HEART;
    }

    @Override
    public Sound getBossMusic() {
        return Sound.MUSIC_DISC_PIGSTEP;
    }

    @Override
    public Location getBossSpawnLocation() {
        return new Location(Bukkit.getWorld(Plugin.OVERWORLD_NAME + "_thetown_illusioner"), 0, 101, 20, 180, 0);
    }

    @Override
    public Location getPlayerSpawnLocation() {
        return new Location(Bukkit.getWorld(Plugin.OVERWORLD_NAME + "_thetown_illusioner"), 0, 101, -20, 0, 0);
    }
}
