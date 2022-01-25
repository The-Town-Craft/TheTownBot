package net.thetowncraft.townbot.custom_bosses.bosses;

import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.custom_bosses.BossEventListener;
import net.thetowncraft.townbot.items.CustomItem;
import net.thetowncraft.townbot.items.CustomItems;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.sound.midi.ControllerEventListener;

public class IllusionerBoss extends BossEventListener {

    /*
    ATTACK IDEAS:
    Arrow rain
    Lightning wall
    Dodge
     */
    @Override
    public void initAttacks() {
        addAttack(this::updateParticles, 10, 10);
    }

    public void updateParticles() {
        world.spawnParticle(Particle.PORTAL, boss.getLocation(), 100);
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
    public void onEffect(EntityPotionEffectEvent event) {
        Entity entity = event.getEntity();
        PotionEffect effect = event.getNewEffect();
        if(effect == null) return;
        if(entity.getType() == EntityType.ILLUSIONER) {
            if(effect.getType() == PotionEffectType.INVISIBILITY) {
                Illusioner illusioner = (Illusioner) entity;
                illusioner.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, effect.getDuration(), 0, true, false, false));
            }
        }
        if(entity.getType() == EntityType.PLAYER & effect.getType() == PotionEffectType.BLINDNESS) {
            event.setCancelled(true);
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
        return new Location(Bukkit.getWorld(Plugin.OVERWORLD_NAME + "_thetown_illusioner"), 0, 101, 42, 180, 0);
    }

    @Override
    public Location getPlayerSpawnLocation() {
        return new Location(Bukkit.getWorld(Plugin.OVERWORLD_NAME + "_thetown_illusioner"), 0, 101, 0, 0, 0);
    }
}
