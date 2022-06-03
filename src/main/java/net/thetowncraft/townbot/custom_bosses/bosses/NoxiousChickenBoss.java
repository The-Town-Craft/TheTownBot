package net.thetowncraft.townbot.custom_bosses.bosses;

import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.custom_bosses.BossEventListener;
import net.thetowncraft.townbot.dimension.DimensionEventListener;
import net.thetowncraft.townbot.items.CustomItem;
import net.thetowncraft.townbot.items.CustomItems;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NoxiousChickenBoss extends BossEventListener {

    private boolean canMirage;

    @Override
    public void initAttacks() {
        this.addAttack(this::summonWolves, 300, 1200);
        this.addAttack(this::launchTNT, 600, 600);
    }

    @Override
    public void spawnBoss() {
        super.spawnBoss();
        canMirage = true;
    }

    public void summonWolves() {
        Chicken chicken = (Chicken) this.boss;
        Wolf wolf1 = (Wolf) world.spawnEntity(chicken.getLocation(), EntityType.WOLF);
        Wolf wolf2 = (Wolf) world.spawnEntity(chicken.getLocation(), EntityType.WOLF);

        wolf1.setTarget(chicken.getTarget());
        wolf2.setTarget(chicken.getTarget());

        wolf1.setAngry(true);
        wolf2.setAngry(true);

        int damage = 30;

        wolf1.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(damage);
        wolf2.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(damage);
    }

    public void launchTNT() {
        Chicken chicken = (Chicken) this.boss;
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

    public void mirageAttack() {
        Chicken chicken = (Chicken) this.boss;
        if(!canMirage) return;
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

    @EventHandler
    public void onChickenDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if(entity.getType() == EntityType.CHICKEN) {
            if (!entity.getWorld().getName().equals(DimensionEventListener.MYSTIC_REALM)) return;

            Chicken chicken = (Chicken) entity;
            Player player = chicken.getKiller();
            if(player == null) return;

            initBossFight(player);
        }
    }

    @EventHandler
    public void onTargetChange(EntityTargetEvent event) {
        Chicken chicken = (Chicken) this.boss;
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
    public void onDamage(EntityDamageEvent event) {
        Entity nonLiving = event.getEntity();
        if(!(nonLiving instanceof LivingEntity)) return;
        LivingEntity entity = (LivingEntity) nonLiving;
        if(entity.getWorld().getName().equals(world.getName())) {
            if(event.getCause() == EntityDamageEvent.DamageCause.CRAMMING) {
                event.setCancelled(true);
                return;
            }
            if(entity.getType() == EntityType.CHICKEN && entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() == getBossHealth()) {
                if(event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
                    event.setCancelled(true);
                    return;
                }
                if(bossBar.getProgress() <= 0.2) {
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

    @Override
    public void sendBossBeingChallengedMessage(Player player) {
        player.sendMessage("<" + ChatColor.YELLOW + "The Noxious Chicken" + ChatColor.RESET+  "> " + ChatColor.RED + "You will regret this...");
    }

    @Override
    public String getBossName() {
        return "Noxious Chicken";
    }

    @Override
    public String getBossDescription() {
        return "Guardian of the Poultry";
    }

    @Override
    public EntityType getBaseEntity() {
        return EntityType.CHICKEN;
    }

    @Override
    public double getBossHealth() {
        return 130;
    }

    @Override
    public ChatColor getBossTitleColor() {
        return ChatColor.YELLOW;
    }

    @Override
    public ChatColor getBossDescColor() {
        return ChatColor.YELLOW;
    }

    @Override
    public BarColor getBarColor() {
        return BarColor.GREEN;
    }

    @Override
    public CustomItem getBossItem() {
        return CustomItems.NOXIOUS_FEATHER;
    }

    @Override
    public Sound getBossMusic() {
        return Sound.MUSIC_DISC_PIGSTEP;
    }

    @Override
    public Location getBossSpawnLocation() {
        return new Location(Bukkit.getWorld(Plugin.OVERWORLD_NAME + "_thetown_chicken"), -40, 111, -26, 90, 0);
    }

    @Override
    public Location getPlayerSpawnLocation() {
        return new Location(Bukkit.getWorld(Plugin.OVERWORLD_NAME + "_thetown_chicken"), -3, 111, -25, 90, 0);
    }
}
