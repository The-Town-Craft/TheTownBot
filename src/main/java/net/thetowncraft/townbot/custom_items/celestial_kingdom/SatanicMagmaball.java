package net.thetowncraft.townbot.custom_items.celestial_kingdom;

import net.thetowncraft.townbot.custom_items.CustomItem;
import net.thetowncraft.townbot.custom_items.CustomItems;
import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class SatanicMagmaball extends CustomItem {

    @Override
    public void onClick(PlayerInteractEvent event) {
        Vector vector = AcidicSlimeball.getVectorFromClick(event, this, 1.8, 10, "Hellfire Ghast");
        if(vector == null) return;

        Player player = event.getPlayer();
        player.setVelocity(vector);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_MAGMA_CUBE_SQUISH, 1f, 0.5f);
    }

    @Override
    public String getName() {
        return ChatColor.GOLD + "Satanic Magmaball";
    }

    @Override
    public String getDescription() {
        return ChatColor.DARK_RED + "Bouncy satanic goo. [LEFT CLICK BLOCK]";
    }

    @Override
    public int getCustomModelData() {
        return 1;
    }

    @Override
    public Material getBaseItem() {
        return Material.MAGMA_CREAM;
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
