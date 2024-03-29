package net.thetowncraft.townbot.custom_items.drugs.ingredients.methingredients;

import net.thetowncraft.townbot.custom_items.CustomItem;
import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.Material;

public class PowderedMeth extends CustomItem {
    @Override
    public String getName(){
        return "Powdered Meth";
    }

    @Override
    public String getDescription(){
        return "Needs to be crystialier";
    }

    @Override
    public int getCustomModelData(){
        return 0;
    }

    @Override
    public Material getBaseItem(){
        return Material.BONE_MEAL;
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
