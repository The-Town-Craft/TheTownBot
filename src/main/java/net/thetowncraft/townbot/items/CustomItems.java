package net.thetowncraft.townbot.items;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomItems {

    private static final Map<String, CustomItem> ITEMS = new HashMap<>();

    public static final CustomItem SHAPED_GLASS = registerItem("shaped_glass", new ShapedGlass());
    public static final CustomItem BLAZING_THUNDERSTAR = registerItem("thunderstar", new BlazingThunderstar());

    static CustomItem registerItem(String id, CustomItem item) {
        ITEMS.put(id, item);
        return item;
    }

    public static Collection<CustomItem> getItems() {
        return ITEMS.values();
    }

    public static CustomItem getItemById(String id) {
        return ITEMS.get(id);
    }

    public static CustomItem getItemByDescription(String desc) {
        for(CustomItem item : CustomItems.getItems()) {
            if(item.getDescription().equals(desc)) {
                return item;
            }
        }
        return null;
    }

    public static void updateItemStats(Player player) {
        for(CustomItem item : ITEMS.values()) {
            int amount = getItemAmountOf(player, item);
            item.updateStats(player, amount);
        }
    }

    public static int getItemAmountOf(Player player, CustomItem item) {
        PlayerInventory inventory = player.getInventory();
        for(ItemStack stack : inventory) {
            if(stack == null) continue;
            ItemMeta meta = stack.getItemMeta();
            if(meta == null) continue;

            List<String> lore = meta.getLore();
            if(lore == null || lore.size() == 0) continue;

            if(lore.get(0).equals(item.getDescription())) return stack.getAmount();
        }
        return 0;
    }
}