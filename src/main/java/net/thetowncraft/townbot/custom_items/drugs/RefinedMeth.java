package net.thetowncraft.townbot.custom_items.drugs;

import net.thetowncraft.townbot.custom_items.CustomItem;
import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class RefinedMeth extends CustomItem {

    @Override
    public String getName(){
        return ChatColor.BLUE + "Refined Meth";
    }

    @Override
    public String getDescription(){
        return ChatColor.ITALIC + "" + ChatColor.BLUE + "\"The special love I have for you, My Baby Blue.\"";
    }

    @Override
    public int getCustomModelData(){
        return 1;
    }

    @Override
    public Material getBaseItem(){
        return Material.SUGAR;
    }

    @Override
    public Rarity getRarity(){
        return Rarity.EPIC;
    }

    @Override
    public boolean shines(){
        return true;
    }
}
