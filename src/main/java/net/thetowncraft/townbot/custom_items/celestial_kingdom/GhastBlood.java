package net.thetowncraft.townbot.custom_items.celestial_kingdom;

import net.thetowncraft.townbot.custom_bosses.BossEventListener;
import net.thetowncraft.townbot.custom_items.CustomItem;
import net.thetowncraft.townbot.custom_items.CustomItems;
import net.thetowncraft.townbot.dimension.CelestialKingdomListener;
import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class GhastBlood extends CustomItem {

    @Override
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(player.getCooldown(getBaseItem()) != 0) return;

        player.setCooldown(getBaseItem(), 20);

        if(!player.getWorld().getName().equals(CelestialKingdomListener.CELESTIAL_KINGDOM)) {
            player.sendMessage("You can only use this item in the Celestial Kingdom!");
            return;
        }

        BossEventListener boss = CustomItems.SATANIC_MAGMABALL.getBoss();
        if(boss == null) {
            player.sendMessage(ChatColor.RED + "Error! Please report this to ModMail: Could not summon boss because boss is null!");
            return;
        }

        ItemStack item = event.getItem();
        if(item == null) {
            player.sendMessage(ChatColor.RED + "Error! Please report this to ModMail: Could not summon boss because item is null!");
            return;
        }

        item.setAmount(item.getAmount() - 1);
        player.getInventory().setItemInMainHand(item);
        boss.initBossFight(player);
    }

    @Override
    public String getName() {
        return ChatColor.RED + "Ghast Blood";
    }

    @Override
    public String getDescription() {
        return ChatColor.DARK_RED + "The Hellfire Ghast awaits...";
    }

    @Override
    public int getCustomModelData() {
        return 1;
    }

    @Override
    public Material getBaseItem() {
        return Material.GHAST_TEAR;
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
