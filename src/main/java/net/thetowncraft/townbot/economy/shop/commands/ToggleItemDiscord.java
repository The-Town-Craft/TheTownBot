package net.thetowncraft.townbot.economy.shop.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.economy.shop.ShopItem;
import net.thetowncraft.townbot.economy.shop.ShopManager;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ToggleItemDiscord extends DiscordCommand {

    @Override
    public void execute(CommandEvent.Discord event) {
        Member member = event.getMember();
        OfflinePlayer player = AccountManager.getInstance().getMinecraftPlayer(member);
        if(player == null) {
            event.getChannel().sendMessage(":x: **Error**! Your account is not linked!").queue();
            return;
        }

        String[] args = event.getArgs();
        if(args.length == 1) {
            event.getChannel().sendMessage("What item would you like to toggle?").queue();
            return;
        }

        String name = Arrays.stream(args).skip(1).collect(Collectors.joining(" "));
        ShopItem item = ShopManager.getItemByName(name);
        if(item == null) {
            event.getChannel().sendMessage(":x: **Error**! Could not find an item by the name of \"" + name + "\"! Type `" + Bot.prefix + "shop` for more info!").queue();
            return;
        }

        if(!item.possessedBy(player)) {
            event.getChannel().sendMessage(":x: **You do not own this item**!").queue();
            return;
        }

        if(item.isOff(player)) {
            item.turnOn(player);
            event.getChannel().sendMessage(":white_check_mark: **Success**! **" + item.getName() + "** has been enabled!").queue();
        }
        else {
            item.turnOff(player);
            event.getChannel().sendMessage(":white_check_mark: **Success**! **" + item.getName() + "** has been disabled!").queue();
        }
    }

    @Override
    public String getName() {
        return "toggle";
    }

    @Override
    public String getDescription() {
        return "Toggle shop items on and off!";
    }

    @Override
    public Permission getRequiredPermission() {
        return null;
    }
}
