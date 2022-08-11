package net.thetowncraft.townbot.custom_items.drugs.ingredients;

import net.thetowncraft.townbot.custom_items.CustomItem;
import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.Material;

public class Spice extends CustomItem {
    @Override
    public String getName() {
        return "Spice";
    }

    @Override
    public String getDescription() {
        return "Like adrenaline, but more!";
    }

    @Override
    public int getCustomModelData() {
        return 0;
    }

    @Override
    public Material getBaseItem() {
        return Material.GLOWSTONE;
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
