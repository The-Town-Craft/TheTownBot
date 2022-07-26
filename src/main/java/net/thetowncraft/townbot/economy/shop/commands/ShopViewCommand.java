package net.thetowncraft.townbot.economy.shop.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.economy.shop.ShopItem;
import net.thetowncraft.townbot.economy.shop.ShopManager;

public class ShopViewCommand extends DiscordCommand {

    @Override
    public void execute(CommandEvent.Discord event) {
        String[] args = event.getArgs();
        if(args.length == 1) {
            event.getChannel().sendMessage(ShopManager.getShopEmbed().build()).queue();
            return;
        }

        String name = args[1];
        ShopItem shop = ShopManager.getShopByName(name);
        if(shop == null) {
            event.getChannel().sendMessage(":x: **Error**! Could not find a shop by the name of \"" + name + "\"").queue();
            return;
        }
        event.getChannel().sendMessage(shop.getEmbed().build()).queue();
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
