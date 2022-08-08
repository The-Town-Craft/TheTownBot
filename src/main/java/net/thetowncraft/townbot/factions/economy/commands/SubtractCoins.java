package net.thetowncraft.townbot.factions.economy.commands;

import net.dv8tion.jda.api.Permission;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.factions.economy.EconomyManager;
import org.bukkit.OfflinePlayer;

public class SubtractCoins extends UpdateCoinCommand {

    @Override
    public void updateCoins(CommandEvent.Discord event, OfflinePlayer player, int amount) {
        EconomyManager.subtractCoins(player.getUniqueId().toString(), amount);
        event.getChannel().sendMessage(":white_check_mark: **Success**! Subtracted " + amount + " coins from " + player.getName() + "'s account!").queue();
    }

    @Override
    public String getName() {
        return "subcoins";
    }

    @Override
    public String getDescription() {
        return "Subtract coins from a player!";
    }

    @Override
    public Permission getRequiredPermission() {
        return Permission.ADMINISTRATOR;
    }
}
