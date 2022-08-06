package net.thetowncraft.townbot.custom_items.mystic_realm.glacial_items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class GlacialLeggings extends GlacialArmor {

    @Override
    public String getName() {
        return "Glacial Leggings";
    }

    @Override
    public ItemStack createItemStack(int amount) {
        ItemStack stack = super.createItemStack(amount);
        stack.addUnsafeEnchantment(Enchantment.SWIFT_SNEAK, 4);
        return stack;
    }

    @Override
    public String getDescription() {
        return ChatColor.AQUA + "Leggings of The Iceologer";
    }

    @Override
    public Material getBaseItem() {
        return Material.NETHERITE_LEGGINGS;
    }
}
