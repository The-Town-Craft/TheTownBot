package net.thetowncraft.townbot.custom_items.mystic_realm.glacial_items;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class GlacialChestplate extends GlacialArmor {

    @Override
    public String getName() {
        return "Glacial Chestplate";
    }

    @Override
    public String getDescription() {
        return ChatColor.AQUA + "Chestplate of The Iceologer";
    }

    @Override
    public Material getBaseItem() {
        return Material.NETHERITE_CHESTPLATE;
    }
}
