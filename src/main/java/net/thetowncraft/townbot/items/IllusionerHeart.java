package net.thetowncraft.townbot.items;

import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.Material;

public class IllusionerHeart extends CustomItem {

    @Override
    public String getName() {
        return "Illusioner's Heart";
    }

    @Override
    public String getDescription() {
        return "Removes all negative status effects";
    }

    @Override
    public int getCustomModelData() {
        return 1;
    }

    @Override
    public Material getBaseItem() {
        return Material.DEBUG_STICK;
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
