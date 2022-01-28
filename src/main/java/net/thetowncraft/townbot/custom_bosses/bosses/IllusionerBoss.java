package net.thetowncraft.townbot.custom_bosses.bosses;

import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.custom_bosses.BossEventListener;
import net.thetowncraft.townbot.items.CustomItem;
import net.thetowncraft.townbot.items.CustomItems;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

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
        addAttack(this::fallingGold, 100, 100);
    }

    @Override
    public void dodge() {
        dodge(1);
        boss.removePotionEffect(PotionEffectType.INVISIBILITY);
        player.removePotionEffect(PotionEffectType.BLINDNESS);
    }

    public void fallingGold() {
        Location location = player.getLocation();
        Location blockLocation = new Location(location.getWorld(), location.getX(), location.getY() + 10, location.getZ());
        blockLocation.getBlock().setType(Material.GOLD_BLOCK);
        FallingBlock fallingBlock = world.spawnFallingBlock(blockLocation, blockLocation.getBlock().getBlockData());
        TNTPrimed tnt = (TNTPrimed) world.spawnEntity(blockLocation, EntityType.PRIMED_TNT);
        tnt.setFuseTicks(40);
        tnt.setCustomName("Gold");
        blockLocation.getBlock().setType(Material.AIR);
        fallingBlock.setDropItem(false);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        Entity entity = event.getEntity();
        if(entity.getCustomName() == null) return;

        if(entity.getWorld().getName().equals(world.getName())) {
            if(entity.getType() == EntityType.PRIMED_TNT && entity.getCustomName().equals("Gold")) {
                for(Block block : event.blockList()) {
                    if(block.getType() == Material.GOLD_BLOCK && block.getY() < 107) {
                        block.setType(Material.AIR);
                    }
                }
            }
            event.blockList().clear();
        }
    }

    @EventHandler
    public void onTotem(EntityResurrectEvent event) {
        if(event.isCancelled()) return;

        LivingEntity entity = event.getEntity();
        if(!(entity instanceof Player)) return;

        Player player = (Player) entity;
        if(player.getWorld().getName().equals(world.getName())) return;

        ItemStack item = null;

        EntityEquipment equipment = player.getEquipment();
        if(equipment == null) return;

        ItemStack offHand = equipment.getItemInOffHand();
        ItemStack mainHand = equipment.getItemInMainHand();

        if(offHand.getType() == Material.TOTEM_OF_UNDYING) {
            item = offHand;
        }
        if(mainHand.getType() == Material.TOTEM_OF_UNDYING) {
            item = mainHand;
        }

        if(item == null) return;
        if(item.getItemMeta() == null) return;

        if(item.getItemMeta().hasCustomModelData() && item.getItemMeta().getCustomModelData() == 1) {
            initBossFight(player);
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

            Location playerPos = player.getLocation();
            Location bossLocation = illusioner.getLocation();
            Vector velocity = player.getVelocity();

            if(playerPos.getZ() > 60) return;

            if(bossBar.getProgress() < 0.65 && playerPos.getX() > -60 && playerPos.getZ() > -60) {
                player.teleport(new Location(playerPos.getWorld(), playerPos.getX() - 119, playerPos.getY(), playerPos.getZ(), playerPos.getYaw(), playerPos.getPitch()));
                boss.teleport(new Location(bossLocation.getWorld(), bossLocation.getX() - 119, bossLocation.getY(), bossLocation.getZ(), bossLocation.getYaw(), bossLocation.getPitch()));
                player.setVelocity(velocity);
            }

            if(bossBar.getProgress() < 0.35 && playerPos.getX() < -60) {
                player.teleport(new Location(playerPos.getWorld(), playerPos.getX() + 119, playerPos.getY() + 1, playerPos.getZ() + 120, playerPos.getYaw(), playerPos.getPitch()));
                boss.teleport(new Location(bossLocation.getWorld(), bossLocation.getX() + 119, bossLocation.getY() + 1, bossLocation.getZ() + 120, bossLocation.getYaw(), bossLocation.getPitch()));
                player.setVelocity(velocity);
            }
        }
    }

    @Override
    public String getBossName() {
        return "The Mystic Illusioner";
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
        return 180;
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
