package net.thetowncraft.townbot.items;

import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class BlazingThunderstar extends CustomItem {

    @Override
    public String getName() {
        return ChatColor.RED + "The Blazing Thunderstar";
    }

    @Override
    public String getDescription() {
        return ChatColor.RED + "Arrows now unleash the anger of the Blazing Wither";
    }

    @Override
    public Material getBaseItem() {
        return Material.NETHER_STAR;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public boolean shines() {
        return false;
    }
}
