package com.cadenkoehl.minecordbot.listeners.minecraft;

import com.cadenkoehl.minecordbot.accountlink.AccountManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.cadenkoehl.minecordbot.Constants;
import com.cadenkoehl.minecordbot.MinecordBot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.md_5.bungee.api.ChatColor;



public class MinecraftChatListener implements Listener {
	AccountManager manager = new AccountManager();

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		
		String message = event.getMessage();
		String player = event.getPlayer().getName();

		if(message.contains("@everyone") || message.contains("@here")) {
			event.getPlayer().kickPlayer("Haha you thought that would work didn't you!");
			Bukkit.getServer().broadcastMessage(ChatColor.RED + player + " was kicked because they tried to @everyone!");
			event.setCancelled(true);
			return;
		}

		MinecordBot.jda.getTextChannelById(Constants.chatLink).sendMessage(">>> <:minecraft_icon:790295561307684925> **<" + player + ">** " + message).queue();
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		double X = event.getEntity().getLocation().getX();
		double Y = event.getEntity().getLocation().getY();
		double Z = event.getEntity().getLocation().getZ();
		
		String deathMsg = event.getDeathMessage();
		EmbedBuilder embed = new EmbedBuilder();
		embed.setDescription("```css\n[" + deathMsg + "]\n```");
		embed.setColor(0xb83838);
		
		EmbedBuilder log = new EmbedBuilder();
		log.setDescription("```css\n[" + deathMsg + "]\n```\n");
		log.setColor(0xb83838);
		log.addField("X: " + X, "", false);
		log.addField("Y: " + Y, "", false);
		log.addField("Z: " + Z, "", false);
		
		MinecordBot.jda.getTextChannelById(Constants.chatLink).sendMessage(embed.build()).queue();
		MinecordBot.jda.getTextChannelById(Constants.logChannel).sendMessage(log.build()).queue();
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		String playerName = event.getPlayer().getName();
		EmbedBuilder embed = new EmbedBuilder();
		Member member = manager.getDiscordMember(player);
		if(member == null) {
			embed.setAuthor(playerName + " joined the game");
		}
		if(member != null) {
			embed.setAuthor(playerName + " joined the game", null, member.getUser().getEffectiveAvatarUrl());
		}
		embed.setColor(0x50bb5f);

		MinecordBot.jda.getTextChannelById(Constants.chatLink).sendMessage(embed.build()).queue();
		MinecordBot.jda.getTextChannelById(Constants.logChannel).sendMessage(embed.build()).queue();
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		String playerName = event.getPlayer().getName();
		EmbedBuilder embed = new EmbedBuilder();
		Member member = manager.getDiscordMember(player);
		if(member == null) {
			embed.setAuthor(playerName + " left the game");
		}
		if(member != null) {
			embed.setAuthor(playerName + " left the game", null, member.getUser().getEffectiveAvatarUrl());
		}
		embed.setColor(0xb83838);
		
		MinecordBot.jda.getTextChannelById(Constants.chatLink).sendMessage(embed.build()).queue();
		MinecordBot.jda.getTextChannelById(Constants.logChannel).sendMessage(embed.build()).queue();
	}
	
	@EventHandler
	public void onBan(PlayerQuitEvent event) {
		if(event.getPlayer().isBanned()) {
			String player = event.getPlayer().getName();
			EmbedBuilder embed = new EmbedBuilder();
			embed.setTitle(player + " was banned");
			embed.setColor(0xb83838);
			
			MinecordBot.jda.getTextChannelById(Constants.chatLink).sendMessage(embed.build()).queue();
			MinecordBot.jda.getTextChannelById(Constants.logChannel).sendMessage(embed.build()).queue();
			
		}
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if(event.getEntityType() == EntityType.VILLAGER) {
			String deathReason = event.getEntity().getLastDamageCause().getCause().name() + "\n";
			double posX = event.getEntity().getLocation().getX();
			double posY = event.getEntity().getLocation().getY();
			double posZ = event.getEntity().getLocation().getZ();
			EmbedBuilder embed = new EmbedBuilder();
			embed.setDescription("```css\n[A Villager Has Died]\n```");
			embed.addField("Reason: ", deathReason, false);
			if(event.getEntity().getKiller() != null) {
				embed.addField("Killer: " + event.getEntity().getKiller().getName(), "", false);
			}
			embed.addField("X: " + posX, "", false);
			embed.addField("Y: " + posY, "", false);
			embed.addField("Z: " + posZ, "", false);
			embed.setColor(0xb83838);
		
			MinecordBot.jda.getTextChannelById(Constants.chatLink).sendMessage(embed.build()).queue();
			MinecordBot.jda.getTextChannelById(Constants.logChannel).sendMessage(embed.build()).queue();
		
		}
	}
	
	@EventHandler
	public void onTotem(EntityResurrectEvent event) {
		if(!event.isCancelled()) {
		
			if(event.getEntityType().equals(EntityType.PLAYER)) {
				double X = event.getEntity().getLocation().getX();
				double Y = event.getEntity().getLocation().getX();
				double Z = event.getEntity().getLocation().getX();
				String entity = event.getEntity().getName();
				String cause = event.getEntity().getLastDamageCause().getCause().name();
				//mc chat
				Bukkit.broadcastMessage(ChatColor.GOLD + entity + " lost their totem!");
			
				//discord chat embed
				EmbedBuilder embed = new EmbedBuilder();
				embed.setDescription("```css\n" + entity + " lost their totem!\n```");
				embed.setColor(0xb83838);
			
				//discord log embed
				EmbedBuilder log = new EmbedBuilder();

				log.setTitle(entity + " activated a totem");
				log.setDescription("Entity data:\n");
				log.addField("X: " +  X, "", false);
				log.addField("Y: " +  Y, "", false);
				log.addField("Z: " +  Z, "", false);
				log.setColor(0xb83838);
				if(event.getEntity().getKiller() != null) {
					String killer = event.getEntity().getKiller().getName();
					log.addField("Killer: " +  killer, "", false);
				}
				log.addField("Reason: " +  cause, "", false);
			
				MinecordBot.jda.getTextChannelById(Constants.chatLink).sendMessage(embed.build()).queue();
				MinecordBot.jda.getTextChannelById(Constants.logChannel).sendMessage(log.build()).queue();
			}
		}
	}
}