package com.cadenkoehl.minecordbot.listeners.discord.commands;

import com.cadenkoehl.minecordbot.MinecordBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class Help extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        if(args[0].equalsIgnoreCase(MinecordBot.prefix + "help")) {
            if(args.length == 1) {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("The Town Discord Bot!");
                embed.setDescription("I am a utility bot for The Town Discord server that links the Discord and Minecraft chat in #mc-chat, and much more! Here's a list of my commands: \n" +
                        "`/help` `staff` - View list of staff commands!\n" +
                        "`/onlineplayers` - find out who's online the Minecraft server!\n" +
                        "`/skin` `[username]` - Render someone's Minecraft skin!\n");
                event.getChannel().sendMessage(embed.build()).queue();
                return;
            }
            if(args[1].equalsIgnoreCase("staff")) {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("Staff Commands");
                embed.setDescription(
                        "If you need non-Minecraft staff commands, do `-help` for CadenBot\n"
                        + "`/whitelist` `add` `[player]` - Add a player to the whitelist\n"
                        + "`/whitelist` `remove` `[player]` - Remove a player from the whitelist\n"
                        + "`/whitelist` `list` - View the entire whitelist\n"
                        + "`/ban` `[player]` `[reason]` - Ban a player\n"
                        + "`/pardon` `[player]` - Unban a player\n"
                        + "`/banlist` - View the entire banlist\n"
                        + "`/mute` `[player]` - Mute a player\n"
                        + "`/unmute` `[player]` - Unmute a player\n"
                        + "**All commands work on the Discord and Minecraft server!**"
                );
                event.getChannel().sendMessage(embed.build()).queue();
            }
        }
    }
}
