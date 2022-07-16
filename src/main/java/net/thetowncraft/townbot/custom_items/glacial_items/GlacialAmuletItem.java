package net.thetowncraft.townbot.custom_items.glacial_items;

import net.thetowncraft.townbot.custom_items.CustomItem;
import net.thetowncraft.townbot.custom_items.CustomItems;
import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class GlacialAmuletItem extends CustomItem {

    @Override
    public void onClick(PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            ItemStack hand = player.getInventory().getItemInMainHand();
            if(CustomItems.isCustomItemStack(hand, CustomItems.GLACIAL_AMULET)) {
                player.getInventory().remove(hand);

                World world = player.getWorld();
                world.dropItem(player.getLocation(), CustomItems.GLACIAL_HELMET.createItemStack(1));
                world.dropItem(player.getLocation(), CustomItems.GLACIAL_CHESTPLATE.createItemStack(1));
                world.dropItem(player.getLocation(), CustomItems.GLACIAL_LEGGINGS.createItemStack(1));
                world.dropItem(player.getLocation(), CustomItems.GLACIAL_BOOTS.createItemStack(1));

                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5f, 1);
                player.setFreezeTicks(100);
            }
        }
    }

    @Override
    public String getName() {
        return "Glacial Amulet";
    }

    @Override
    public String getDescription() {
        return "Craft Glacial Armor [Right Click]";
    }

    @Override
    public int getCustomModelData() {
        return 1;
    }

    @Override
    public Material getBaseItem() {
        return Material.CARROT_ON_A_STICK;
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
