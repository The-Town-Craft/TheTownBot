package com.cadenkoehl.minecordbot.listeners.discord.commands;

import com.cadenkoehl.minecordbot.Constants;
import com.cadenkoehl.minecordbot.MinecordBot;
import com.cadenkoehl.minecordbot.listeners.util.SkinRender;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class Ban extends ListenerAdapter {
	@Override
	public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");
		if(args[0].equalsIgnoreCase(MinecordBot.prefix + "ban") || args[0].equalsIgnoreCase(MinecordBot.prefix + "mcban")) {
			MinecordBot plugin = MinecordBot.getPlugin(MinecordBot.class);
			if(event.isWebhookMessage()) {return;}
			if(event.getAuthor().isBot()) {return;}
			Member member = event.getMember();
			if(!member.hasPermission(Permission.BAN_MEMBERS)) {
				event.getChannel().sendMessage(":x: You can't use that!").queue();
				return;
			}
			if(args.length == 1) {
				event.getChannel().sendMessage(":x: Please specify a username!\n**Correct usage**: `/ban` `username` `reason`").queue();
				return;
			}
			String reason = "Unspecified";
			if(args.length != 2) {
				reason = Arrays.stream(args).skip(2).collect(Collectors.joining(" "));
			}
			OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
			if(player.getName() == null) {
				event.getChannel().sendMessage(":x: Player does not exist!").queue();
				return;
			}
			if(player.isBanned()) {
				event.getChannel().sendMessage(":x: **" + player.getName() + "** is already banned!").queue();
				return;
			}
			Bukkit.getServer().getBanList(BanList.Type.NAME).addBan(player.getName(), reason, null, null);
			EmbedBuilder embed = new EmbedBuilder();
			embed.setAuthor(player.getName() + " was banned", null, SkinRender.renderFace(player));
			embed.setDescription("Reason: " + reason);
			embed.setColor(Constants.RED);
			event.getChannel().sendMessage(embed.build()).queue();
			if(player.isOnline()) {
				Player onlinePlayer = (Player) player;
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> onlinePlayer.kickPlayer("You are banned from this server"));
			}
		}
		if(args[0].equalsIgnoreCase(MinecordBot.prefix + "unban") || args[0].equalsIgnoreCase(MinecordBot.prefix + "mcunban") || args[0].equalsIgnoreCase(MinecordBot.prefix + "pardon")) {
			if(event.isWebhookMessage()) {return;}
			if(event.getAuthor().isBot()) {return;}
			Member member = event.getMember();
			if(!member.hasPermission(Permission.BAN_MEMBERS)) {
				event.getChannel().sendMessage(":x: You can't use that!").queue();
				return;
			}
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
		if(args[0].equalsIgnoreCase(MinecordBot.prefix + "banlist") || args[0].equalsIgnoreCase(MinecordBot.prefix + "mcbanlist")) {
			if(event.isWebhookMessage()) {return;}
			if(event.getAuthor().isBot()) {return;}
			Member member = event.getMember();
			if(!member.hasPermission(Permission.BAN_MEMBERS)) {
				event.getChannel().sendMessage(":x: You can't use that!").queue();
				return;
			}
			EmbedBuilder embed = new EmbedBuilder();
			Set<OfflinePlayer> players = Bukkit.getServer().getBannedPlayers();
			embed.setTitle("Minecraft Banlist");
			for (OfflinePlayer player : players) {
				embed.appendDescription("\n" + player.getName());
			}
			embed.setColor(Constants.RED);
			event.getChannel().sendMessage(embed.build()).queue();
		}
	}
}













