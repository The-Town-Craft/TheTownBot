package net.thetowncraft.townbot.custom_items;

import net.thetowncraft.townbot.custom_items.celestial_kingdom.AcidicArtifact;
import net.thetowncraft.townbot.custom_items.celestial_kingdom.AcidicSlimeball;
import net.thetowncraft.townbot.custom_items.celestial_kingdom.GhastBlood;
import net.thetowncraft.townbot.custom_items.celestial_kingdom.SatanicMagmaball;
import net.thetowncraft.townbot.custom_items.mystic_realm.glacial_items.*;
import net.thetowncraft.townbot.custom_items.mystic_realm.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
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

    //Mystic Realm
    public static final CustomItem SHAPED_GLASS = registerItem("shaped_glass", new ShapedGlass());
    public static final CustomItem MYSTIC_PEARL = registerItem("mystic_pearl", new MysticPearl());
    public static final CustomItem BLAZING_THUNDERSTAR = registerItem("blazing_thunderstar", new BlazingThunderstar());
    public static final CustomItem NOXIOUS_FEATHER = registerItem("noxious_feather", new NoxiousFeather());
    public static final CustomItem HUNTER_SKULL = registerItem("hunter_skull", new HunterSkull());
    public static final CustomItem HUNTER_SWORD = registerItem("hunter_sword", new HunterSword());
    public static final CustomItem ILLUSIONER_HEART = registerItem("illusioner_heart", new IllusionerHeart());
    public static final CustomItem MYSTIC_ARTIFACT = registerItem("mystic_artifact", new MysticArtifact());
    public static final CustomItem GLACIAL_AMULET = registerItem("glacial_amulet", new GlacialAmuletItem());
    public static final CustomItem GLACIAL_SHARD = registerItem("glacial_shard", new GlacialShardItem());
    public static final CustomItem GLACIAL_HELMET = registerItem("glacial_helmet", new GlacialHelmet());
    public static final CustomItem GLACIAL_CHESTPLATE = registerItem("glacial_chestplate", new GlacialChestplate());
    public static final CustomItem GLACIAL_LEGGINGS = registerItem("glacial_leggings", new GlacialLeggings());
    public static final CustomItem GLACIAL_BOOTS = registerItem("glacial_boots", new GlacialBoots());

    //Celestial Kingdom
    public static final CustomItem ACIDIC_ARTIFACT = registerItem("acidic_artifact", new AcidicArtifact());
    public static final CustomItem ACIDIC_SLIMEBALL = registerItem("acidic_slimeball", new AcidicSlimeball());
    public static final CustomItem HELLFIRE_GHAST_TEAR = registerItem("ghast_blood", new GhastBlood());
    public static final CustomItem SATANIC_MAGMABALL = registerItem("satanic_magmaball", new SatanicMagmaball());

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

    public static String getLore0(ItemStack stack) {
        if(stack == null) return "";
        ItemMeta meta = stack.getItemMeta();
        if(meta == null) return "";

        List<String> lore = meta.getLore();
        if(lore == null || lore.size() == 0) return "";
        return lore.get(0);
    }

    public static boolean isCustomItemStack(ItemStack stack, CustomItem customItem) {
        return getLore0(stack).equals(customItem.getDescription());
    }

    public static boolean isHolding(CustomItem item, Player player) {
        PlayerInventory inventory = player.getInventory();
        return isCustomItemStack(inventory.getItemInMainHand(), item);
    }

    public static int playerHoldingItemAmount(Player player, CustomItem item) {
        ItemStack stack = player.getInventory().getItemInMainHand();

        ItemMeta meta = stack.getItemMeta();
        if(meta == null) return 0;

        List<String> lore = meta.getLore();
        if(lore == null || lore.size() == 0) return 0;
        if(lore.get(0).equals(item.getDescription())) return stack.getAmount();
        return 0;
    }

    static void onItemClick(PlayerInteractEvent event) {
        for(CustomItem item : CustomItems.getItems()) {
            if(isHolding(item, event.getPlayer())) {
                item.onClick(event);
            }
         }
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
    static void onPlayerDropItem(PlayerDropItemEvent event) {
        Item item = event.getItemDrop();
        for(CustomItem custom : ITEMS.values()) {
            if(isCustomItemStack(item.getItemStack(), custom)) custom.onPlayerDrop(event);
        }
    }
}
