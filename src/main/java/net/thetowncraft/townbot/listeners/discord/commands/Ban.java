package net.thetowncraft.townbot.listeners.discord.commands;

import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.util.Constants;
import net.thetowncraft.townbot.util.SkinRender;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Ban extends DiscordCommand {

	@Override
	public void execute(CommandEvent.Discord event) {
		String[] args = event.getArgs();
		Plugin plugin = Plugin.get();

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
		Bukkit.getServer().getBanList(org.bukkit.BanList.Type.NAME).addBan(player.getName(), reason, null, null);
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

	@Override
	public String getName() {
		return "ban";
	}

	@Override
	public String getDescription() {
		return "Ban a player from the Minecraft server!";
	}

	@Override
	public Permission getRequiredPermission() {
		return Permission.BAN_MEMBERS;
	}
}













