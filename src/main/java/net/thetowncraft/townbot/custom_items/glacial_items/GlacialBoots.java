package net.thetowncraft.townbot.custom_items.glacial_items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class GlacialBoots extends GlacialArmor {

    @Override
    public String getName() {
        return "Glacial Boots";
    }

    @Override
    public ItemStack createItemStack(int amount) {
        ItemStack stack = super.createItemStack(amount);
        stack.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 10);
        stack.addUnsafeEnchantment(Enchantment.DEPTH_STRIDER, 5);
        stack.addUnsafeEnchantment(Enchantment.SOUL_SPEED, 5);
        return stack;
    }

    @Override
    public String getDescription() {
        return ChatColor.AQUA + "Boots of The Iceologer";
    }

    @Override
    public Material getBaseItem() {
        return Material.NETHERITE_BOOTS;
    }
}
