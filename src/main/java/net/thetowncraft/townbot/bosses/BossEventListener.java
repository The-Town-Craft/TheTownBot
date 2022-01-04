package net.thetowncraft.townbot.bosses;

import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.bosses.portal.CustomPortal;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.sound.sampled.Port;
import java.util.ArrayList;
import java.util.List;

public abstract class BossEventListener implements Listener {

    private static final List<BossEventListener> LISTENERS = new ArrayList<>();

    public boolean bossBeingChallenged = false;
    public LivingEntity boss = null;
    public BossBar bossBar = null;
    public final World world;

    public BossEventListener() {
        this.world = this.getBossSpawnLocation().getWorld();
        LISTENERS.add(this);
        initAttacks();
    }

    public void initBossFight(Player player) {
        if(bossBeingChallenged) {
            sendBossBeingChallengedMessage(player);
            return;
        }

        sendBossTitleEffects(player);

    }

    public void sendBossBeingChallengedMessage(Player player) {
        player.sendMessage(ChatColor.RED + "This boss is already being challenged");
    }

    public void sendBossTitleEffects(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 5, false, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 1, false, false, false));
        player.sendTitle(this.getBossTitleColor() + "" + ChatColor.BOLD + this.getBossName(), this.getBossDescColor() + this.getBossDescription(), 10, 70, 20);
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 100, 1);
    }

    public void setUpBoss(Player player) {
        for(Entity entityInWorld : world.getEntities()) {
            if(!(entityInWorld instanceof Player)) {
                entityInWorld.remove();
            }
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.get(), () -> {
            player.teleport(this.getPlayerSpawnLocation());
            Location bossSpawn = this.getBossSpawnLocation();
            player.playSound(new Location(world, bossSpawn.getX(), bossSpawn.getY() + 60, bossSpawn.getZ()), Sound.MUSIC_DISC_PIGSTEP, 100, 1);
            if(player.getGameMode() == GameMode.SURVIVAL) player.setGameMode(GameMode.ADVENTURE);

            world.setGameRule(GameRule.MOB_GRIEFING, false);

            boss = (LivingEntity) world.spawnEntity(bossSpawn, this.getBaseEntity());
            boss.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(this.getBossHealth());
            boss.setHealth(this.getBossHealth());
            boss.setCustomName(this.getBossName());
            boss.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 99999, 0));
            boss.setCustomNameVisible(false);
            bossBeingChallenged = true;

        }, 70);
    }

    public abstract void initAttacks();
    public abstract String getBossName();
    public abstract String getBossDescription();
    public abstract EntityType getBaseEntity();
    public abstract double getBossHealth();
    public abstract ChatColor getBossTitleColor();
    public abstract ChatColor getBossDescColor();
    public abstract BarColor getBarColor();

    public abstract Location getBossSpawnLocation();
    public abstract Location getPlayerSpawnLocation();
    public abstract CustomPortal getPortal();
}
