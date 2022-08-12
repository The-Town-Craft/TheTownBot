package net.thetowncraft.townbot.custom_items.drugs;

import net.thetowncraft.townbot.custom_items.CustomItem;
import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class CrystalMeth extends CustomItem {

    @Override
    public String getName(){
        return ChatColor.GRAY + "Crystal Meth";
    }

    @Override
    public String getDescription(){
        return ChatColor.WHITE + "\"Jesse! We're in Minecraft now! JESSE!\"";
    }

    @Override
    public int getCustomModelData(){
        return 1;
    }

    @Override
    public Material getBaseItem(){
        return Material.AMETHYST_SHARD;
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
