package com.cadenkoehl.minecordbot.listeners.discord.commands;

import com.cadenkoehl.minecordbot.Constants;
import com.cadenkoehl.minecordbot.MinecordBot;
import com.cadenkoehl.minecordbot.listeners.chatmute.MuteManager;
import com.cadenkoehl.minecordbot.listeners.util.SkinRender;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class DiscordMute extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        if(args[0].equalsIgnoreCase(MinecordBot.prefix + "mute")) {
            if(event.isWebhookMessage()) {
                return;
            }
            if(event.getAuthor().isBot()) {
                return;
            }
            Member member = event.getMember();
            if(!member.hasPermission(Permission.BAN_MEMBERS)) {
                event.getChannel().sendMessage(":x: You can't use that!").queue();
                return;
            }
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
    }
}
