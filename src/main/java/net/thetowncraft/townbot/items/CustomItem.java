package net.thetowncraft.townbot.items;

import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public abstract class CustomItem {

    public ItemStack createItemStack(int amount) {
        ItemStack stack = new ItemStack(this.getBaseItem(), amount);
        ItemMeta itemMeta = stack.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(this.getDescription());
        itemMeta.setLore(lore);
        if(this.getCustomModelData() != 0) itemMeta.setCustomModelData(this.getCustomModelData());
        itemMeta.setDisplayName(this.getRarity().getColor() + this.getName());
        stack.setItemMeta(itemMeta);
        return stack;
    }

    public void procOnHit(Player player, int itemAmount, LivingEntity target, World world) {
        
    }

    public void onPlayerJump(Player player, int amount) {}

    public void updateStats(Player player, int itemAmount) {}

    public void onInteract(PlayerInteractEvent event, int amount) {}
    public void onPlayerDamage(Player player, EntityDamageEvent event, int amount) {}

    public abstract String getName();

    public abstract String getDescription();

    public abstract int getCustomModelData();

    public abstract Material getBaseItem();

    public abstract Rarity getRarity();

    public abstract boolean shines();
}
