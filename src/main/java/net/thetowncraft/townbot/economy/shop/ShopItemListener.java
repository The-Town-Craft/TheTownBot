package net.thetowncraft.townbot.economy.shop;

import net.dv8tion.jda.api.entities.Member;
import net.thetowncraft.townbot.economy.shop.items.ParticleShopItem;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class ShopItemListener implements Listener {

    public static void updateItems() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            Member member = AccountManager.getInstance().getDiscordMember(player);
            if(member == null) continue;

            for(ShopItem item : ShopManager.getItems()) {
                if(!item.possessedBy(member)) continue;
                if(item.isOff(player)) continue;

                if(item instanceof ParticleShopItem) {
                    player.getWorld().spawnParticle(((ParticleShopItem) item).getParticle(), player.getLocation(), ((ParticleShopItem) item).getAmount());
                }
            }
        }
    }
}
