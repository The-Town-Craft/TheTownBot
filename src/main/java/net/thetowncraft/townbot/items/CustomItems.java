package net.thetowncraft.townbot.items;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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
    public static final CustomItem MYSTIC_PEARL = registerItem("mystic_pearl", new MysticPearl());
    public static final CustomItem BLAZING_THUNDERSTAR = registerItem("blazing_thunderstar", new BlazingThunderstar());
    public static final CustomItem NOXIOUS_FEATHER = registerItem("noxious_feather", new NoxiousFeather());
    public static final CustomItem HUNTER_SKULL = registerItem("hunter_skull", new HunterSkull());
    public static final CustomItem HUNTER_SWORD = registerItem("hunter_sword", new HunterSword());
    public static final CustomItem ILLUSIONER_HEART = registerItem("illusioner_heart", new IllusionerHeart());
    public static final CustomItem MYSTIC_ARTIFACT = registerItem("mystic_artifact", new MysticArtifact());

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
        ItemStack stack = getItemStackOf(player, item);
        if(stack == null) return 0;
        return stack.getAmount();
    }

    public static ItemStack getItemStackOf(Player player, CustomItem item) {
        PlayerInventory inventory = player.getInventory();
        for(ItemStack stack : inventory) {
            if(stack == null) continue;
            ItemMeta meta = stack.getItemMeta();
            if(meta == null) continue;

            List<String> lore = meta.getLore();
            if(lore == null || lore.size() == 0) continue;
            if(lore.get(0).equals(item.getDescription())) return stack;
        }
        return null;
    }

    public static int playerHoldingItemAmount(Player player, CustomItem item) {
        ItemStack stack = player.getInventory().getItemInMainHand();

        ItemMeta meta = stack.getItemMeta();
        if(meta == null) return 0;

        List<String> lore = meta.getLore();
        if(lore == null || lore.size() == 0) return 0;
        if(lore.get(0).equals(item.getDescription())) return stack.getAmount();;
        return 0;
    }

    static void onItemInteract(PlayerInteractEvent event) {
        for(CustomItem item : ITEMS.values()) {
            int amount = playerHoldingItemAmount(event.getPlayer(), item);
            if(amount == 0) return;

            item.onInteract(event, amount);
        }
    }
    static void onPlayerDamage(Player player, EntityDamageEvent event) {
        for(CustomItem item : ITEMS.values()) {
            int amount = playerHoldingItemAmount(player, item);
            if(amount == 0) return;

            item.onPlayerDamage(player, event, amount);
        }
    }
}
