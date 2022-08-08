package net.thetowncraft.townbot.factions.economy.shop.commands;

import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.minecraft.MinecraftCommand;
import net.thetowncraft.townbot.factions.economy.shop.ShopItem;
import net.thetowncraft.townbot.factions.economy.shop.ShopManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class PurchasesCommandMinecraft extends MinecraftCommand {

    @Override
    public void execute(CommandEvent.Minecraft event) {
        Player player = event.getPlayer();
        List<ShopItem> purchases = ShopManager.getPurchases(player);
        if(purchases.size() == 0) {
            player.sendMessage(ChatColor.RED + "You haven't purchased anything yet! Type ?shop on the Discord server for more information!");
            return;
        }

        StringBuilder message = new StringBuilder(ChatColor.AQUA + "---Your Purchases---");

        for(ShopItem item : purchases) {
            if(!item.possessedBy(player)) continue;
            if(!item.isOff(player)) message.append("\n" + ChatColor.AQUA + item.getName());
            else message.append("\n" + ChatColor.GRAY + item.getName() + " (Disabled)");
        }
        player.sendMessage(message.toString());
    }

    @Override
    public boolean isAdminCommand() {
        return false;
    }

    @Override
    public String getName() {
        return "purchases";
    }
}
