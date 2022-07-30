package net.thetowncraft.townbot.custom_bosses.bosses.celestial_kingdom;

import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.custom_bosses.BossEventListener;
import net.thetowncraft.townbot.custom_items.CustomItem;
import net.thetowncraft.townbot.custom_items.CustomItems;
import net.thetowncraft.townbot.dimension.CelestialKingdomListener;
import net.thetowncraft.townbot.dimension.MysticRealmListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class AcidicCreeperBoss extends BossEventListener {

    @Override
    public void initAttacks() {
        addAttack(this::dodge, 0, 200);
        addAttack(this::slam, 100, 200);
    }

    @Override
    public void onSlam(EntityDamageEvent event, Entity boss) {
        super.onSlam(event, boss);
        Location pos = event.getEntity().getLocation();
        pos.getWorld().playSound(pos, Sound.ENTITY_SLIME_SQUISH, 1, 1);
    }

    @EventHandler
    public void entityExplode(EntityExplodeEvent event) {
        Entity entity = event.getEntity();
        if(!entity.getWorld().getName().equals(world.getName())) return;

        if(entity.getType() == EntityType.CREEPER) {
            respawnPlayer();
        }
    }

    public void slam() {
        slam(40);
    }

    @Override
    public void dodge() {
        dodge(1);
    }

    @EventHandler
    public void onPlayerEat(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if(item.getType() == CustomItems.ACIDIC_ARTIFACT.getBaseItem() && item.getItemMeta() != null) {
            ItemMeta itemMeta = item.getItemMeta();
            if(itemMeta.hasCustomModelData() && itemMeta.getCustomModelData() == 2) {
                if(!player.getWorld().getName().equals(CelestialKingdomListener.CELESTIAL_KINGDOM)) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(ChatColor.RED + "This item can only be consumed in Celestial Kingdom.");
                    return;
                }
                event.setCancelled(true);
                item.setAmount(item.getAmount() - 1);
                event.setItem(item);
                initBossFight(player);
            }
        }
    }

    @Override
    public void spawnBoss() {
        super.spawnBoss();
        Creeper creeper = (Creeper) this.boss;
        creeper.setPowered(true);
    }

    @Override
    public String getBossName() {
        return "Acidic Creeper";
    }

    @Override
    public String getBossDescription() {
        return "King of the Slimes";
    }

    @Override
    public String getBossEmoji() {
        return "<:acidic_artifact:1002476936972488714> ";
    }

    @Override
    public EntityType getBaseEntity() {
        return EntityType.CREEPER;
    }

    @Override
    public double getBossHealth() {
        return 500;
    }

    @Override
    public ChatColor getBossTitleColor() {
        return ChatColor.YELLOW;
    }

    @Override
    public ChatColor getBossDescColor() {
        return ChatColor.YELLOW;
    }

    @Override
    public BarColor getBarColor() {
        return BarColor.YELLOW;
    }

    @Override
    public CustomItem getBossItem() {
        return CustomItems.ACIDIC_SLIMEBALL;
    }

    @Override
    public String getBossRoleId() {
        return "1002477408600981544";
    }

    @Override
    public Sound getBossMusic() {
        return Sound.MUSIC_DISC_PIGSTEP;
    }

    @Override
    public String getDeathMessage() {
        return "was squished by";
    }

    @Override
    public Location getBossSpawnLocation() {
        return new Location(Bukkit.getWorld(Plugin.OVERWORLD_NAME + "_thetown_slime"), 0, 100, 0, 0, 0);
    }

    @Override
    public Location getPlayerSpawnLocation() {
        return new Location(Bukkit.getWorld(Plugin.OVERWORLD_NAME + "_thetown_slime"), 0, 100, 50, 180, 0);
    }
}
