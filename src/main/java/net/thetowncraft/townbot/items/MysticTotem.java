package net.thetowncraft.townbot.items;

import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class MysticTotem extends CustomItem {

    @Override
    public String getName() {
        return "Mystic Totem";
    }

    @Override
    public String getDescription() {
        return ChatColor.DARK_PURPLE + "" + ChatColor.MAGIC + "Warps you to the....";
    }

    @Override
    public int getCustomModelData() {
        return 1;
    }

    @Override
    public Material getBaseItem() {
        return Material.TOTEM_OF_UNDYING;
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
