package net.thetowncraft.townbot.custom_bosses;

import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.items.CustomItem;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class BossEventListener implements Listener {

    private static final List<BossEventListener> LISTENERS = new ArrayList<>();

    public boolean bossBeingChallenged = false;
    public boolean bossHalfHealth = false;
    public LivingEntity boss = null;
    public Player player = null;
    public BossBar bossBar = null;
    public final World world;

    public BossEventListener() {
        this.world = this.getBossSpawnLocation().getWorld();
        LISTENERS.add(this);
        initAttacks();
        initDefaultAttacks();
        initEquipment();
    }

    @EventHandler
    public void onEntityResurrect(EntityResurrectEvent event) {
        LivingEntity entity = event.getEntity();
        if(entity.getType() == EntityType.PLAYER && entity.getWorld().getName().equals(world.getName())) {
            event.setCancelled(true);
        }
    }

    public void initDefaultAttacks() {
        this.addAttack(this::focusPlayer, 30, 30);
    }

    public void initAttacks() {}
    public void initEquipment() {}

    public void addAttack(Runnable attack, long delay, long period) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Plugin.get(), () -> {
            if(bossBeingChallenged && boss != null && player != null) attack.run();
        }, delay, period);
    }

    public void focusPlayer() {
        if(boss instanceof Mob) {
            ((Mob) boss).setTarget(player);
        }
    }

    public void dodge() {
        dodge(5);
    }

    public void dodge(int speed) {
        Random random = new Random();
        int dir = random.nextInt(4);
        if(dir == 0) {
            boss.setVelocity(new Vector(speed, 0, 0));
        }
        else if(dir == 1) {
            boss.setVelocity(new Vector(-speed, 0, 0));
        }
        else if(dir == 2) {
            boss.setVelocity(new Vector(0, 0, speed));
        }
        else {
            boss.setVelocity(new Vector(0, 0, -speed));
        }

        boss.getWorld().playSound(boss.getLocation(), Sound.ENTITY_WITHER_BREAK_BLOCK, 5, 1);
    }

    public boolean initBossFight(Player player) {
        if(bossBeingChallenged) {
            sendBossBeingChallengedMessage(player);
            return false;
        }

        bossBeingChallenged = true;

        for(PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        sendBossTitleEffects(player);
        setUpBoss(player);
        return true;
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
        this.player = player;
        bossHalfHealth = false;
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        world.setGameRule(GameRule.MOB_GRIEFING, false);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.get(), () -> {
            player.teleport(this.getPlayerSpawnLocation());
            Location bossSpawn = this.getBossSpawnLocation();
            player.playSound(new Location(world, bossSpawn.getX(), bossSpawn.getY() + 200, bossSpawn.getZ()), this.getBossMusic(), 1000, 1);
            if(player.getGameMode() == GameMode.SURVIVAL) player.setGameMode(GameMode.ADVENTURE);

            world.setGameRule(GameRule.MOB_GRIEFING, false);

            for(Entity entity : player.getNearbyEntities(200, 200, 200)) {
                if(!(entity instanceof Player)) {
                    entity.remove();
                }
            }
            ItemStack[] stacks = {};
            boss = (LivingEntity) world.spawnEntity(bossSpawn, this.getBaseEntity());
            boss.getEquipment().setArmorContents(stacks);
            boss.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(this.getBossHealth());
            boss.setHealth(this.getBossHealth());
            boss.setCustomName(this.getBossName());
            boss.setCustomNameVisible(false);

            if(boss instanceof Boss) {
                Boss bossEntity = (Boss) this.boss;
                BossBar bar = bossEntity.getBossBar();
                if(bar == null) {
                    setUpBossBar(player);
                }
                else {
                    bar.setColor(this.getBarColor());
                    this.bossBar = bar;
                }
            }
            else {
                setUpBossBar(player);
            }

            bossBeingChallenged = true;
            onBossSpawn(boss, player);
        }, 70);
    }

    public void setUpBossBar(Player player) {
        bossBar = Bukkit.createBossBar(this.getBossName(), this.getBarColor(), BarStyle.SOLID);
        bossBar.addPlayer(player);
    }

    public void onBossSpawn(LivingEntity boss, Player player) {

    }

    public void onBossHalfHealth() {

    }

    @EventHandler
    public final void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if(entity.getWorld().getName().equals(world.getName())) {
            event.getDrops().clear();
        }
        if(entity.equals(boss)) {
            boss = null;
            bossBar.removeAll();
            event.getDrops().add(this.getBossItem().createItemStack(1));
        }
    }

    @EventHandler
    public final void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if(!player.getWorld().getName().equals(world.getName())) return;
        event.setKeepInventory(true);
        event.setKeepLevel(true);
        event.getDrops().clear();
        player.setGameMode(GameMode.SURVIVAL);
        bossBeingChallenged = false;
        boss.remove();
        boss = null;
        this.player = null;
        bossBar.removeAll();
    }



    @EventHandler
    public final void onItemDrop(PlayerDropItemEvent event) {
        if(event.getPlayer().getWorld().getName().equals(world.getName())) event.setCancelled(true);
    }

    @EventHandler
    public final void onBlockExplode(BlockExplodeEvent event) {
        if(event.getBlock().getWorld().getName().equals(world.getName())) {
            event.blockList().clear();
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if(event.getEntity().getWorld().getName().equals(world.getName())) {
            event.blockList().clear();
        }
    }

    @EventHandler
    public final void onBlockBurn(BlockBurnEvent event) {
        if(!event.getBlock().getWorld().getName().equals(world.getName())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public final void onConsume(PlayerItemConsumeEvent event) {
        Material item = event.getItem().getType();

        if(item == Material.CHORUS_FRUIT) {
            event.setCancelled(true);
            return;
        }
        if(item == Material.MILK_BUCKET) {
            event.setCancelled(true);
            return;
        }
        if(item == Material.POTION) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public final void onPlayerQuit(PlayerQuitEvent event) {
        if(world.getName().equals(event.getPlayer().getWorld().getName())) {
            if(event.getPlayer().getGameMode() != GameMode.ADVENTURE) return;
            event.getPlayer().setHealth(0);
            boss.remove();
            bossBar = null;
            bossBeingChallenged = false;
        }
    }

    @EventHandler
    public final void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if(entity.getWorld().getName().equals(world.getName())) {
            if(entity.getType() == EntityType.DROPPED_ITEM) {
                event.setCancelled(true);
            }
            if(entity.equals(boss)) {
                if(bossBar != null) {
                    if(!(boss instanceof Boss)) bossBar.setProgress(boss.getHealth() / boss.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
                    if(bossBar.getProgress() <= 0.5 && !bossHalfHealth) {
                        bossHalfHealth = true;
                        onBossHalfHealth();
                    }
                }
            }
        }
    }

    @EventHandler
    public final void onPlayerItemDamage(PlayerItemDamageEvent event) {
        Player player = event.getPlayer();
        if(player.getWorld().getName().equals(world.getName())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public final void onItemPickup(EntityPickupItemEvent event) {
        LivingEntity entity = event.getEntity();
        if(entity.getWorld().getName().equals(world.getName()) && entity.getType() == EntityType.PLAYER) {
            Player player = (Player) entity;
            if(event.getItem().getItemStack().getItemMeta().hasLore() && boss == null) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 60, 0, true, false, false));
                Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.get(), () -> {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30, 0, true, false, false));

                    Location bedLocation = player.getBedSpawnLocation();
                    if(bedLocation == null) {
                        player.teleport(new Location(Bukkit.getWorld("world_1597802541"),-161, 64, 230));
                        player.setGameMode(GameMode.SURVIVAL);
                        bossBeingChallenged = false;
                        this.player = null;
                        return;
                    }
                    player.teleport(bedLocation);
                    player.setGameMode(GameMode.SURVIVAL);
                    bossBeingChallenged = false;
                    this.player = null;
                }, 60);
            }
        }
    }

    @EventHandler
    public final void onBlockIgnite(BlockIgniteEvent event) {
        if(event.getBlock().getWorld().getName().equals(world.getName())) {
            event.setCancelled(true);
        }
    }

    public abstract String getBossName();
    public abstract String getBossDescription();
    public abstract EntityType getBaseEntity();
    public abstract double getBossHealth();
    public abstract ChatColor getBossTitleColor();
    public abstract ChatColor getBossDescColor();
    public abstract BarColor getBarColor();
    public abstract CustomItem getBossItem();
    public abstract Sound getBossMusic();

    public abstract Location getBossSpawnLocation();
    public abstract Location getPlayerSpawnLocation();
}
