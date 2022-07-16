package net.thetowncraft.townbot.custom_bosses.bosses;

import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.custom_bosses.BossEventListener;
import net.thetowncraft.townbot.custom_items.CustomItem;
import net.thetowncraft.townbot.custom_items.CustomItems;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class BlazingWitherBoss extends BossEventListener {

    public static final String bossWorldName = "world_1597802541_thetown_void";

    @Override
    public void initAttacks() {
        this.addAttack(this::lightning, 200, 200);
        this.addAttack(this::dodge, 80, 80);
        this.addAttack(this::tnt, 40, 160);
    }

    public void lightning() {
        Location targetPos = player.getLocation();

        Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.get(), () -> {
            world.spawnEntity(targetPos, EntityType.LIGHTNING);
        }, 30);
    }

    @Override
    public void dodge() {
        super.dodge(5);
    }

    public void tnt() {
        world.playSound(boss.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 10, 1);
        summonTNT(new Vector(0,0,0), 30);
        if(boss.getHealth() > boss.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() / 2) return;
        Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.get(), () -> {
            summonTNT(new Vector(0.5, 0, 0));
            summonTNT(new Vector(-0.5, 0, 0));
            summonTNT(new Vector(0, 0, 0.5));
            summonTNT(new Vector(0, 0, -0.5));
        }, 20);
    }

    private void summonTNT(Vector velocity) {
        summonTNT(velocity, 100);
    }
    private void summonTNT(Vector velocity, int fuse) {
        TNTPrimed tnt = (TNTPrimed) boss.getWorld().spawnEntity(boss.getLocation(), EntityType.PRIMED_TNT);
        tnt.setFuseTicks(fuse);
        tnt.setVelocity(velocity);
        tnt.setSource(boss);
    }

    @EventHandler
    public void onFlintAndSteel(PlayerInteractEvent event) {
        Material centerBlock = Material.CRYING_OBSIDIAN;
        Material surroundingBlock = Material.NETHERRACK;

        Block block = event.getClickedBlock();
        Player player = event.getPlayer();

        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(block == null || block.getType() != centerBlock) return;
        if(player.getInventory().getItemInMainHand().getType() != Material.FLINT_AND_STEEL) return;

        World world = block.getWorld();
        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();

        if(world.getBlockAt(x + 1, y, z).getType() != surroundingBlock) return;
        if(world.getBlockAt(x - 1, y, z).getType() != surroundingBlock) return;


        if(world.getBlockAt(x, y, z + 1).getType() != surroundingBlock) return;
        if(world.getBlockAt(x, y, z - 1).getType() != surroundingBlock) return;

        initBossFight(player);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if(!entity.getWorld().getName().equals(world.getName())) return;

        if(entity instanceof Player) {

            if(event.getCause() != EntityDamageEvent.DamageCause.FIRE_TICK) entity.setFireTicks(100);

            if(event.getCause() == EntityDamageEvent.DamageCause.LIGHTNING) {
                ((Player) entity).setHealth(2);
                entity.setVelocity(new Vector(entity.getVelocity().getX(), 1, entity.getVelocity().getZ()));
                ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200,1, false, false, false));
                ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100,1));
            }
            if(event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100,1));
            }
        }
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        if(event.getEntity().getWorld().getName().equals(world.getName())) {
            world.spawnEntity(event.getLocation(), EntityType.LIGHTNING);
        }
    }

    @Override
    public String getBossName() {
        return "The Blazing Wither";
    }

    @Override
    public String getBossDescription() {
        return "King of the NetherVoid";
    }

    @Override
    public EntityType getBaseEntity() {
        return EntityType.WITHER;
    }

    @Override
    public double getBossHealth() {
        return 145;
    }

    @Override
    public ChatColor getBossTitleColor() {
        return ChatColor.RED;
    }

    @Override
    public ChatColor getBossDescColor() {
        return ChatColor.RED;
    }

    @Override
    public BarColor getBarColor() {
        return BarColor.RED;
    }

    @Override
    public CustomItem getBossItem() {
        return CustomItems.BLAZING_THUNDERSTAR;
    }

    @Override
    public String getBossRoleId() {
        return "995754819446906910";
    }

    @Override
    public Sound getBossMusic() {
        return Sound.MUSIC_DISC_PIGSTEP;
    }

    @Override
    public String getDeathMessage() {
        return "was damned to hell by";
    }

    @Override
    public String getBossEmoji() {
        return ":fire:";
    }

    @Override
    public Location getBossSpawnLocation() {
        return new Location(Bukkit.getWorld(bossWorldName), 54, 140, 0, 90.0f, 0.0f);
    }

    @Override
    public Location getPlayerSpawnLocation() {
        return new Location(Bukkit.getWorld(bossWorldName), 27, 122, 0, -90.0f, 0.0f);
    }
}
