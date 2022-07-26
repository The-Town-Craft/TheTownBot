package net.thetowncraft.townbot.economy.shop.commands;

import net.dv8tion.jda.api.Permission;
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
        ShopItem item = ShopManager.getItemByName(name);
        if(item == null) {
            event.getChannel().sendMessage(":x: **Error**! Could not find an item by the name of \"" + name + "\"").queue();
            return;
        }
        event.getChannel().sendMessage(item.getEmbed().build()).queue();
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
