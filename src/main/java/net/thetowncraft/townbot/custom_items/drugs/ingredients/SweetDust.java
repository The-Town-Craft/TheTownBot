package net.thetowncraft.townbot.custom_items.drugs.ingredients;

import net.thetowncraft.townbot.custom_items.CustomItem;
import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.Material;

public class SweetDust extends CustomItem {

    @Override
    public String getName() {
        return "Sweet Dust";
    }

    @Override
    public String getDescription() {
        return "It's not just sugar, trust me";
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
