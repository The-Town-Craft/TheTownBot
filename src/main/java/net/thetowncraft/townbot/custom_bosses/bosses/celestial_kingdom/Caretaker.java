package net.thetowncraft.townbot.custom_bosses.bosses.celestial_kingdom;

import net.thetowncraft.townbot.Plugin;
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
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;

import java.awt.*;

public class Caretaker extends BossEventListener {

    @Override
    public void initAttacks() {
        addAttack(this::clearDarkness, 0, 5);
        addAttack(this::lightning, 0, 200);
        addAttack(this::scalingTnt, 100, 200);
    }


    @Override
    public void lightning() {
        if(bossHalfHealth) super.lightning();
    }

    public void clearDarkness() {
        if(player != null && !bossHalfHealth) player.removePotionEffect(PotionEffectType.DARKNESS);
    }

    @EventHandler
    public void onDarkness(EntityPotionEffectEvent event) {
        if(!event.getEntity().equals(player)) return;
        if(!player.getWorld().getName().equals(world.getName())) return;

        player.removePotionEffect(PotionEffectType.DARKNESS);
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
    public String getBossName() {
        return "The Caretaker";
    }

    @Override
    public String getBossDescription() {
        return "Warden of Darkness.";
    }

    @Override
    public String getBossEmoji() {
        return "<:celestial_membrane:1003465278589513799>";
    }

    @Override
    public EntityType getBaseEntity() {
        return EntityType.WARDEN;
    }

    @Override
    public double getBossHealth() {
        return 500;
    }

    @Override
    public ChatColor getBossTitleColor() {
        return ChatColor.BLUE;
    }

    @Override
    public ChatColor getBossDescColor() {
        return ChatColor.DARK_BLUE;
    }

    @Override
    public BarColor getBarColor() {
        return BarColor.BLUE;
    }

    @Override
    public CustomItem getBossItem() {
        return CustomItems.CELESTINE_ELYTRA;
    }

    @Override
    public String getBossRoleId() {
        return "1003464368647835669";
    }

    @Override
    public Sound getBossMusic() {
        return Sound.MUSIC_DISC_13;
    }

    @Override
    public String getDeathMessage() {
        return "was slaughtered by";
    }

    @Override
    public Location getBossSpawnLocation() {
        return new Location(Bukkit.getWorld(Plugin.OVERWORLD_NAME + "_thetown_phantom"), 0, 100, 0);
    }

    @Override
    public Location getPlayerSpawnLocation() {
        return new Location(Bukkit.getWorld(Plugin.OVERWORLD_NAME + "_thetown_phantom"), 20, 100, 0, 180, 0);
    }
}
