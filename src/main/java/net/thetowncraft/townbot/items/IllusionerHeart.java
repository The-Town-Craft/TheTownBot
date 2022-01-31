package net.thetowncraft.townbot.items;

import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class IllusionerHeart extends CustomItem {

    private final List<String> players = new ArrayList<>();

    private static final int COOLDOWN = 200;
    private static final int DAMAGE = 20;
    private static final int RADIUS = 20;

    @Override
    public void onInteract(PlayerInteractEvent event, int amount) {
        Player player = event.getPlayer();
        World world = player.getWorld();

        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            if(players.contains(player.getUniqueId().toString())) return;
            if(player.getCooldown(getBaseItem()) != 0) return;

            if(!player.isOnGround()) {
                player.sendMessage(ChatColor.RED + "You must be on ground to use this item");
                return;
            }

            players.add(player.getUniqueId().toString());
            player.setCooldown(getBaseItem(), COOLDOWN);
            player.setVelocity(new Vector(0, 2, 0));
            player.playSound(player.getLocation(), Sound.ENTITY_EVOKER_PREPARE_SUMMON, 10, 1);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.get(), () -> {
                if(!player.isOnline()) {
                    players.remove(player.getUniqueId().toString());
                    return;
                }
                LivingEntity target = null;
                for(Entity entity : player.getNearbyEntities(RADIUS, RADIUS, RADIUS)) {
                    if(entity instanceof LivingEntity) {
                        target = (LivingEntity) entity;
                        break;
                    }
                }
                if(target == null) return;

                Location targetPos = target.getLocation();
                player.teleport(new Location(world, targetPos.getX(), player.getLocation().getY(), targetPos.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
                player.setVelocity(new Vector(0, -2, 0));
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10, 1);
            }, 15);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.get(), () -> {
                players.remove(player.getUniqueId().toString());
            }, COOLDOWN);
        }
    }

    @Override
    public void onPlayerDamage(Player player, EntityDamageEvent event, int amount) {
        if(event.getCause() != EntityDamageEvent.DamageCause.FALL) return;
        String uuid = player.getUniqueId().toString();
        if(players.contains(uuid)) {
            players.remove(uuid);
            event.setCancelled(true);
            boolean damaged = false;
            for(Entity entity : player.getNearbyEntities(5, 5, 5)) {
                if(entity instanceof LivingEntity) {
                    ((LivingEntity) entity).damage(DAMAGE, player);
                    entity.setVelocity(new Vector(0, 1, 0));
                    damaged = true;
                }
            }
            if(damaged) {
                player.playSound(player.getLocation(), Sound.ENTITY_WITHER_BREAK_BLOCK, 10, 1);
                player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_DEATH, 10, 1);
                player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 10, 1);
            }
        }
    }

    @Override
    public String getName() {
        return "Illusioner's Heart";
    }

    @Override
    public String getDescription() {
        return "Slam Attack [Right Click]";
    }

    @Override
    public int getCustomModelData() {
        return 1;
    }

    @Override
    public Material getBaseItem() {
        return Material.DEBUG_STICK;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.EPIC;
    }

    @Override
    public boolean shines() {
        return false;
    }
}
