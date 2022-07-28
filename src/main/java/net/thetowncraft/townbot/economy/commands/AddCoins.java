package net.thetowncraft.townbot.economy.commands;

import net.dv8tion.jda.api.Permission;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.economy.EconomyManager;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;
import java.util.stream.Collectors;

public class AddCoins extends UpdateCoinCommand {

    @Override
    public void updateCoins(CommandEvent.Discord event, OfflinePlayer player, int amount) {
        String[] args = event.getArgs();
        if(args.length < 4) {
            EconomyManager.addCoins(player.getUniqueId().toString(), amount, null);
        }
        else {
            String reason = Arrays.stream(args).skip(3).collect(Collectors.joining(" "));
            EconomyManager.addCoins(player.getUniqueId().toString(), amount, reason);
        }
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
