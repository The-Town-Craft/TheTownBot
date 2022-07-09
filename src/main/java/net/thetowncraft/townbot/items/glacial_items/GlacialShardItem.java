package net.thetowncraft.townbot.items.glacial_items;

import net.thetowncraft.townbot.items.CustomItem;
import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class GlacialShardItem extends CustomItem {

    @Override
    public String getName() {
        return "Glacial Shard";
    }

    @Override
    public String getDescription() {
        return ChatColor.BLUE + "Used to craft Glacial Armor.";
    }

    @Override
    public int getCustomModelData() {
        return 1;
    }

    @Override
    public Material getBaseItem() {
        return Material.GUNPOWDER;
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
