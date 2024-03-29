package net.thetowncraft.townbot.custom_items.drugs.ingredients;

import net.thetowncraft.townbot.custom_items.CustomItem;
import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class NetherCandy extends CustomItem {
    @Override
    public String getName(){
        return ChatColor.DARK_RED + "Nether Candy";
    }

    @Override
    public String getDescription(){
        return ChatColor.DARK_RED + "Not actually candy";
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
        return true;
    }
}
