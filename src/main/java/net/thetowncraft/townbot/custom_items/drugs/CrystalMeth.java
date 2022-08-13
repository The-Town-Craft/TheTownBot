package net.thetowncraft.townbot.custom_items.drugs;

import net.thetowncraft.townbot.custom_items.CustomItem;
import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;

public class CrystalMeth extends CustomItem {

    public Player[] stageOne = {};
    public Player[] stageTwo = {};
    public Player[] stageThree = {};
    public Player[] stageFour = {};
    public Player[] stageFive = {};

    @Override
    public String getName(){
        return ChatColor.AQUA + "Crystal Meth";
    }

    @Override
    public String getDescription(){
        return ChatColor.AQUA + "\"Yeah! Science!\"";
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
