package net.thetowncraft.townbot.custom_items.mystic_realm.glacial_items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

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
