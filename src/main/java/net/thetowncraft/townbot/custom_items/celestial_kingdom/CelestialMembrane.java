package net.thetowncraft.townbot.custom_items.celestial_kingdom;

import net.thetowncraft.townbot.custom_items.CustomItem;
import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class CelestialMembrane extends CustomItem {

    @Override
    public String getName() {
        return ChatColor.BLUE + "Celestial Membrane";
    }

    @Override
    public String getDescription() {
        return ChatColor.DARK_BLUE + "A formidable foe awaits...";
    }

    @Override
    public int getCustomModelData() {
        return 1;
    }

    @Override
    public Material getBaseItem() {
        return Material.PHANTOM_MEMBRANE;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.RARE;
    }

    @Override
    public boolean shines() {
        return false;
    }
}
