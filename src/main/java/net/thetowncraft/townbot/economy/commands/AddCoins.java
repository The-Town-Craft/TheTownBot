package net.thetowncraft.townbot.economy.commands;

import net.dv8tion.jda.api.Permission;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.economy.EconomyManager;
import org.bukkit.OfflinePlayer;

public class AddCoins extends UpdateCoinCommand {

    @Override
    public void updateCoins(CommandEvent.Discord event, OfflinePlayer player, int amount) {
        EconomyManager.addCoins(player.getUniqueId().toString(), amount);
        event.getChannel().sendMessage(":white_check_mark: **Success**! Added " + amount + " coins to " + player.getName() + "'s account!").queue();
    }

    @Override
    public String getName() {
        return "addcoins";
    }

    @Override
    public String getDescription() {
        return "Add coins to a player!";
    }

    @Override
    public Permission getRequiredPermission() {
        return Permission.ADMINISTRATOR;
    }
}
