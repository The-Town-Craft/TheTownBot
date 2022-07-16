package net.thetowncraft.townbot.custom_items.glacial_items;

import net.thetowncraft.townbot.custom_bosses.BossEventListener;
import net.thetowncraft.townbot.custom_items.CustomItem;
import net.thetowncraft.townbot.custom_items.CustomItems;
import net.thetowncraft.townbot.dimension.DimensionEventListener;
import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class GlacialShardItem extends CustomItem {

    @Override
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            BossEventListener boss = CustomItems.GLACIAL_AMULET.getBoss();
            if(boss == null) {
                player.sendMessage(ChatColor.RED + "Error! Please type \"-apply bug-report\" on the Discord Server!");
                return;
            }
            if(!player.getWorld().getName().equals(DimensionEventListener.MYSTIC_REALM)) {
                player.sendMessage(ChatColor.RED + "You can only use this item in the Mystic Realm!");
                return;
            }
            player.getInventory().remove(player.getInventory().getItemInMainHand());
            boss.initBossFight(event.getPlayer());
        }
    }

    @Override
    public String getName() {
        return "Glacial Shard";
    }

    @Override
    public String getDescription() {
        return ChatColor.DARK_RED + "Right click to stab yourself.";
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
