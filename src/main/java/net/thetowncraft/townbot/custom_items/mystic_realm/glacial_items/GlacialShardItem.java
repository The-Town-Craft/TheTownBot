package net.thetowncraft.townbot.custom_items.mystic_realm.glacial_items;

import net.thetowncraft.townbot.custom_bosses.BossEventListener;
import net.thetowncraft.townbot.custom_items.CustomItem;
import net.thetowncraft.townbot.custom_items.CustomItems;
import net.thetowncraft.townbot.dimension.MysticRealmListener;
import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class GlacialShardItem extends CustomItem {

    @Override
    public void onClick(PlayerInteractEvent event) {
        tryInitBossFight(event, CustomItems.GLACIAL_AMULET, MysticRealmListener.MYSTIC_REALM, "Mystic Realm");
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
