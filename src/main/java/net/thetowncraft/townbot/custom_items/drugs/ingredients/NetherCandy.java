package net.thetowncraft.townbot.custom_items.drugs.ingredients;

import net.thetowncraft.townbot.custom_items.CustomItem;
import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.Material;

public class NetherCandy extends CustomItem {
    @Override
    public String getName(){
        return "Nether Candy";
    }

    @Override
    public String getDescription(){
        return "Not actually candy";
    }

    @Override
    public int getCustomModelData(){
        return 0;
    }

    @Override
    public Material getBaseItem(){
        return Material.NETHER_WART;
    }

    @Override
    public Rarity getRarity(){
        return Rarity.RARE;
    }

    @Override
    public boolean shines(){
        return false;
    }
}
