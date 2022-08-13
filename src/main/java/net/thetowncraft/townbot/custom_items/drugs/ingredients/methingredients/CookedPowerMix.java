package net.thetowncraft.townbot.custom_items.drugs.ingredients.methingredients;

import net.thetowncraft.townbot.custom_items.CustomItem;
import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.Material;

public class CookedPowerMix extends CustomItem {
    @Override
    public String getName(){
        return "Cooked Power Mix";
    }

    @Override
    public String getDescription(){
        return "Ok, don't put this in a protein shake";
    }

    @Override
    public int getCustomModelData(){
        return 0;
    }

    @Override
    public Material getBaseItem(){
        return Material.BLACK_DYE;
    }

    @Override
    public Rarity getRarity(){
        return Rarity.RARE;
    }

    @Override
    public boolean shines(){
        return true;
    }
}
