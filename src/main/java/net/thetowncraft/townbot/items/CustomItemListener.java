package net.thetowncraft.townbot.items;

import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.bosses.BossEventListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.List;
import java.util.Random;

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
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity nonLiving = event.getEntity();

        if(!(nonLiving instanceof LivingEntity)) {
            return;
        }

        LivingEntity entity = (LivingEntity) nonLiving;

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
                if(BossEventListener.bossWorldName.equals(player.getWorld().getName())) return;
                Random random = new Random();
                int thunderstars = CustomItems.getItemAmountOf(player, CustomItems.BLAZING_THUNDERSTAR);
                if(thunderstars == 0) return;
                int procChance = thunderstars * 25;
                int num = random.nextInt(100);

                boolean proc = num < procChance;

                if(proc) {
                    CustomItems.BLAZING_THUNDERSTAR.procOnHit(player, thunderstars, entity, entity.getWorld());
                }
            }
        }
    }
}
