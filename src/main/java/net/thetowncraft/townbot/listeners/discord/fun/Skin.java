package net.thetowncraft.townbot.listeners.discord.fun;

import net.dv8tion.jda.api.Permission;
import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
import net.thetowncraft.townbot.util.SkinRender;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Skin extends DiscordCommand {

    @Override
    public void execute(CommandEvent.Discord event) {
        String[] args = event.getArgs();
        OfflinePlayer player;
        if(args.length == 1) {
            event.getChannel().sendMessage(":x: Please specify a username!").queue();
            return;
        }
        player = Bukkit.getOfflinePlayer(args[1]);
        String skin = SkinRender.renderSkin(player);
        String head = SkinRender.renderHead(player);
        String playerName = player.getName();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor(playerName + "'s Skin!", null, head);
        embed.setTitle("Download Image", skin);
        if(player.isOnline()) {
            embed.appendDescription("\n:green_circle: **Online**");
        }
        if(!player.isOnline()) {
            if(player.isBanned()) {
                embed.appendDescription("\n:red_circle: **Banned**");
            }
            if(!player.isBanned()) {
                embed.appendDescription("\n:red_circle: **Offline**");
            }
        }
        embed.setColor(Color.GREEN);
        embed.setImage(skin);
        event.getChannel().sendMessage(embed.build()).queue();
    }

    @Override
    public String getName() {
        return "skin";
    }

    @Override
    public String getDescription() {
        return "Render a player's skin!";
    }

    @Override
    public Permission getRequiredPermission() {
        return null;
    }
}
