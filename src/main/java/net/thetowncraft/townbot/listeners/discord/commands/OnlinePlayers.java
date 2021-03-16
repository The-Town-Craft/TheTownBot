package net.thetowncraft.townbot.listeners.discord.commands;

import net.dv8tion.jda.api.Permission;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.util.Constants;
import org.bukkit.Bukkit;

import net.thetowncraft.townbot.Bot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.entity.Player;

import java.util.Collection;

public class OnlinePlayers extends DiscordCommand {

	@Override
	public void execute(CommandEvent.Discord event) {
		Collection<? extends Player> players = Bukkit.getServer().getOnlinePlayers();
		int playerCount = players.size();
		int maxPlayerCount = Bukkit.getServer().getMaxPlayers();

		EmbedBuilder embed = new EmbedBuilder();

		embed.setTitle("There are " + playerCount + " out of " + maxPlayerCount + " players online." );
		embed.setColor(0x50bb5f);
		for(Player player : players) {
			String name = player.getName();
			embed.appendDescription("\n" + name);
		}
		event.getChannel().sendMessage(embed.build()).queue();
		Bot.jda.getTextChannelById(Constants.MC_LOGS).sendMessage(embed.build()).queue();
	}

	@Override
	public String getName() {
		return "onlineplayers";
	}

	@Override
	public String getDescription() {
		return "See a list of online players!";
	}

	@Override
	public Permission getRequiredPermission() {
		return null;
	}
}