package net.thetowncraft.townbot.economy.shop.commands;

import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.minecraft.MinecraftCommand;
import net.thetowncraft.townbot.economy.shop.ShopItem;
import net.thetowncraft.townbot.economy.shop.ShopManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ToggleItemMinecraft extends MinecraftCommand {

    @Override
    public void execute(CommandEvent.Minecraft event) {
        String[] args = event.getArgs();
        Player player = event.getPlayer();
        if(args.length == 0) {
            player.sendMessage(ChatColor.RED + "What item would you like to toggle?");
            return;
        }

        String name = String.join(" ", args);
        ShopItem item = ShopManager.getItemByName(name);
        if(item == null) {
            player.sendMessage(ChatColor.RED + "Could not find an item by the name of \"" + name + "\"! Type /purchases to view your items!");
            return;
        }

        if(!item.possessedBy(player)) {
            player.sendMessage(ChatColor.RED + "You do not own this item!");
            return;
        }

        if(item.isOff(player)) {
            item.turnOn(player);
            player.sendMessage(ChatColor.GREEN + "Success! " + item.getName() + " has been enabled!");
        }
        else {
            item.turnOff(player);
            player.sendMessage(ChatColor.GREEN + "Success! " + item.getName() + " has been disabled!");
        }
    }

    @Override
    public boolean isAdminCommand() {
        return false;
    }

    @Override
    public String getName() {
        return "toggle";
    }
}
