package net.thetowncraft.townbot.economy.commands;

import net.dv8tion.jda.api.Permission;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.economy.EconomyManager;
import org.bukkit.OfflinePlayer;

public class SetCoins extends UpdateCoinCommand {

    @Override
    public void updateCoins(CommandEvent.Discord event, OfflinePlayer player, int amount) {
        EconomyManager.setCoinBalance(player.getUniqueId().toString(), amount);
        event.getChannel().sendMessage("Set " + player.getName() + "'s coin balance to " + amount).queue();
    }

    @Override
    public String getName() {
        return "setcoins";
    }

    @Override
    public String getDescription() {
        return "Set a player's coin balance!";
    }

    @Override
    public Permission getRequiredPermission() {
        return Permission.ADMINISTRATOR;
    }
}
