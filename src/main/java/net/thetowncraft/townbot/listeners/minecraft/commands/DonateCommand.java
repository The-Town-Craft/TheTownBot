package net.thetowncraft.townbot.listeners.minecraft.commands;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.minecraft.MinecraftCommand;
import net.thetowncraft.townbot.util.Constants;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class DonateCommand extends MinecraftCommand {

    public static void sendDonationMessage(Player player) {
        player.sendMessage(ChatColor.GREEN + "Enjoying the server? Donations help keep it running! :) Consider visiting #donations on the Discord server!");
        Constants.CADEN.openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage(":white_check_mark: Just sent **" + player.getName() + "** a donation reminder.").queue();
        });
    }

    @Override
    public void execute(CommandEvent.Minecraft event) {
        sendDonationMessage(event.getPlayer());
    }

    @Override
    public boolean isAdminCommand() {
        return false;
    }

    @Override
    public String getName() {
        return "donate";
    }
}
