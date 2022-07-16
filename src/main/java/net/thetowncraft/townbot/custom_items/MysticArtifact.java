package net.thetowncraft.townbot.custom_items;

import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class MysticArtifact extends CustomItem {

    @Override
    public String getName() {
        return "Mystic Artifact";
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
        return Material.ENCHANTED_GOLDEN_APPLE;
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
