package net.thetowncraft.townbot.economy.shop.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.economy.shop.ShopManager;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
import org.bukkit.OfflinePlayer;

public class ShopViewCommand extends DiscordCommand {

    @Override
    public void execute(CommandEvent.Discord event) {
        event.getChannel().sendMessage(ShopManager.getShopEmbed().build()).queue();
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
