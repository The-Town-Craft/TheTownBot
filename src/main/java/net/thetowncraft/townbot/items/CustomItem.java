package net.thetowncraft.townbot.items;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.custom_bosses.BossEventListener;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

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

    public void procOnHit(Player player, int itemAmount, LivingEntity target, World world) {
        
    }

    public void onPlayerJump(Player player, int amount) {}

    public void updateStats(Player player, int itemAmount) {}

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
        if(member == null) return false;

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
