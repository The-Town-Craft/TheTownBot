package net.thetowncraft.townbot.economy.player_shops.commands;

import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.minecraft.MinecraftCommand;
import net.thetowncraft.townbot.economy.player_shops.PlayerShopManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class WithdrawDiamonds extends MinecraftCommand {

    @Override
    public void execute(CommandEvent.Minecraft event) {
        String[] args = event.getArgs();
        Player player = event.getPlayer();
        if(args.length == 0) {
            player.sendMessage(ChatColor.RED + "/with <amount>");
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Invalid number! /with <amount>");
            return;
        }

        if(amount < 1) {
            player.sendMessage(ChatColor.RED + "Amount must be greater than 0");
            return;
        }

        PlayerShopManager.withdrawDiamonds(player, amount);
    }

    @Override
    public boolean isAdminCommand() {
        return true;
    }

    @Override
    public String getName() {
        return "with";
    }
}
