package net.thetowncraft.townbot.custom_items.drugs.ingredients.methingredients;

import net.thetowncraft.townbot.custom_items.CustomItem;
import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.Material;

public class Hypersugar extends CustomItem {
    @Override
    public String getName(){
        return "Hypersugar";
    }

    @Override
    public String getDescription(){
        return "Goes great on donuts!";
    }

    @Override
    public int getCustomModelData(){
        return 0;
    }

    @Override
    public Material getBaseItem(){
        return Material.YELLOW_DYE;
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
