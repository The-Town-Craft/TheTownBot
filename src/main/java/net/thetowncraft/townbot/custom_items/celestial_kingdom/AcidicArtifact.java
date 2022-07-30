package net.thetowncraft.townbot.custom_items.celestial_kingdom;

import net.thetowncraft.townbot.custom_items.CustomItem;
import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class AcidicArtifact extends CustomItem {

    @Override
    public String getName() {
        return "Acidic Artifact";
    }

    @Override
    public String getDescription() {
        return ChatColor.YELLOW + "The Acidic Slime awaits...";
    }

    @Override
    public int getCustomModelData() {
        return 2;
    }

    @Override
    public Material getBaseItem() {
        return Material.ENCHANTED_GOLDEN_APPLE;
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
