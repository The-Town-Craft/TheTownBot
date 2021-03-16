package net.thetowncraft.townbot.listeners.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.util.Constants;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Set;

public class BanList extends DiscordCommand {
    @Override
    public void execute(CommandEvent.Discord event) {
        if(event.isWebhookMessage()) {return;}
        if(event.getAuthor().isBot()) {return;}
        Member member = event.getMember();
        if(!member.hasPermission(Permission.BAN_MEMBERS)) {
            event.getChannel().sendMessage(":x: You can't use that!").queue();
            return;
        }
        EmbedBuilder embed = new EmbedBuilder();
        Set<OfflinePlayer> players = Bukkit.getServer().getBannedPlayers();
        embed.setTitle("Minecraft BanList");
        for (OfflinePlayer player : players) {
            embed.appendDescription("\n" + player.getName());
        }
        embed.setColor(Constants.RED);
        event.getChannel().sendMessage(embed.build()).queue();
    }

    @Override
    public String getName() {
        return "banlist";
    }

    @Override
    public String getDescription() {
        return "See the Minecraft banlist!";
    }

    @Override
    public Permission getRequiredPermission() {
        return Permission.BAN_MEMBERS;
    }
}
