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
        Player player = event.getPlayer();
        if(event.getAction() != Action.LEFT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();
        if(block == null) return;

        if(!canUse(player)) {
            event.getPlayer().sendMessage(ChatColor.RED + "You must defeat the Hellfire Ghast to use this item.");
            return;
        }

        if(player.getCooldown(getBaseItem()) != 0) return;

        int targetX = block.getX();
        int targetZ = block.getZ();

        int amount = CustomItems.getItemAmountOf(player, this);

        double speed = 1.2;

        int cooldown = 10;

        player.setCooldown(getBaseItem(), cooldown);

        double angle = Math.atan2(targetX - player.getLocation().getX(), targetZ - player.getLocation().getZ());
        double vx = (speed) * Math.sin(angle);
        double vz = (speed) * Math.cos(angle);

        player.setVelocity(new Vector(-vx, speed, -vz));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_MAGMA_CUBE_SQUISH, 0.5f, 0.3f);
    }

    @Override
    public String getName() {
        return ChatColor.GOLD + "Satanic Magmaball";
    }

    @Override
    public String getDescription() {
        return ChatColor.DARK_RED + "Bouncy Satanic Goo";
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
