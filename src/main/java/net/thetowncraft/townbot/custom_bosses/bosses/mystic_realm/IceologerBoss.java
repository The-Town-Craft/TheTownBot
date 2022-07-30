package net.thetowncraft.townbot.custom_bosses.bosses.mystic_realm;

import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.custom_bosses.BossDungeonEventListener;
import net.thetowncraft.townbot.dimension.MysticRealmListener;
import net.thetowncraft.townbot.custom_items.CustomItem;
import net.thetowncraft.townbot.custom_items.CustomItems;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.Vector;

public class IceologerBoss extends BossDungeonEventListener {

    //Dungeon spawn 25 245 -199
    //Shouldnt pass -66 Z in dungeon until defeated everyone

    @Override
    public void initSpawns() {
        addSpawn(new Vector(22, 126, -168), EntityType.CREEPER);
        addSpawn(new Vector(29, 126, -153), EntityType.DROWNED);

        addSpawn(new Vector(25, 126, -144), EntityType.STRAY);
        addSpawn(new Vector(25, 126, -145), EntityType.CREEPER);
        addSpawn(new Vector(26, 126, -145), EntityType.DROWNED);

        addSpawn(new Vector(20, 126, -138), EntityType.STRAY);
        addSpawn(new Vector(19, 126, -138), EntityType.CREEPER);
        addSpawn(new Vector(18, 126, -138), EntityType.WITHER_SKELETON);
        addSpawn(new Vector(17, 126, -138), EntityType.GUARDIAN);

        //BIG ROOM
        addSpawn(new Vector(5, 126, -128), EntityType.GUARDIAN);
        addSpawn(new Vector(5, 126, -127), EntityType.GUARDIAN);
        addSpawn(new Vector(5, 126, -125), EntityType.GUARDIAN);
        addSpawn(new Vector(5, 126, -124), EntityType.GUARDIAN);

        for(int i = 0; i < 5; i++) {
            addSpawn(new Vector(5, 126 + i, -128), EntityType.ZOMBIE);
            addSpawn(new Vector(5, 126 + i, -127), EntityType.SKELETON);
            addSpawn(new Vector(5, 126 + i, -126), EntityType.CREEPER);
            addSpawn(new Vector(5, 126 + i, -125), EntityType.DROWNED);
            addSpawn(new Vector(5, 126 + i, -124), EntityType.CREEPER);
            addSpawn(new Vector(5, 126 + i, -128), EntityType.SPIDER);
            addSpawn(new Vector(5, 126 + i, -127), EntityType.CAVE_SPIDER);
            addSpawn(new Vector(5, 126 + i, -126), EntityType.STRAY);
            addSpawn(new Vector(5, 126 + i, -125), EntityType.ZOMBIE);
            addSpawn(new Vector(5, 126 + i, -124), EntityType.STRAY);
        }
        //BIG ROOM


        //WINDING ROOM
        addSpawn(new Vector(-5.80, 126, -110), EntityType.ZOMBIE);
        addSpawn(new Vector(-4.73, 126, -118.91), EntityType.GUARDIAN);
        addSpawn(new Vector(-9.50, 126, -80.76), EntityType.CREEPER);
        addSpawn(new Vector(-4, 126, -85), EntityType.DROWNED);
        addSpawn(new Vector(-4.59, 128, -96.62), EntityType.CREEPER);

        addSpawn(new Vector(25, 126, -133), EntityType.CREEPER);
        addSpawn(new Vector(21, 126, -140), EntityType.SPIDER);
        addSpawn(new Vector(25, 126, -170), EntityType.CREEPER);

        addSpawn(new Vector(31, 126, -149), EntityType.CREEPER);
        addSpawn(new Vector(31, 126, -150), EntityType.SKELETON);
        addSpawn(new Vector(31, 126, -151), EntityType.WITHER_SKELETON);
        addSpawn(new Vector(31, 126, -152), EntityType.DROWNED);

    }

    @Override
    public void initAttacks() {
        addAttack(this::dodge, 100, 120);
        addAttack(this::levitate, 0, 120);
        addAttack(this::slam, 40, 240);
        addAttack(this::slam2, 160, 240);
        addAttack(this::vexTnt, 50, 120);

        addAttack(this::tnt, 100, 120);
        addAttack(this::tnt2, 105, 120);
        addAttack(this::tnt2, 110, 120);
        addAttack(this::tnt2, 115, 120);
        addAttack(this::tnt2, 120, 120);
    }

    @Override
    public void dodge() {
        dodge(1);
    }

    public void slam() {
        if(!bossHalfHealth) return;
        slam(20);
    }
    public void slam2() {
        if(bossBar == null) return;
        if(bossBar.getProgress() > 0.2) return;
        slam(20);
    }

    public void tnt() {
        if(boss == null) return;
        if(player == null) return;

        Location pos = player.getLocation();
        TNTPrimed tnt = (TNTPrimed) world.spawnEntity(new Location(world, pos.getX(), pos.getY() + 10, pos.getZ()), EntityType.PRIMED_TNT);
        tnt.setFuseTicks(90);
    }

