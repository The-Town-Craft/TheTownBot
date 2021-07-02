package net.thetowncraft.townbot.economy.player_shops.commands;

import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.minecraft.MinecraftCommand;
import net.thetowncraft.townbot.economy.player_shops.PlayerShopManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class DepositDiamonds extends MinecraftCommand {

    @Override
    public void execute(CommandEvent.Minecraft event) {
        String[] args = event.getArgs();
        Player player = event.getPlayer();
        if(args.length == 0) {
            player.sendMessage(ChatColor.RED + "/dep <amount>");
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Invalid number! /dep <amount>");
            return;
        }

        if(amount < 1) {
            player.sendMessage(ChatColor.RED + "Amount must be greater than 0");
            return;
        }

        if(amount > 64) {
            player.sendMessage(ChatColor.RED + "You may only deposit 64 diamonds at a time!");
            return;
        }

        if(player.getVelocity().getX() != 0 || player.getVelocity().getY() != 0 || player.getVelocity().getZ() != 0) {
            player.sendMessage(ChatColor.RED + "You must be standing still to use this command!");
            return;
        }

        PlayerShopManager.depositDiamonds(player, amount);
    }

    @Override
    public boolean isAdminCommand() {
        return true;
    }

    @Override
    public String getName() {
        return "dep";
    }
}
