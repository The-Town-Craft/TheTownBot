package net.thetowncraft.townbot.listeners.discord.commands;

import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.util.Constants;
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

public class DiscordUnmute extends DiscordCommand {

    @Override
    public void execute(CommandEvent.Discord event) {
        String[] args = event.getArgs();

        Member member = event.getMember();
        if(!member.hasPermission(Permission.BAN_MEMBERS)) {
            event.getChannel().sendMessage(":x: You can't use that!").queue();
            return;
        }
        if(args.length == 1) {
            event.getChannel().sendMessage(":x: Please specify a player to unmute!").queue();
            return;
        }
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
        MuteManager manager = new MuteManager();

        if(manager.unmutePlayer(offlinePlayer)) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Constants.GREEN);
            embed.setAuthor(offlinePlayer.getName() + " was unmuted!", null, SkinRender.renderHead(offlinePlayer));
            event.getChannel().sendMessage(embed.build()).queue();
        }
        else {
            event.getChannel().sendMessage(":x: Player is not muted!").queue();
        }
    }

    @Override
    public String getName() {
        return "mute";
    }

    @Override
    public String getDescription() {
        return "Unmutes a player on the Minecraft server!";
    }

    @Override
    public Permission getRequiredPermission() {
        return Permission.VOICE_MUTE_OTHERS;
    }
}
