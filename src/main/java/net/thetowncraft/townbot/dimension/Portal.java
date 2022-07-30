package net.thetowncraft.townbot.dimension;

import net.thetowncraft.townbot.Plugin;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Portal {

    public static boolean isPortal(PlayerInteractEvent event, Material surroundingBlock, Material centerBlock) {
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();

        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return false;
        if(block == null || block.getType() != centerBlock) return false;
        if(player.getInventory().getItemInMainHand().getType() != Material.FLINT_AND_STEEL) return false;

        World world = block.getWorld();
        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();

        if(world.getBlockAt(x + 1, y, z).getType() != surroundingBlock) return false;
        if(world.getBlockAt(x - 1, y, z).getType() != surroundingBlock) return false;


        if(world.getBlockAt(x, y, z + 1).getType() != surroundingBlock) return false;
        if(world.getBlockAt(x, y, z - 1).getType() != surroundingBlock) return false;

        return player.getGameMode() != GameMode.ADVENTURE;
    }
}
