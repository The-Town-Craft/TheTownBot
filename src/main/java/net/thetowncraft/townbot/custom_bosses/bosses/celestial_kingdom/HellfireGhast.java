package net.thetowncraft.townbot.custom_bosses.bosses.celestial_kingdom;

import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.custom_bosses.BossDungeonEventListener;
import net.thetowncraft.townbot.custom_bosses.BossEventListener;
import net.thetowncraft.townbot.custom_items.CustomItem;
import net.thetowncraft.townbot.custom_items.CustomItems;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.util.Vector;

import java.util.Random;

public class HellfireGhast extends BossDungeonEventListener {

    @Override
    public void initAttacks() {
        addAttack(this::dodge, 0, 200);
        addAttack(this::witherSkeleton, 50, 200);
        addAttack(this::tnt, 100, 200);
        addAttack(this::lightning, 150, 200);
        addAttack(this::checkHeight, 0, 20);
    }

    @Override
    public void initSpawns() {
        addSpawn(new Vector(-44, 156, -218), EntityType.WITHER_SKELETON);
        addSpawn(new Vector(-44, 156, -199), EntityType.MAGMA_CUBE);
    }

    public void witherSkeleton() {
        if(bossHalfHealth) {
            summonEntity(EntityType.WITHER_SKELETON, new Vector(1,0,0));
            summonEntity(EntityType.WITHER_SKELETON, new Vector(-1,0,0));
            summonEntity(EntityType.WITHER_SKELETON, new Vector(0,0,1));
            summonEntity(EntityType.WITHER_SKELETON, new Vector(0,0,-1));
        }
        else {
            summonEntity(EntityType.WITHER_SKELETON, new Vector(0,0,0));
        }
    }

    public void checkHeight() {
        if(boss.getLocation().getY() > 110) {
            boss.setVelocity(new Vector(0, -3, 0));
        }
    }

    public void tnt() {
        if(bossHalfHealth) {
            summonTNT(new Vector(1, 0, 0), 50);
            summonTNT(new Vector(-1, 0, 0), 50);
            summonTNT(new Vector(0, 0, 1), 50);
            summonTNT(new Vector(0, 0, -1), 50);
        }
        else {
            summonTNT(new Vector(0, 0, 0), 75);
        }
    }

    @Override
    public boolean superLightning() {
        return true;
    }

    @Override
    public boolean superTNT() {
        return true;
    }

    @Override
    public void dodge() {
        Ghast ghast = (Ghast) this.boss;
        ghast.setTarget(player);
        if(!bossHalfHealth) dodge(2);
    }

    @EventHandler
    public void shootBow(EntityShootBowEvent event) {
        LivingEntity entity = event.getEntity();
        if(!entity.getWorld().getName().equals(world.getName())) return;
        if(!bossHalfHealth) return;

        if(entity instanceof Player) {
            if(new Random().nextInt(2) == 1) dodge(2);
        }
    }

    @EventHandler
    public void entityDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if(!entity.getWorld().getName().equals(world.getName())) return;

        if(entity instanceof Ghast && event.getDamager() instanceof Fireball) {
            event.setCancelled(true);
            ((Ghast) entity).damage(10, player);
        }
    }

    @Override
    public String getBossName() {
        return "Hellfire Ghast";
    }

    @Override
    public String getBossDescription() {
        return "Queen of Magma";
    }

    @Override
    public String getBossEmoji() {
        return "<:datsfire:758769290748625019>";
    }

    @Override
    public EntityType getBaseEntity() {
        return EntityType.GHAST;
    }

    @Override
    public double getBossHealth() {
        return 400;
    }

    @Override
    public ChatColor getBossTitleColor() {
        return ChatColor.RED;
    }

    @Override
    public ChatColor getBossDescColor() {
        return ChatColor.GOLD;
    }

    @Override
    public BarColor getBarColor() {
        return BarColor.RED;
    }

    @Override
    public CustomItem getBossItem() {
        return CustomItems.SATANIC_MAGMABALL;
    }

    @Override
    public String getBossRoleId() {
        return "1003068913485099089";
    }

    @Override
    public Sound getBossMusic() {
        return Sound.MUSIC_DISC_PIGSTEP;
    }

    @Override
    public String getDeathMessage() {
        if(inDungeon) return "burnt to a crisp in the dungeon of the";
        return "was melted by";
    }

    @Override
    public Location getBossSpawnLocation() {
        return new Location(Bukkit.getWorld(Plugin.OVERWORLD_NAME + "_thetown_ghast"), -69, 100, -41, 180, 0);
    }

    @Override
    public Location getPlayerBossSpawnLocation() {
        return new Location(Bukkit.getWorld(Plugin.OVERWORLD_NAME + "_thetown_ghast"), -69, 92, -87);
    }

    @Override
    public Location getDungeonSpawnLocation() {
        return new Location(Bukkit.getWorld(Plugin.OVERWORLD_NAME + "_thetown_ghast"), -44.5, 253, -233.5);
    }

    @Override
    public int getYBarrier() {
        return 140;
    }
}
