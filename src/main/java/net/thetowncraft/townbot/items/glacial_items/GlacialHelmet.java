package net.thetowncraft.townbot.items.glacial_items;

import net.thetowncraft.townbot.items.CustomItem;
import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GlacialHelmet extends GlacialArmor {

    @Override
    public String getName() {
        return "Glacial Helmet";
    }

    @Override
    public ItemStack createItemStack(int amount) {
        ItemStack stack = super.createItemStack(amount);
        stack.addUnsafeEnchantment(Enchantment.OXYGEN, 5);
        stack.addUnsafeEnchantment(Enchantment.WATER_WORKER, 1);
        return stack;
    }

    @Override
    public String getDescription() {
        return ChatColor.AQUA + "Helmet of The Iceologer";
    }

    @Override
    public Material getBaseItem() {
        return Material.NETHERITE_HELMET;
    }
}
