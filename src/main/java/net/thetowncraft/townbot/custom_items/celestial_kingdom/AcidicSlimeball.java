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
        Vector vector = getVectorFromClick(event, this, 1.2, 10, "Acidic Creeper");
        if(vector == null) return;

        Player player = event.getPlayer();
        player.setVelocity(new Vector(-vector.getX(), vector.getY(), -vector.getZ()));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SLIME_SQUISH, 1f, 1);
    }

    public static Vector getVectorFromClick(PlayerInteractEvent event, CustomItem item, double speed, int cooldown, String bossName) {
        Player player = event.getPlayer();
        if(event.getAction() != Action.LEFT_CLICK_BLOCK) return null;

        Block block = event.getClickedBlock();
        if(block == null) return null;

        if(!item.canUse(player)) {
            event.getPlayer().sendMessage(ChatColor.RED + "You must defeat the " + bossName + " to use this item.");
            return null;
        }

        if(player.getCooldown(item.getBaseItem()) != 0) return null;

        int targetX = block.getX();
        int targetZ = block.getZ();

        player.setCooldown(item.getBaseItem(), cooldown);

        double angle = Math.atan2(targetX - player.getLocation().getX(), targetZ - player.getLocation().getZ());
        double vx = (speed) * Math.sin(angle);
        double vz = (speed) * Math.cos(angle);

        return new Vector(vx, speed * 0.7, vz);
    }

    @Override
    public String getName() {
        return ChatColor.YELLOW + "Acidic Slimeball";
    }

    @Override
    public String getDescription() {
        return ChatColor.YELLOW + "Bouncy ball of acid. [LEFT CLICK BLOCK]";
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
