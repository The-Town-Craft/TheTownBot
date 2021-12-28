package net.thetowncraft.townbot.items;

import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class ItemDropListener implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        World world = entity.getWorld();
        if(!world.getName().equals("world_1597802541")) return;

        long days = world.getFullTime()/24000;
        long phase = days % 8;
        if(phase == 1) {

        }
    }
}
