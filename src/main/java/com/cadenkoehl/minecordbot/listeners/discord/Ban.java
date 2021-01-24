package com.cadenkoehl.minecordbot.listeners.discord;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.cadenkoehl.minecordbot.MinecordBot;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Ban extends ListenerAdapter {
	
	static Player onlinePlayer;
	
	@SuppressWarnings("deprecation")
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");
		String username = Arrays.stream(args).skip(1).collect(Collectors.joining(" "));
		
		Member mod = event.getMember();
			if(args[0].equalsIgnoreCase(MinecordBot.prefix + "mcban")) {
				if(mod.hasPermission(Permission.ADMINISTRATOR)) {
					OfflinePlayer player = Bukkit.getOfflinePlayer(username);
					onlinePlayer = Bukkit.getPlayer(username);
					if(player.isBanned()) {
					event.getChannel().sendMessage(username + " is already banned!").queue();
					}
					if(!player.isBanned()) {
						Bukkit.getBanList(BanList.Type.NAME).addBan(username, "Please contact " + event.getAuthor().getAsTag() + " if you believe this is a mistake.", null, event.getAuthor().getName());
						event.getChannel().sendMessage("Banned " + username + ". Reason: Please contact " + event.getAuthor().getAsTag() + " if you believe this is a mistake.").queue();
						
				}
			}
				if(!mod.hasPermission(Permission.ADMINISTRATOR)) {
					event.getChannel().sendMessage("You can't use that!").queue();
				}
		}
	}
}
