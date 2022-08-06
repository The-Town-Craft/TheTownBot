package net.thetowncraft.townbot.custom_items;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.custom_bosses.BossEventListener;
import net.thetowncraft.townbot.dimension.CelestialKingdomListener;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class CustomItem {

    private BossEventListener boss;

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

    public boolean has(Player player) {
        ItemStack stack = CustomItems.getItemStackOf(player, this);
        return stack != null;
    }

    public void tryInitBossFight(PlayerInteractEvent event, CustomItem bossItem, String requiredWorld, String requiredWorldName) {

        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        if(player.getCooldown(getBaseItem()) != 0) return;

        player.setCooldown(getBaseItem(), 20);

        if(!player.getWorld().getName().equals(requiredWorld)) {
            player.sendMessage(ChatColor.RED + "You can only use this item in the " + requiredWorldName + "!");
            return;
        }

        BossEventListener boss = bossItem.getBoss();
        if(boss == null) {
            player.sendMessage(ChatColor.RED + "Error! Please report this to ModMail: Could not summon boss because boss is null!");
            return;
        }

        ItemStack item = event.getItem();
        if(item == null) {
            player.sendMessage(ChatColor.RED + "Error! Please report this to ModMail: Could not summon boss because item is null!");
            return;
        }

        if(boss.initBossFight(player)) {
            item.setAmount(item.getAmount() - 1);
            player.getInventory().setItemInMainHand(item);
        }
    }

    public void procOnHit(Player player, int itemAmount, LivingEntity target, World world) {
        
    }

    public void onPlayerJump(Player player, int amount) {}

    public void updateStats(Player player, int itemAmount) {}

    public void onClick(PlayerInteractEvent event) {}
    public void onInteract(PlayerInteractEvent event, int amount) {}
    public void onPlayerDamage(Player player, EntityDamageEvent event, int amount) {}
    public void onPlayerDrop(PlayerDropItemEvent event) {}

    public void setBoss(BossEventListener boss) {
        this.boss = boss;
    }

    public BossEventListener getBoss() {
        return boss;
    }

    public boolean canUse(Player player) {
        Member member = AccountManager.getInstance().getDiscordMember(player);
        if(member == null) return true;

        BossEventListener boss = this.getBoss();
        if(boss == null) return true;

        Role role = Bot.jda.getRoleById(boss.getBossRoleId());
        if(role == null) return true;

        return member.getRoles().contains(role);
    }

    public abstract String getName();

    public abstract String getDescription();

    public abstract int getCustomModelData();

    public abstract Material getBaseItem();

    public abstract Rarity getRarity();

    public abstract boolean shines();
}
