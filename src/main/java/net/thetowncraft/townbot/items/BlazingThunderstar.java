package net.thetowncraft.townbot.items;

import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class BlazingThunderstar extends CustomItem {

    @Override
    public void procOnHit(Player player, int itemAmount, LivingEntity target, World world) {
        LightningStrike lightning = (LightningStrike) world.spawnEntity(target.getLocation(), EntityType.LIGHTNING);
        target.damage(10 * itemAmount, lightning);
        target.addPotionEffect(PotionEffectType.WITHER.createEffect(20 * itemAmount, itemAmount - 1));
        target.setFireTicks(20 * itemAmount);
        int radius = 2 * itemAmount;
        List<Entity> entities = target.getNearbyEntities(radius, radius, radius);
        for(Entity entity : entities) {
            if(entity == player) continue;
            if(entity instanceof LivingEntity) {
                ((LivingEntity) entity).addPotionEffect(PotionEffectType.WITHER.createEffect(20 * itemAmount, itemAmount - 1));
                entity.setFireTicks(20 * itemAmount);
            }

        }
    }

    @Override
    public String getName() {
        return ChatColor.RED + "The Blazing Thunderstar";
    }

    @Override
    public String getDescription() {
        return ChatColor.RED + "Unleash the anger of The Blazing Wither";
    }

    @Override
    public Material getBaseItem() {
        return Material.NETHER_STAR;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public boolean shines() {
        return false;
    }
}
