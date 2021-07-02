package net.thetowncraft.townbot.economy.player_shops.commands;

import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.minecraft.MinecraftCommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ShopChestCommand extends MinecraftCommand {

    @Override
    public void execute(CommandEvent.Minecraft event) {
        String[] args = event.getArgs();
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if(args.length < 1) {
            player.sendMessage(ChatColor.RED + "/shopchest <price per item> <per stack?(true or false)>");
            return;
        }

        int price;

        try {
            price = Integer.parseInt(args[0]);
        }
        catch (NumberFormatException ex) {
            player.sendMessage(ChatColor.RED + "Invalid number!\n/shopchest <price per item>");
            return;
        }

        boolean perStack;

        if(args.length < 2) {
            perStack = false;
        }
        else {
            perStack = Boolean.parseBoolean(args[1]);
        }

        if(item.getType() != Material.CHEST) {
            event.getPlayer().sendMessage(ChatColor.RED + "You must be holding a chest in your main hand for this command to work!");
            return;
        }

        ItemMeta meta = item.getItemMeta();

        List<String> lore = new ArrayList<>();
        lore.add(String.valueOf(price));
        lore.add(String.valueOf(perStack));

        meta.setLore(lore);
        meta.setDisplayName(player.getDisplayName() + "'s Shop, " + price + " diamonds per item");
        item.setItemMeta(meta);
        player.sendMessage(ChatColor.GREEN + "Turned your " + item.getAmount() + " chest(s) into shop chests!");
    }

    @Override
    public boolean isAdminCommand() {
        return true;
    }

    @Override
    public String getName() {
        return "shopchest";
    }
}
