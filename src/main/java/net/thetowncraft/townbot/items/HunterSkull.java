package net.thetowncraft.townbot.items;

import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class HunterSkull extends CustomItem {

    @Override
    public String getName() {
        return ChatColor.DARK_RED + "Old Hunter's Skull";
    }

    @Override
    public String getDescription() {
        return ChatColor.DARK_RED + "Skull of " + ChatColor.MAGIC + "The Wicked Hunter";
    }

    @Override
    public int getCustomModelData() {
        return 0;
    }

    @Override
    public Material getBaseItem() {
        return Material.SKELETON_SKULL;
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
