package net.thetowncraft.townbot.items;

import com.google.common.collect.Sets;
import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.bosses.BlazingWitherEventListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.type.Fire;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class CustomItemListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        HumanEntity entity = event.getWhoClicked();

        if(entity instanceof Player) {
            Player player = (Player) entity;
            CustomItems.updateItemStats(player);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        HumanEntity entity = event.getPlayer();
        if(entity instanceof Player) {
            Player player = (Player) entity;
            CustomItems.updateItemStats(player);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        CustomItems.updateItemStats(event.getPlayer());
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        LivingEntity entity = event.getEntity();
        if(entity instanceof Player) {
            Player player = (Player) entity;
            Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.get(), () -> CustomItems.updateItemStats(player), 5);
        }
    }

    @EventHandler
    public void onShootBow(EntityShootBowEvent event) {
        ProjectileSource shooter = event.getEntity();
        if(shooter instanceof Player) {
            ItemStack stack = CustomItems.getItemStackOf(((Player) shooter).getPlayer(), CustomItems.BLAZING_THUNDERSTAR);
            if(stack == null) return;
            SizedFireball fireball = event.getEntity().launchProjectile(SizedFireball.class);
            Vector vel = event.getProjectile().getVelocity();
            fireball.setVelocity(new Vector(vel.getX() * 2, vel.getY() * 2, vel.getZ() * 2));
            fireball.setDisplayItem(stack);
            fireball.setIsIncendiary(false);
            fireball.setCustomName("Thunderstar");
            event.setProjectile(fireball);
            fireball.setFireTicks(0);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if(event.getEntity() instanceof SizedFireball) {
            if(event.getEntity().getName().equals("Thunderstar")) {
                event.blockList().clear();
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity nonLiving = event.getEntity();

        if(!(nonLiving instanceof LivingEntity)) {
            return;
        }

        LivingEntity entity = (LivingEntity) nonLiving;

        if(damager instanceof SizedFireball && damager.getCustomName().equals("Thunderstar")) {
            SizedFireball fireball = (SizedFireball) damager;
            ProjectileSource shooter = fireball.getShooter();
            if(shooter != entity) entity.damage(20);
        }

        if(damager instanceof Player) {
            Player player = (Player) damager;
            int damageMultiplier = CustomItems.getItemAmountOf(player, CustomItems.SHAPED_GLASS) + 1;
            double finalDamage = event.getDamage();

            for(int i = 1; i < damageMultiplier; i++) {
                finalDamage = finalDamage * 2;
            }

            event.setDamage(finalDamage);
        }

        if(damager instanceof Projectile) {
            Projectile projectile = (Projectile) damager;
            ProjectileSource shooter = projectile.getShooter();
            if(shooter instanceof Player) {
                Player player = (Player) shooter;
                if(BlazingWitherEventListener.bossWorldName.equals(player.getWorld().getName())) return;
                Random random = new Random();
                int thunderstars = CustomItems.getItemAmountOf(player, CustomItems.BLAZING_THUNDERSTAR);
                if(thunderstars == 0) return;
                if(BlazingWitherEventListener.bossWorldName.equals(player.getWorld().getName())) return;
                int procChance = thunderstars * 25;
                int num = random.nextInt(100);

                boolean proc = num < procChance;

                if(proc) {
                    CustomItems.BLAZING_THUNDERSTAR.procOnHit(player, thunderstars, entity, entity.getWorld());
                }
            }
        }
    }

    private final Set<UUID> prevPlayersOnGround = Sets.newHashSet();

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Vector velocity = player.getVelocity();
        if (velocity.getY() > 0) {
            double jumpVelocity = (double) 0.42F;
            if (player.hasPotionEffect(PotionEffectType.JUMP)) {
                jumpVelocity += (double) ((float) (player.getPotionEffect(PotionEffectType.JUMP).getAmplifier() + 1) * 0.1F);
            }
            if (e.getPlayer().getLocation().getBlock().getType() != Material.LADDER && prevPlayersOnGround.contains(player.getUniqueId())) {
                if (!player.isOnGround() && Double.compare(velocity.getY(), jumpVelocity) == 0) {
                    for(CustomItem item : CustomItems.getItems()) {
                        int amount = CustomItems.getItemAmountOf(player, item);
                        if(amount == 0) continue;
                        item.onPlayerJump(player, amount);
                    }
                }
            }
        }
        if (player.isOnGround()) {
            prevPlayersOnGround.add(player.getUniqueId());
        } else {
            prevPlayersOnGround.remove(player.getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        CustomItems.onItemInteract(event);
    }
}
