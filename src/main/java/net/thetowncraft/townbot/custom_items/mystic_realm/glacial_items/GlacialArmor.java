package net.thetowncraft.townbot.custom_items.mystic_realm.glacial_items;

import net.thetowncraft.townbot.custom_items.CustomItem;
import net.thetowncraft.townbot.custom_items.CustomItems;
import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class GlacialArmor extends CustomItem {

    public static void bossDefeatCheck() {
        for(Player player : Bukkit.getOnlinePlayers())  {

            if(CustomItems.GLACIAL_AMULET.canUse(player)) continue;

            PlayerInventory inventory = player.getInventory();
            boolean removed = false;

            if(CustomItems.isCustomItemStack(inventory.getHelmet(), CustomItems.GLACIAL_HELMET)) {
                inventory.setHelmet(null);
                removed = true;
            }
            if(CustomItems.isCustomItemStack(inventory.getChestplate(), CustomItems.GLACIAL_CHESTPLATE)) {
                inventory.setChestplate(null);
                removed = true;
            }
            if(CustomItems.isCustomItemStack(inventory.getLeggings(), CustomItems.GLACIAL_LEGGINGS)) {
                inventory.setLeggings(null);
                removed = true;
            }
            if(CustomItems.isCustomItemStack(inventory.getBoots(), CustomItems.GLACIAL_BOOTS)) {
                inventory.setBoots(null);
                removed = true;
            }

            if(removed) player.sendMessage(ChatColor.RED + "You must defeat The Iceologer to use Glacial Armor!");
        }
    }

    @Override
    public ItemStack createItemStack(int amount) {
        ItemStack stack = super.createItemStack(amount);

        stack.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 10);

        ItemMeta meta = stack.getItemMeta();
        if(meta != null) {
            meta.setUnbreakable(true);
            stack.setItemMeta(meta);
        }
        return stack;
    }

    @Override
    public int getCustomModelData() {
        return 1;
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
