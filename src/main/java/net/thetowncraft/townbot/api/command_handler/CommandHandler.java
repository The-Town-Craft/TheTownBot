package net.thetowncraft.townbot.api.command_handler;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.api.command_handler.minecraft.MinecraftCommand;
import net.thetowncraft.townbot.util.Constants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class CommandHandler {

    public static class Discord extends ListenerAdapter {

        @Override
        public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
            try {
                if(event.isWebhookMessage()) return;
                if(event.getAuthor().isBot()) return;

                String[] args = event.getMessage().getContentRaw().split("\\s+");

                Member member = event.getMember();
                if(member == null) return;

                for(DiscordCommand cmd : DiscordCommand.COMMANDS) {
                    if(args[0].equalsIgnoreCase(Bot.prefix + cmd.getName())) {
                        execute(member, cmd, event);
                    }
                    else if (Arrays.asList(cmd.getAliases()).contains(args[0].replace(Bot.prefix, ""))) {
                        execute(member, cmd, event);
                    }
                }
            }
            catch (Exception ex) {
                event.getChannel().sendMessage(":x: [**" + Constants.DEV_ROLE.getAsMention() + "**] **An exception has occurred with a Discord command!** `" + ex + "`").queue();
                ex.printStackTrace();
            }
        }

        private void execute(Member member, DiscordCommand cmd, GuildMessageReceivedEvent event) {
            if(!member.hasPermission(cmd.getRequiredPermission())) {
                event.getChannel().sendMessage(":x: You must have the **" + cmd.getRequiredPermission().getName() + "** permission to use this command!").queue();
                return;
            }
            cmd.execute(new CommandEvent.Discord(event, cmd));
        }

    }
}