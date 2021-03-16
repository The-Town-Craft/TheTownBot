package net.thetowncraft.townbot.listeners.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.util.Constants;
import net.thetowncraft.townbot.util.SkinRender;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class Unban extends DiscordCommand {
    @Override
    public void execute(CommandEvent.Discord event) {
        String[] args = event.getArgs();

        if(args.length == 1) {
            event.getChannel().sendMessage(":x: Please specify a username!\n**Correct usage**: `/unban` `username`").queue();
            return;
        }
        OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
        String playerName = player.getName();
        if(playerName == null) {
            event.getChannel().sendMessage(":x: Player does not exist!").queue();
            return;
        }
        if(!player.isBanned()) {
            event.getChannel().sendMessage(":x: **" + player.getName() + "** is not banned!").queue();
            return;
        }
        Bukkit.getServer().getBanList(BanList.Type.NAME).pardon(args[1]);
        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor(player.getName() + " was unbanned", null, SkinRender.renderFace(player));
        embed.setColor(Constants.GREEN);
        event.getChannel().sendMessage(embed.build()).queue();
    }

    @Override
    public String getName() {
        return "unban";
    }

    @Override
    public String getDescription() {
        return "Unban a player from the Minecraft server!";
    }

    @Override
    public Permission getRequiredPermission() {
        return Permission.BAN_MEMBERS;
    }
}
