package net.thetowncraft.townbot.custom_items.glacial_items;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class GlacialLeggings extends GlacialArmor {

    @Override
    public String getName() {
        return "Glacial Leggings";
    }

    @Override
    public String getDescription() {
        return ChatColor.AQUA + "Leggings of The Iceologer";
    }

    @Override
    public Material getBaseItem() {
        return Material.NETHERITE_LEGGINGS;
    }
}
