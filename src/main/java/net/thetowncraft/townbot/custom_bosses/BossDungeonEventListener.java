package net.thetowncraft.townbot.custom_bosses;

import net.thetowncraft.townbot.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BossDungeonEventListener extends BossEventListener {

    public boolean completedDungeon;
    public boolean inDungeon;
    public Map<Vector, EntityType> spawns;

    public BossDungeonEventListener() {
        spawns = new HashMap<>();
        initSpawns();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Plugin.get(), this::checkDungeonStatus, 10, 10);
    }

    public void initSpawns() {
    }

    public void checkDungeonStatus() {
        if(player == null) return;
        if(!player.getWorld().getName().equals(world.getName())) return;

        if(inDungeon) {
            if(player.getLocation().getY() < getYBarrier()) {
                int enemies = 0;
                for(Entity entity : entities) {
                    if(entity instanceof Monster && entity.getLocation().getY() > getYBarrier()) {
                        if(entity instanceof Vex) continue;
                        if(!entity.isDead()) enemies++;
                    }
                }
                if(enemies == 0) {
                    inDungeon = false;
                    super.spawnBoss();
                    checkBar();
                    super.playBossMusic();
                    sendBossChallengeMsg(player);
                }
                else {
                    player.teleport(getPlayerSpawnLocation());
                    player.sendMessage(ChatColor.RED + "There are " + enemies + " enemies left.");
                }
            }
        }
    }

    public void addSpawn(Vector vector, EntityType type) {
        spawns.put(vector, type);
    }

    public void spawnEntities() {
        clearEntities();
        int i = 0;
        for(Map.Entry<Vector, EntityType> entry : spawns.entrySet()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.get(), () -> {
                Entity entity = world.spawnEntity(new Location(world, entry.getKey().getX(), entry.getKey().getY(), entry.getKey().getZ()), entry.getValue());
                if(entity instanceof LivingEntity) {
                    ((LivingEntity) entity).setRemoveWhenFarAway(false);
                }
            }, i * 10L);
            i++;
        }
    }

    @Override
    public void playBossMusic() {
        if(!inDungeon) super.playBossMusic();
    }

    @Override
    public boolean initBossFight(Player player) {
        if(!bossBeingChallenged) inDungeon = true;
        return super.initBossFight(player);
    }

    @Override
    public void spawnBoss() {
        if(inDungeon) {
            initDungeon(player);
        }
        else {
            super.spawnBoss();
        }
    }

    @Override
    public void checkBar() {
        if(!inDungeon) super.checkBar();
    }

    public void initDungeon(Player player) {
        inDungeon = true;
        completedDungeon = false;
        spawnEntities();
    }

    @Override
    public String getChallengeMessage() {
        if(inDungeon) return "has entered the dungeon of";
        return super.getChallengeMessage();
    }

    @Override
    public Location getPlayerSpawnLocation() {
        if(inDungeon) return getDungeonSpawnLocation();
        else return getPlayerBossSpawnLocation();
    }

    public abstract Location getPlayerBossSpawnLocation();
    public abstract Location getDungeonSpawnLocation();
    public abstract int getYBarrier();
}
