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
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class AcidicSlimeball extends CustomItem {

    @Override
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(event.getAction() != Action.LEFT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();
        if(block == null) return;

        if(!CustomItems.ACIDIC_SLIMEBALL.has(player)) {
            event.getPlayer().sendMessage(ChatColor.RED + "You must defeat the Acidic Slime to use this item.");
            return;
        }

        if(player.getCooldown(getBaseItem()) != 0) return;

        int targetX = block.getX();
        int targetZ = block.getZ();

        int amount = CustomItems.getItemAmountOf(player, CustomItems.ACIDIC_SLIMEBALL);

        double speed = 1.2;

        int cooldown = 10;

        player.setCooldown(getBaseItem(), cooldown);

        double angle = Math.atan2(targetX - player.getLocation().getX(), targetZ - player.getLocation().getZ());
        double vx = (speed) * Math.sin(angle);
        double vz = (speed) * Math.cos(angle);

        player.setVelocity(new Vector(vx, speed, vz));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SLIME_SQUISH, 0.5f, 1);
    }

    @Override
    public String getName() {
        return ChatColor.YELLOW + "Acidic Slimeball";
    }

    @Override
    public String getDescription() {
        return ChatColor.YELLOW + "Bouncy ball of acid.";
    }

    @Override
    public int getCustomModelData() {
        return 1;
    }

    @Override
    public Material getBaseItem() {
        return Material.SLIME_BALL;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.EPIC;
    }

    @Override
    public boolean shines() {
        return false;
    }
}
