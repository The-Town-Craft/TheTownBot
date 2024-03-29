package net.thetowncraft.townbot.custom_bosses.bosses.mystic_realm;

import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.custom_bosses.BossEventListener;
import net.thetowncraft.townbot.custom_items.CustomItem;
import net.thetowncraft.townbot.custom_items.CustomItems;
import net.thetowncraft.townbot.dimension.MysticRealmListener;
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

        if(!world.getName().equals(MysticRealmListener.MYSTIC_REALM)) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You must be in the Mystic Realm to use this altar.");
            return;
        }

        initBossFight(player);
    }

    @Override
    public boolean superLightning() {
        return true;
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
