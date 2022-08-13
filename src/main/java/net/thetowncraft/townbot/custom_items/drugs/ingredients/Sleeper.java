package net.thetowncraft.townbot.custom_items.drugs.ingredients;

import net.thetowncraft.townbot.custom_items.CustomItem;
import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class Sleeper extends CustomItem {
    @Override
    public String getName() {
        return ChatColor.LIGHT_PURPLE + "Sleeper";
    }

    @Override
    public String getDescription() {
        return ChatColor.LIGHT_PURPLE + "Slowwwws your thoughts down";
    }

    @Override
    public int getCustomModelData() {
        return 0;
    }

    @Override
    public Material getBaseItem() {
        return Material.RED_DYE;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.RARE;
    }

    @Override
    public boolean shines() {
        return true;
    }
}