    public void tnt2() {
        if(boss == null) return;
        if(player == null) return;
        if(!bossHalfHealth) return;

        Location pos = player.getLocation();
        TNTPrimed tnt = (TNTPrimed) world.spawnEntity(new Location(world, pos.getX(), pos.getY() + 10, pos.getZ()), EntityType.PRIMED_TNT);
        tnt.setFuseTicks(90);
    }

    @Override
    public void onBossHalfHealth() {
        super.onBossHalfHealth();
        boss.playEffect(EntityEffect.TOTEM_RESURRECT);
        world.playSound(boss.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 10, 1);
    }

    public void vexTnt() {
        if(boss == null) return;
        for(Entity entity : entities) {
            if(entity == null) continue;
            if(entity.getType() == EntityType.VEX) {
                TNTPrimed tnt = (TNTPrimed) world.spawnEntity(entity.getLocation(), EntityType.PRIMED_TNT);
                tnt.setFuseTicks(0);
                break;
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity entity = event.getEntity();

        if(entity == boss) {
            if(bossHalfHealth && damager != player) {
                event.setCancelled(true);
                return;
            }
        }

        if(!entity.getWorld().getName().equals(world.getName())) return;
        if(entity.getType() != EntityType.PLAYER) return;
        if(player == null) return;
        if(boss == null) return;

        if(damager.getType() == EntityType.PRIMED_TNT) {
            if(bossHalfHealth) player.damage(10,  damager);
        }
        if(damager.getType() == EntityType.EVOKER_FANGS) {
            if(bossHalfHealth) {
                player.damage(15, damager);
            }
        }
    }

    @Override
    public void onSlam(EntityDamageEvent event, Entity boss) {
        super.onSlam(event, boss);
        MysticRealmListener.spawnMysticCreeper(boss.getLocation()).setVelocity(new Vector(1, 0.3, 0));
        MysticRealmListener.spawnMysticCreeper(boss.getLocation()).setVelocity(new Vector(-1, 0.3, 0));
        MysticRealmListener.spawnMysticCreeper(boss.getLocation()).setVelocity(new Vector(0, 0.3, 1));
        MysticRealmListener.spawnMysticCreeper(boss.getLocation()).setVelocity(new Vector(0, 0.3, -1));
    }

    public void levitate() {
        levitate(15);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(event.getBlock().getType() == Material.COMMAND_BLOCK) {
            initBossFight(event.getPlayer());
            event.setCancelled(true);
        }
    }


//    @EventHandler
//    public void entitySpawn(EntitySpawnEvent event) {
//        Location location = event.getLocation();
//        if(location.getWorld().getName().equals(world.getName())) {
//            Entity entity = event.getEntity();
//            if(entity.getType() == EntityType.CREEPER) {
//                if(entity.getCustomName() != null && entity.getCustomName().equals("Mystic Creeper")) return;
//                DimensionEventListener.spawnMysticCreeper(location);
//                event.setCancelled(true);
//            }
//        }
//    }

    @EventHandler
    public void entityExplode(EntityExplodeEvent event) {
        Entity entity = event.getEntity();
        if(entity.getWorld().getName().equals(world.getName())) {
            if(entity.getType() == EntityType.PRIMED_TNT) world.strikeLightning(entity.getLocation());
        }
    }

    @Override
    public String getBossName() {
        return "The Iceologer";
    }

    @Override
    public String getBossDescription() {
        return "An ancient evil held by a prison of ice.";
    }

    @Override
    public EntityType getBaseEntity() {
        return EntityType.EVOKER;
    }

    @Override
    public double getBossHealth() {
        return 500;
    }

    @Override
    public double getCustomAddedBossDamage() {
        return 20;
    }

    @Override
    public ChatColor getBossTitleColor() {
        return ChatColor.AQUA;
    }

    @Override
    public ChatColor getBossDescColor() {
        return ChatColor.DARK_AQUA;
    }

    @Override
    public BarColor getBarColor() {
        return BarColor.BLUE;
    }

    @Override
    public CustomItem getBossItem() {
        return CustomItems.GLACIAL_AMULET;
    }

    @Override
    public String getBossRoleId() {
        return "995837403120013333";
    }

    @Override
    public Sound getBossMusic() {
        return Sound.MUSIC_DISC_PIGSTEP;
    }

    @Override
    public String getDeathMessage() {
        if(inDungeon) return "froze to death in the dungeon of";
        return "was frozen in time by";
    }

    @Override
    public String getBossEmoji() {
        return ":cold_face:";
    }

    @Override
    public Location getBossSpawnLocation() {
        return new Location(Bukkit.getWorld(Plugin.OVERWORLD_NAME + "_thetown_ice_dragon"), 0, 66, 0, 180, 0);
    }

    @Override
    public Location getPlayerBossSpawnLocation() {
        return new Location(Bukkit.getWorld(Plugin.OVERWORLD_NAME + "_thetown_ice_dragon"), 0, 62, -55, 0, 0);
    }

    @Override
    public Location getDungeonSpawnLocation() {
        return new Location(Bukkit.getWorld(Plugin.OVERWORLD_NAME + "_thetown_ice_dragon"), 25, 200, -199, 0, 0);
    }

    @Override
    public int getYBarrier() {
        return 115;
    }
}
