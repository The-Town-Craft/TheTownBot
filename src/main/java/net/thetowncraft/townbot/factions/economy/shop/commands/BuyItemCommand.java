package net.thetowncraft.townbot.factions.economy.shop.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.factions.economy.EconomyManager;
import net.thetowncraft.townbot.factions.economy.shop.ShopItem;
import net.thetowncraft.townbot.factions.economy.shop.ShopManager;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
import net.thetowncraft.townbot.util.Constants;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;
import java.util.stream.Collectors;

public class BuyItemCommand extends DiscordCommand {

    @Override
    public void execute(CommandEvent.Discord event) {
        Member member = event.getMember();
        OfflinePlayer player = AccountManager.getInstance().getMinecraftPlayer(member);
        if(player == null) {
            event.getChannel().sendMessage(":x: **Error**! Your account must be linked to use this command!").queue();
            return;
        }

        String[] args = event.getArgs();
        if(args.length == 1) {
            event.getChannel().sendMessage("What would you like to buy?").queue();
            return;
        }

        String name = Arrays.stream(args).skip(1).collect(Collectors.joining(" "));
        ShopItem item = ShopManager.getItemByName(name);
        if(item == null) {
            event.getChannel().sendMessage(":x: **Error**! Could not find an item by the name of \"" + name + "\"").queue();
            return;
        }

        if(item.possessedBy(member)) {
            event.getChannel().sendMessage(":x: **Error**! You already have this item!").queue();
            return;
        }

        int balance = EconomyManager.getCoinBalance(player);
        if(balance < item.getPrice()) {
            event.getChannel().sendMessage(":x: **Error**! Insufficient funds! Find out how you can earn coins in the " + Constants.COINS_CHANNEL.getAsMention() + " channel!").queue();
            return;
        }

        EconomyManager.subtractCoins(player.getUniqueId().toString(), item.getPrice());
        event.getGuild().addRoleToMember(member, item.getRole()).queue();
        event.getGuild().addRoleToMember(member, Constants.PURCHASES_CATEGORY_ROLE).queue();
        event.getChannel().sendMessage(":white_check_mark: **Success**! You have purchased the **" + item.getName() + "** for " + item.getPrice() + " coins!").queue();
    }

    @Override
    public String getName() {
        return "buy";
    }

    @Override
    public String getDescription() {
        return "Buy an item from the shop!";
    }

    @Override
    public Permission getRequiredPermission() {
        return null;
    }
}
