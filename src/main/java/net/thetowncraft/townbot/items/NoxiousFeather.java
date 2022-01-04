package net.thetowncraft.townbot.items;

import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class NoxiousFeather extends CustomItem {


    @Override
    public String getName() {
        return ChatColor.GREEN + "The Noxious Feather";
    }

    @Override
    public String getDescription() {
        return ChatColor.DARK_GREEN + "A horizontal jump boost.";
    }

    @Override
    public Material getBaseItem() {
        return Material.FEATHER;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.EPIC;
    }

    @Override
    public void onPlayerJump(Player player, int amount) {
        Vector velocity = player.getVelocity();
        if(player.isSprinting()) {
            player.setVelocity(new Vector(velocity.getX() * (amount + 2), velocity.getY(), velocity.getZ() * (amount + 2)));
        }
        else {
            player.setVelocity(new Vector(velocity.getX() * (amount * 4), velocity.getY(), velocity.getZ() * (amount * 4)));
        }
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GHAST_SHOOT, 3, 1);
    }

    @Override
    public boolean shines() {
        return false;
    }
}
