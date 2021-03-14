package net.thetowncraft.townbot.listeners.discord.commands;

import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.util.Constants;
import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.listeners.chatmute.MuteManager;
import net.thetowncraft.townbot.util.SkinRender;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class DiscordMute extends DiscordCommand {


    @Override
    public void execute(CommandEvent.Discord event) {
        String[] args = event.getArgs();

        if(args.length == 1) {
            event.getChannel().sendMessage(":x: Please specify a player to mute!").queue();
            return;
        }
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
        MuteManager manager = new MuteManager();

        if(manager.mutePlayer(offlinePlayer)) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Constants.RED);
            embed.setAuthor(offlinePlayer.getName() + " was muted!", null, SkinRender.renderHead(offlinePlayer));
            event.getChannel().sendMessage(embed.build()).queue();
        }
        else {
            event.getChannel().sendMessage(":x: Player is already muted!").queue();
        }
    }

    @Override
    public String getName() {
        return "mute";
    }

    @Override
    public String getDescription() {
        return "Mute a player on the Minecraft server!";
    }

    @Override
    public Permission getRequiredPermission() {
        return Permission.VOICE_MUTE_OTHERS;
    }
}
