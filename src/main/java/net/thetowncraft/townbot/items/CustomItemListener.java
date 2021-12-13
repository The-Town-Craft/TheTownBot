package net.thetowncraft.townbot.items;

import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.items.CustomItems;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

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
        if(damager instanceof Player) {
            Player player = (Player) damager;
            int damageMultiplier = CustomItems.getItemAmountOf(player, CustomItems.SHAPED_GLASS) + 1;
            double finalDamage = event.getDamage();

            for(int i = 1; i < damageMultiplier; i++) {
                finalDamage = finalDamage * 2;
            }

            event.setDamage(finalDamage);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
    }
}
