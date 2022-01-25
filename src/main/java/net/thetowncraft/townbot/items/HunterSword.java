package net.thetowncraft.townbot.items;

import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HunterSword extends CustomItem {

    @Override
    public String getName() {
        return ChatColor.RED + "Old Hunter's Sword";
    }

    @Override
    public String getDescription() {
        return ChatColor.DARK_RED + "Sword of " + ChatColor.BOLD + "The Wicked Hunter";
    }

    @Override
    public int getCustomModelData() {
        return 1;
    }

    @Override
    public Material getBaseItem() {
        return Material.NETHERITE_SWORD;
    }

    @Override
    public ItemStack createItemStack(int amount) {
        ItemStack stack = super.createItemStack(amount);
        stack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 10);
        stack.addUnsafeEnchantment(Enchantment.SWEEPING_EDGE, 5);
        stack.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 5);
        ItemMeta meta = stack.getItemMeta();
        meta.setUnbreakable(true);
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public boolean shines() {
        return false;
    }
}
