package net.thetowncraft.townbot.listeners.discord.commands;

import net.dv8tion.jda.api.Permission;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
import net.thetowncraft.townbot.listeners.minecraft.player_activity.afk.AFKManager;
import net.thetowncraft.townbot.util.Constants;
import net.thetowncraft.townbot.util.Utils;
import org.bukkit.Bukkit;

import net.thetowncraft.townbot.Bot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

public class OnlinePlayers extends DiscordCommand {

	@Override
	public void execute(CommandEvent.Discord event) {
		List<Player> players = Utils.getEffectiveOnlinePlayers();
		int playerCount = players.size();

		EmbedBuilder embed = new EmbedBuilder();

		if(playerCount == 1) embed.setTitle("There is " + playerCount + " player online.");
		else embed.setTitle("There are " + playerCount + " players online." );
		embed.setColor(0x50bb5f);
		for(Player player : players) {
			String name = player.getName();
			if(AFKManager.isAFK(player)) embed.appendDescription("\n[AFK] " + name);
			else embed.appendDescription("\n" + name);
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
	public String[] getAliases() {
		return new String[]{"online", "onl"};
	}

	@Override
	public Permission getRequiredPermission() {
		return null;
	}
}