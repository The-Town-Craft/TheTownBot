package net.thetowncraft.townbot.custom_bosses;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.custom_items.CustomItem;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
import net.thetowncraft.townbot.util.Constants;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.data.type.TNT;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Random;

public abstract class BossEventListener implements Listener {

    private static final List<BossEventListener> LISTENERS = new ArrayList<>();

    public boolean bossBeingChallenged = false;
    public boolean bossHalfHealth = false;
    public LivingEntity boss = null;
    public Player player = null;
    public Location prevPlayerLocation;
    public BossBar bossBar = null;
    public final World world;
    public List<Entity> entities;
    public boolean slam;
    public double slamDamage;

    public BossEventListener() {
        entities = new ArrayList<>();
        this.world = this.getBossSpawnLocation().getWorld();
        getBossItem().setBoss(this);
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
        this.addAttack(this::fillHunger, 10, 10);
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
    public void fillHunger() {
        player.setFoodLevel(20);
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

    public void levitate(int damage) {
        if(boss == null) return;
        boss.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 30, 1, true, false, false));
        world.playSound(boss.getLocation(), Sound.ENTITY_EVOKER_PREPARE_SUMMON, 10, 1);
        Location location = player.getLocation();
        Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.get(), () -> {
            if(boss != null) {
                boss.teleport(location);
                for(Entity entity : boss.getNearbyEntities(3, 3, 3)) {
                    if(entity instanceof Player) {
                        Player player = (Player) entity;
                        player.setVelocity(new Vector(0, 1, 0));
                        player.damage(damage, boss);
                    }
                }
                world.playSound(boss.getLocation(), Sound.ENTITY_WITHER_BREAK_BLOCK, 5, 1);
                world.playSound(boss.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 5, 1);
                world.playSound(boss.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 5, 1);
            }
        }, 30);
    }

    public boolean initBossFight(Player player) {
        if(bossBeingChallenged) {
            sendBossBeingChallengedMessage(player);
            return false;
        }

        player.setGameMode(GameMode.ADVENTURE);
        player.setInvulnerable(true);

        bossBeingChallenged = true;
        prevPlayerLocation = player.getLocation();

        for(PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        sendBossChallengeMsg(player);
        sendBossTitleEffects(player);
        setUpArena(player);
        return true;
    }

    public void sendBossChallengeMsg(Player player) {
        Bukkit.getServer().broadcastMessage(player.getName() + " " + getChallengeMessage() + " " + getBossTitleColor() + getBossName());
        Bot.jda.getTextChannelById(Constants.MC_CHAT).sendMessage(">>> " + getBossEmoji() + " **" + player.getName() + "** " + getChallengeMessage() + " **" + getBossName() + "**").queue();
    }

    public String getChallengeMessage() {
        return "has challenged";
    }

    public void sendVictoryMsg() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setDescription("```\n[" + player.getName() + " has defeated " + getBossName() + "]\n```");
        embed.setColor(Constants.GREEN);
        Bot.jda.getTextChannelById(Constants.MC_CHAT).sendMessage(embed.build()).queue();

        Bukkit.getServer().broadcastMessage(player.getName() + " has defeated " + getBossTitleColor() + getBossName());
    }

    public void sendBossBeingChallengedMessage(Player player) {
        player.sendMessage(ChatColor.RED + "This boss is already being challenged");
    }

    public void addBossRole() {
        if(player == null) return;

        Member member = AccountManager.getInstance().getDiscordMember(player);
        if(member == null) return;

        Role role = Bot.jda.getRoleById(getBossRoleId());
        if(role == null) return;

        member.getGuild().addRoleToMember(member, role).queue();
    }

    public void sendBossTitleEffects(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 5, false, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 1, false, false, false));
        player.sendTitle(this.getBossTitleColor() + "" + ChatColor.BOLD + this.getBossName(), this.getBossDescColor() + this.getBossDescription(), 10, 70, 20);
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 100, 1);
    }

    public void setUpArena(Player player) {
        this.player = player;
        bossHalfHealth = false;
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        world.setGameRule(GameRule.MOB_GRIEFING, false);
        world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.get(), () -> {
            player.teleport(this.getPlayerSpawnLocation());
            playBossMusic();
            if(player.getGameMode() == GameMode.SURVIVAL) player.setGameMode(GameMode.ADVENTURE);

            world.setGameRule(GameRule.MOB_GRIEFING, false);

            spawnBoss();

            checkBar();

            bossBeingChallenged = true;
            player.setInvulnerable(false);
            onBossSpawn(boss, player);
            player.teleport(this.getPlayerSpawnLocation());
        }, 70);
    }

    public void clearEntities() {
        try {
            for(Entity entity : new ArrayList<>(entities)) {
                if(entity != null) entity.remove();
            }
            entities.clear();
        }
        catch (ConcurrentModificationException ex) {
            clearEntities();
        }
    }

    public void checkBar() {
        if(boss instanceof Wither) {
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
    }

    public void spawnBoss() {
        ItemStack[] stacks = {};
        boss = (LivingEntity) world.spawnEntity(getBossSpawnLocation(), this.getBaseEntity());
        boss.getEquipment().setArmorContents(stacks);
        boss.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(this.getBossHealth());
        boss.setHealth(this.getBossHealth());
        boss.setCustomName(this.getBossName());
        boss.setCustomNameVisible(false);
        bossHalfHealth = false;
        if(boss instanceof Wither) {
            ((Wither) boss).getBossBar().setColor(getBarColor());
            this.bossBar = ((Wither) boss).getBossBar();
        }
        System.out.println("SPAWNED BOSS");
    }

    public void respawnPlayer() {
        player.teleport(this.getPlayerSpawnLocation());
        player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
        if(boss!= null) boss.remove();
        clearEntities();
        spawnBoss();
        if(bossBar != null) bossBar.setProgress(1.0);
        player.stopAllSounds();
        player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 10, 1);
        player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_DEATH, 10, 1);
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_DEATH, 10, 1);
        player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 10, 1);
        player.removePotionEffect(PotionEffectType.WITHER);
        player.setFireTicks(0);
        player.setFreezeTicks(0);
        playBossMusic();
        Bukkit.getServer().broadcastMessage(player.getName() + " " + getDeathMessage() + " " + getBossTitleColor() + getBossName());

        EmbedBuilder embed = new EmbedBuilder();
        embed.setDescription("```css\n[" + player.getName() + " " + getDeathMessage() + " " + getBossName() + "]\n```");
        embed.setColor(0xb83838);
        Bot.jda.getTextChannelById(Constants.MC_CHAT).sendMessage(embed.build()).queue();
    }

    public void setUpBossBar(Player player) {
        bossBar = Bukkit.createBossBar(this.getBossName(), this.getBarColor(), BarStyle.SOLID);
        bossBar.addPlayer(player);
    }

    public void playBossMusic() {
        Location bossSpawn = this.getBossSpawnLocation();
        player.playSound(new Location(world, bossSpawn.getX(), bossSpawn.getY() + 200, bossSpawn.getZ()), this.getBossMusic(), 1000, 1);
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
            onBossDeath(event);
        }
    }

    public void onBossDeath(EntityDeathEvent event) {
        boss = null;
        bossBar.removeAll();
        event.getDrops().add(this.getBossItem().createItemStack(1));
        addBossRole();
        sendVictoryMsg();
        clearEntities();
        if(player != null) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 10));
        }
    }

    public void slam(double damage) {
        if(boss.isInvulnerable()) return;
        slam = true;
        boss.setVelocity(new Vector(0, 2, 0));
        slamDamage = damage;
        world.playSound(boss.getLocation(), Sound.ENTITY_EVOKER_PREPARE_SUMMON, 10, 0.5f);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.get(), () -> {
            if(boss == null) return;
            boss.teleport(new Location(world, player.getLocation().getX(), boss.getLocation().getY(), player.getLocation().getZ()));
        }, 15);
    }

    @EventHandler
    public final void onFallDamageSlam(EntityDamageEvent event) {
        if(!slam) return;
        Entity damaged = event.getEntity();
        if(!damaged.getWorld().getName().equals(world.getName())) return;

        if(boss == null) return;

        if(event.getCause() == EntityDamageEvent.DamageCause.FALL && event.getEntity().getType() == getBaseEntity()) {
            onSlam(event, damaged);
        }
    }

    public void onSlam(EntityDamageEvent event, Entity boss) {
        event.setCancelled(true);
        List<Entity> entities = boss.getNearbyEntities(4, 4, 4);
        for(Entity entity : entities) {
            if(entity instanceof Player) {
                Player player = (Player) entity;
                player.damage(slamDamage, boss);
                player.setVelocity(new Vector(0, 1, 0));
            }
        }
        world.playSound(this.boss.getLocation(), Sound.ENTITY_WITHER_BREAK_BLOCK, 5, 1);
        world.playSound(this.boss.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 5, 1);
    }

    @EventHandler
    public final void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if(!player.getWorld().getName().equals(world.getName())) return;
        event.setKeepInventory(true);
        event.setKeepLevel(true);
        event.getDrops().clear();
        clearEntities();
        resetFight();
        if(prevPlayerLocation != null) player.teleport(prevPlayerLocation);
    }

    public void resetFight() {
        if(player != null) player.setGameMode(GameMode.SURVIVAL);
        bossBeingChallenged = false;
        if(boss != null) boss.remove();
        boss = null;
        this.player = null;
        if(bossBar != null) bossBar.removeAll();
    }

    @EventHandler
    public void onClickSign(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(!player.getWorld().getName().equals(world.getName())) return;
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(event.getClickedBlock().getType().name().contains("SIGN")) {
                resetFight();
                player.teleport(prevPlayerLocation);
            }
        }
    }

    @EventHandler
    public final void onItemDrop(PlayerDropItemEvent event) {
        if(event.getPlayer().getWorld().getName().equals(world.getName())) {
            if(boss != null) {
                if(this instanceof BossDungeonEventListener) {
                    BossDungeonEventListener dungeon = (BossDungeonEventListener) this;
                    if(dungeon.inDungeon) return;
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public final void onBlockExplode(BlockExplodeEvent event) {
        if(event.getBlock().getWorld().getName().equals(world.getName())) {
            event.blockList().clear();
        }
    }

    @EventHandler
    public final void onEntityExplode(EntityExplodeEvent event) {
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
        if(!event.getPlayer().getWorld().getName().equals(world.getName())) return;

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
            resetFight();
            event.getPlayer().setHealth(0);
        }
    }

    @EventHandler
    public final void onEntitySpawn(EntitySpawnEvent event) {
        EntityType type = event.getEntity().getType();
        if(!event.getLocation().getWorld().getName().equals(world.getName())) return;

        if(type == EntityType.SPLASH_POTION) {
            event.setCancelled(true);
            return;
        }

        if(event.isCancelled()) return;

        Entity entity = event.getEntity();
        if(entity.getType() == EntityType.PLAYER) return;
        entities.add(entity);
    }

    @EventHandler
    public final void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if(event.isCancelled()) return;
        if (entity.getWorld().getName().equals(world.getName())) {
            if (entity.getType() == EntityType.DROPPED_ITEM) {
                event.setCancelled(true);
            }
            if(entity.equals(boss)) {
                if(bossBar != null) {
                    if(!(boss instanceof Wither) && !event.isCancelled()) {
                        double newProgress = (boss.getHealth()) / boss.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
                        if(newProgress >= 0.0 && newProgress <= 1.0) bossBar.setProgress(newProgress);
                    }
                    if(bossBar.getProgress() <= 0.5 && !bossHalfHealth) {
                        bossHalfHealth = true;
                        onBossHalfHealth();
                    }
                }
            }
            if (entity instanceof Player) {
                if (player.getHealth() - event.getFinalDamage() < 1.0) {
                    event.setCancelled(true);
                    respawnPlayer();
                }
            }
        }
    }

    @EventHandler
    public final void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

        if(event.getEntity().equals(boss)) {
            if(event.getDamager() instanceof TNTPrimed || event.getDamager() instanceof TNT || event.getDamager() instanceof LightningStrike) {
                event.setCancelled(true);
                return;
            }
        }

        if(event.getDamager().equals(boss)) {
            event.setDamage(event.getDamage() + this.getCustomAddedBossDamage());
            if(event.getEntity().equals(player)) {
                if(player.getHealth() - event.getFinalDamage() < 1.0) {
                    event.setCancelled(true);
                    respawnPlayer();
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
                    player.teleport(prevPlayerLocation);
                    resetFight();
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

    public double getCustomAddedBossDamage() {
        return 0.0;
    }

    public abstract String getBossName();
    public abstract String getBossDescription();
    public abstract String getBossEmoji();
    public abstract EntityType getBaseEntity();
    public abstract double getBossHealth();
    public abstract ChatColor getBossTitleColor();
    public abstract ChatColor getBossDescColor();
    public abstract BarColor getBarColor();
    public abstract CustomItem getBossItem();
    public abstract String getBossRoleId();
    public abstract Sound getBossMusic();

    /**
     *
     * @return the death message text in between the player name and the boss name
     */
    public abstract String getDeathMessage();

    public abstract Location getBossSpawnLocation();
    public abstract Location getPlayerSpawnLocation();
}
