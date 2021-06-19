package net.thetowncraft.townbot.economy.commands;

import net.dv8tion.jda.api.entities.Member;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.List;

public abstract class UpdateCoinCommand extends DiscordCommand {

    final String USAGE;

    public UpdateCoinCommand() {
        USAGE = "`/" + getName() + "` `@member` `amount`";
    }

    @Override
    public void execute(CommandEvent.Discord event) {
        String[] args = event.getArgs();
        if(args.length < 3) {
            event.getChannel().sendMessage(":x: Incomplete command! Usage: " + USAGE).queue();
            return;
        }

        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();

        int amount;
        OfflinePlayer player;

        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            event.getChannel().sendMessage("Please provide a valid integer for the amount of coins you wish to add! Usage: " + USAGE).queue();
            return;
        }

        if(mentionedMembers.size() == 0) {
            player = Bukkit.getOfflinePlayer(args[1]);
            if(!AccountManager.getInstance().isLinked(player)) {
                event.getChannel().sendMessage(":x: " + args[1] + "'s account is either not linked, or the player does not exist.").queue();
                return;
            }
        }
        else {
            player = AccountManager.getInstance().getMinecraftPlayer(mentionedMembers.get(0));
            if(player == null) {
                event.getChannel().sendMessage(":x: " + args[1] + "'s account is either not linked, or the player does not exist.").queue();
                return;
            }
        }
        updateCoins(event, player, amount);
    }

    public abstract void updateCoins(CommandEvent.Discord event, OfflinePlayer player, int amount);
}
