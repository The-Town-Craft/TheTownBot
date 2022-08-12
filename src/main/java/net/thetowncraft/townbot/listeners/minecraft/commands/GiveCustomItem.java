package net.thetowncraft.townbot.listeners.minecraft.commands;

import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.minecraft.MinecraftCommand;
import net.thetowncraft.townbot.custom_items.CustomItem;
import net.thetowncraft.townbot.custom_items.CustomItems;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GiveCustomItem extends MinecraftCommand {

    @Override
    public void execute(CommandEvent.Minecraft event) {
        Player player = event.getPlayer();
        String[] args = event.getArgs();
        if(args.length == 0) {
            player.sendMessage(ChatColor.RED + "Please specify a custom item to give.");
            return;
        }

        String id = args[0];
        int amount = 1;

        if(args.length > 1) {
            try {
                int inputAmount = Integer.parseInt(args[1]);
                if(inputAmount < 1 || inputAmount > 64) {
                    player.sendMessage(ChatColor.RED + "Invalid amount.");
                    return;
                }
                amount = inputAmount;
            }
            catch (NumberFormatException ex) {
                //empty catch statement
            }
        }

        CustomItem item = CustomItems.getItemById(id);
        if(item == null) {
            player.sendMessage(ChatColor.RED + "Item does not exist.");
            return;
        }

        player.getWorld().dropItem(player.getLocation(), item.createItemStack(amount));
        player.sendMessage(item.getRarity().getColor() + "You were given " + amount + " " + item.getName());
    }

    @Override
    public boolean isAdminCommand() {
        return true;
    }

    @Override
    public String getName() {
        return "gci";
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 1) {
            return new ArrayList<>(CustomItems.ITEMS.keySet());
        }
        return null;
    }
}
