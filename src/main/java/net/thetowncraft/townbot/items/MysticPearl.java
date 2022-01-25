package net.thetowncraft.townbot.items;

import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class MysticPearl extends CustomItem {

    @Override
    public String getName() {
        return "Mystic Pearl";
    }

    @Override
    public String getDescription() {
        return ChatColor.DARK_PURPLE + "A corrupted ender pearl.";
    }

    @Override
    public int getCustomModelData() {
        return 1;
    }

    @Override
    public Material getBaseItem() {
        return Material.ENDER_PEARL;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.EPIC;
    }

    @Override
    public boolean shines() {
        return false;
    }
}
