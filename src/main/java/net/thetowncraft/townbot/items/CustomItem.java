package net.thetowncraft.townbot.items;

import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class CustomItem {

    public ItemStack createItemStack(int amount) {
        ItemStack stack = new ItemStack(this.getBaseItem(), amount);
        ItemMeta itemMeta = stack.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(this.getDescription());
        itemMeta.setLore(lore);
        itemMeta.setDisplayName(this.getRarity().getColor() + this.getName());
        stack.setItemMeta(itemMeta);
        return stack;
    }

    public void updateStats(Player player, int itemAmount) {}

    public abstract String getName();

    public abstract String getDescription();

    public abstract Material getBaseItem();

    public abstract Rarity getRarity();

    public abstract boolean shines();
}
