package net.thetowncraft.townbot.custom_bosses.bosses.celestial_kingdom;

import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.custom_bosses.BossEventListener;
import net.thetowncraft.townbot.custom_items.CustomItem;
import net.thetowncraft.townbot.custom_items.CustomItems;
import net.thetowncraft.townbot.dimension.CelestialKingdomListener;
import net.thetowncraft.townbot.dimension.MysticRealmListener;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class AcidicCreeperBoss extends BossEventListener {

    @Override
    public void initAttacks() {
        addAttack(this::zombie, 0, 400);
        addAttack(this::slime, 200, 400);

        addAttack(this::dodge, 50, 100);
        addAttack(this::poison, 75, 100);
        addAttack(this::slam, 100, 200);
        addAttack(this::tnt, 150, 200);
    }

    public void tnt() {
        summonTNT(new Vector(0,0,0));
    }

    public void poison() {
        world.playSound(boss.getLocation(), Sound.ENTITY_LINGERING_POTION_THROW, 5f,  0.7f);

        Location pos = player.getLocation();
        ThrownPotion potion = (ThrownPotion) world.spawnEntity(new Location(world, pos.getX(), pos.getY() + 10, pos.getZ()), EntityType.SPLASH_POTION);
        ItemStack potionItem = new ItemStack(Material.LINGERING_POTION);
        PotionMeta meta = ((PotionMeta) potionItem.getItemMeta());
        if(meta == null) {
            System.out.println("Meta is null! " + getClass());
            return;
        }

        meta.setColor(Color.GREEN);
        meta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 100, 1), true);
        meta.addCustomEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 1), true);
        potionItem.setItemMeta(meta);
        potion.setItem(potionItem);
    }

    public void zombie() {
        if(world == null) return;
        if(boss == null) return;
        if(!bossHalfHealth) return;
        Zombie zombie = (Zombie) world.spawnEntity(boss.getLocation(), EntityType.ZOMBIE);
        zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40);
        zombie.setHealth(40);
        EntityEquipment equipment = zombie.getEquipment();
        if(equipment == null) return;
        equipment.setItemInMainHand(CustomItems.HUNTER_SWORD.createItemStack(1));
    }

    public void slime() {
        CelestialKingdomListener.spawnAcidicSlime(boss.getLocation());
    }

    @Override
    public void onSlam(EntityDamageEvent event, Entity boss) {
        super.onSlam(event, boss);
        Location pos = event.getEntity().getLocation();
        pos.getWorld().playSound(pos, Sound.ENTITY_SLIME_SQUISH, 1, 1);
        if(bossHalfHealth) {
            summonTNT(new Vector(0.5, 0, 0));
            summonTNT(new Vector(-0.5, 0, 0));
            summonTNT(new Vector(0, 0, 0.5));
            summonTNT(new Vector(0, 0, -0.5));
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

    @EventHandler
    public void entityExplode(EntityExplodeEvent event) {
        Entity entity = event.getEntity();
        if(!entity.getWorld().getName().equals(world.getName())) return;

        if(entity instanceof Creeper) {
            respawnPlayer();
        }
    }

    @Override
    public void setUpArena(Player player) {
        super.setUpArena(player);
        world.setGameRule(GameRule.MOB_GRIEFING, true);
    }

    public void slam() {
        if(bossHalfHealth) slam(50);
        else levitate(30);
    }

    @Override
    public void dodge() {
        dodge(1);
        tnt();
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
                item.setAmount(item.getAmount() - 1);
                event.setItem(item);
                event.setCancelled(true);
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
        return 300;
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
        return "was obliterated by";
    }

    @Override
    public Location getBossSpawnLocation() {
        return new Location(Bukkit.getWorld(Plugin.OVERWORLD_NAME + "_thetown_slime"), 0, 100, 375, 180, 0);
    }

    @Override
    public Location getPlayerSpawnLocation() {
        return new Location(Bukkit.getWorld(Plugin.OVERWORLD_NAME + "_thetown_slime"), 0, 100, 365, 0, 0);
    }
}
