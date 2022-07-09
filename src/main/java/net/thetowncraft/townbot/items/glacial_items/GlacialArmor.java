package net.thetowncraft.townbot.items.glacial_items;

import net.thetowncraft.townbot.items.CustomItem;
import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class GlacialArmor extends CustomItem {
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
