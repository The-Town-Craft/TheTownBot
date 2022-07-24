package net.thetowncraft.townbot.custom_items;

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
    public int getCustomModelData() {
        return 1;
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
        if(!canUse(player)) {
            player.sendMessage(ChatColor.RED + "You must defeat the Noxious Chicken to use this item!");
            return;
        }
        if(player.isSprinting()) {
            player.setVelocity(new Vector(velocity.getX() * 3, velocity.getY(), velocity.getZ() * 3));
        }
        else {
            player.setVelocity(new Vector(velocity.getX() * 4, velocity.getY(), velocity.getZ() * 4));
        }
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GHAST_SHOOT, 0.5f, 1);
    }

    @Override
    public boolean shines() {
        return false;
    }
}
