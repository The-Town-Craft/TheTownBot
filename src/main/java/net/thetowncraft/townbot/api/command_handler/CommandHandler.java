package net.thetowncraft.townbot.api.command_handler;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.api.command_handler.minecraft.MinecraftCommand;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;

public class CommandHandler {

    public static class Minecraft implements Listener {
        @EventHandler
        public void onCommand(PlayerCommandPreprocessEvent event) {
            for(MinecraftCommand cmd : MinecraftCommand.COMMANDS) {
                if(!event.getPlayer().isOp() && cmd.isAdminCommand()) {
                    event.getPlayer().sendMessage(ChatColor.RED + "You do not have permission to use this command!");
                    return;
                }
                if(event.getMessage().equals(cmd.getName())) {
                    cmd.execute(new CommandEvent.Minecraft(event, cmd));
                }
            }
        }
    }

    public static class Discord extends ListenerAdapter {
        @Override
        public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

            if(event.isWebhookMessage()) return;
            if(event.getAuthor().isBot()) return;

            Member member = event.getMember();
            if(member == null) return;

            for(DiscordCommand cmd : DiscordCommand.COMMANDS) {
                if(!member.hasPermission(cmd.getRequiredPermission())) {
                    event.getChannel().sendMessage(":x: You must have the **" + cmd.getRequiredPermission().getName() + "** permission to use this command!").queue();
                    return;
                }
                cmd.execute(new CommandEvent.Discord(event, cmd));
            }
        }
    }
}