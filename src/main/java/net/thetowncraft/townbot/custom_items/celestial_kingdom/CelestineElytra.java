package net.thetowncraft.townbot.custom_items.celestial_kingdom;

import net.thetowncraft.townbot.custom_items.CustomItem;
import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CelestineElytra extends CustomItem {

    @Override
    public ItemStack createItemStack(int amount) {
        ItemStack stack = super.createItemStack(amount);
        ItemMeta meta = stack.getItemMeta();
        meta.setUnbreakable(true);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier("generic.armor", 6, AttributeModifier.Operation.ADD_NUMBER));
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier("generic.armor_toughness", 8, AttributeModifier.Operation.ADD_NUMBER));
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public String getName() {
        return ChatColor.AQUA + "Celestine Elytra";
    }

    @Override
    public String getDescription() {
        return ChatColor.DARK_AQUA + "Wings of the Fearsome Phantom";
    }

    @Override
    public int getCustomModelData() {
        return 1;
    }

    @Override
    public Material getBaseItem() {
        return Material.ELYTRA;
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
