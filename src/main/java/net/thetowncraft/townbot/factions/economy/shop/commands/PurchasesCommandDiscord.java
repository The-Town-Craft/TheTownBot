package net.thetowncraft.townbot.factions.economy.shop.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.factions.economy.shop.ShopItem;
import net.thetowncraft.townbot.factions.economy.shop.ShopManager;

import java.util.List;

public class PurchasesCommandDiscord extends DiscordCommand {

    @Override
    public void execute(CommandEvent.Discord event) {
        Member member = event.getMember();
        List<ShopItem> purchases = ShopManager.getPurchases(member);
        if(purchases.size() == 0) {
            event.getChannel().sendMessage(":x: You haven't purchased anything yet! Type `" + Bot.prefix + "shop` for more information!").queue();
            return;
        }

        event.getChannel().sendMessage(ShopManager.getPurchasesEmbed(member).build()).queue();
    }

    @Override
    public String getName() {
        return "purchases";
    }

    @Override
    public String getDescription() {
        return "View your purchases from the shop!";
    }

    @Override
    public Permission getRequiredPermission() {
        return null;
    }
}
