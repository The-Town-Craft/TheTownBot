package net.thetowncraft.townbot.custom_items.drugs.ingredients;

import net.thetowncraft.townbot.custom_items.CustomItem;
import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class SweetDust extends CustomItem {

    @Override
    public String getName() {
        return ChatColor.valueOf("FFC0CB") + "Sweet Dust";
    }

    @Override
    public String getDescription() {
        return ChatColor.WHITE + "It's not just sugar, trust me";
    }

    @Override
    public int getCustomModelData() {
        return 0;
    }

    @Override
    public Material getBaseItem() {
        return Material.SUGAR;
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
