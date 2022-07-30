package net.thetowncraft.townbot.custom_items.celestial_kingdom;

import net.thetowncraft.townbot.custom_items.CustomItem;
import net.thetowncraft.townbot.custom_items.CustomItems;
import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class AcidicSlimeball extends CustomItem {

    @Override
    public String getName() {
        return ChatColor.YELLOW + "Acidic Slimeball";
    }

    @Override
    public String getDescription() {
        return ChatColor.YELLOW + "Bouncy ball of acid.";
    }

    @Override
    public int getCustomModelData() {
        return 1;
    }

    @Override
    public Material getBaseItem() {
        return Material.SLIME_BALL;
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
