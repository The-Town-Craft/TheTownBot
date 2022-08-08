package net.thetowncraft.townbot.factions.economy.shop.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.factions.economy.EconomyManager;
import net.thetowncraft.townbot.factions.economy.shop.ShopItem;
import net.thetowncraft.townbot.factions.economy.shop.ShopManager;
import net.thetowncraft.townbot.util.Constants;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ShopViewCommand extends DiscordCommand {

    @Override
    public void execute(CommandEvent.Discord event) {
        String[] args = event.getArgs();
        if(args.length == 1) {
            event.getChannel().sendMessage(ShopManager.getShopEmbed().build()).queue();
            return;
        }

        String name = Arrays.stream(args).skip(1).collect(Collectors.joining(" "));
        ShopItem item = ShopManager.getItemByName(name);
        if(item == null) {
            event.getChannel().sendMessage(":x: **Error**! Could not find an item by the name of \"" + name + "\"").queue();
            return;
        }
        EmbedBuilder embed = item.getEmbed();
        if(EconomyManager.getCoinBalance(event.getMember()) < item.getPrice()) {
            embed.appendDescription("\nFind out how you can earn coins in the " + Constants.COINS_CHANNEL.getAsMention() + " channel!");
        }
        event.getChannel().sendMessage(embed.build()).queue();
    }

    @Override
    public String getName() {
        return "shop";
    }

    @Override
    public String getDescription() {
        return "Shop for items!";
    }

    @Override
    public Permission getRequiredPermission() {
        return null;
    }
}
