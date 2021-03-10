package com.cadenkoehl.minecordbot.listeners.discord.fun;

import com.cadenkoehl.minecordbot.Bot;
import com.cadenkoehl.minecordbot.listeners.accountlink.AccountManager;
import com.cadenkoehl.minecordbot.listeners.util.SkinRender;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Skin extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        if(args[0].equalsIgnoreCase(Bot.prefix + "skin")) {
            if(event.isWebhookMessage()) {
                return;
            }
            if(event.getAuthor().isBot()) {
                return;
            }
            Member member;

            System.out.println(args.length);
            AccountManager manager = AccountManager.getInstance();

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
    }
}
