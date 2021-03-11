package net.thetowncraft.townbot.listeners.minecraft.chat;

import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
import net.thetowncraft.townbot.listeners.chatmute.MuteManager;
import net.thetowncraft.townbot.util.SkinRender;
import net.thetowncraft.townbot.util.Constants;
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

import net.dv8tion.jda.api.EmbedBuilder;
import net.md_5.bungee.api.ChatColor;


public class MinecraftChatListener implements Listener {

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Plugin plugin = Plugin.get();

		String message = event.getMessage();
		String name = event.getPlayer().getName();
		Player player = event.getPlayer();

		if(message.contains("@everyone") || message.contains("@here")) {
			event.setCancelled(true);
			return;
		}
		MuteManager mute = new MuteManager();
		if(mute.isMuted(player)) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You are muted and cannot send messages!");
			return;
		}

		Bot.jda.getTextChannelById(Constants.MC_CHAT).sendMessage(">>> <:minecraft_icon:790295561307684925> **<" + name + ">** " + message).queue();
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
		
		Bot.jda.getTextChannelById(Constants.MC_CHAT).sendMessage(embed.build()).queue();
		Bot.jda.getTextChannelById(Constants.MC_LOGS).sendMessage(log.build()).queue();
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		AccountManager manager = AccountManager.getInstance();
		Player player = event.getPlayer();
		if(!manager.isLinked(player)) {
			return;
		}
		String playerName = event.getPlayer().getName();
		EmbedBuilder embed = new EmbedBuilder();
		embed.setAuthor(playerName + " joined the game", null, SkinRender.renderHead(event.getPlayer()));
		embed.setColor(0x50bb5f);

		Bot.jda.getTextChannelById(Constants.MC_CHAT).sendMessage(embed.build()).queue();
		Bot.jda.getTextChannelById(Constants.MC_LOGS).sendMessage(embed.build()).queue();
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		AccountManager manager = AccountManager.getInstance();
		Player player = event.getPlayer();
		if(!manager.isLinked(player)) {
			return;
		}
		String playerName = event.getPlayer().getName();
		EmbedBuilder embed = new EmbedBuilder();
		embed.setAuthor(playerName + " left the game", null, SkinRender.renderHead(event.getPlayer()));
		embed.setColor(0xb83838);
		
		Bot.jda.getTextChannelById(Constants.MC_CHAT).sendMessage(embed.build()).queue();
		Bot.jda.getTextChannelById(Constants.MC_LOGS).sendMessage(embed.build()).queue();
	}
	
	@EventHandler
	public void onBan(PlayerQuitEvent event) {
		if(event.getPlayer().isBanned()) {
			String player = event.getPlayer().getName();
			EmbedBuilder embed = new EmbedBuilder();
			embed.setTitle(player + " was banned");
			embed.setColor(0xb83838);
			
			Bot.jda.getTextChannelById(Constants.MC_CHAT).sendMessage(embed.build()).queue();
			Bot.jda.getTextChannelById(Constants.MC_LOGS).sendMessage(embed.build()).queue();
		}
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if(event.getEntityType() == EntityType.VILLAGER) {
			String deathReason = event.getEntity().getLastDamageCause().getCause().name().toLowerCase().replace("_", " ") + "\n";
			double posX = event.getEntity().getLocation().getX();
			double posY = event.getEntity().getLocation().getY();
			double posZ = event.getEntity().getLocation().getZ();
			EmbedBuilder embed = new EmbedBuilder();
			EmbedBuilder log = new EmbedBuilder();
			embed.setDescription("```css\n[A Villager Has Died]\n```");
			log.setDescription("```css\n[A Villager Has Died]\n```");
			embed.addField("Reason: ", deathReason, false);
			log.addField("Reason: ", deathReason, false);
			if(event.getEntity().getKiller() != null) {
				embed.addField("Killer: " + event.getEntity().getKiller().getName(), "", false);
				log.addField("Killer: " + event.getEntity().getKiller().getName(), "", false);
			}

			log.addField("X: " + posX, "", false);
			log.addField("Y: " + posY, "", false);
			log.addField("Z: " + posZ, "", false);
			embed.setColor(0xb83838);
			log.setColor(0xb83838);
		
			Bot.jda.getTextChannelById(Constants.MC_CHAT).sendMessage(embed.build()).queue();
			Bot.jda.getTextChannelById(Constants.MC_LOGS).sendMessage(log.build()).queue();
		
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
			
				Bot.jda.getTextChannelById(Constants.MC_CHAT).sendMessage(embed.build()).queue();
				Bot.jda.getTextChannelById(Constants.MC_LOGS).sendMessage(log.build()).queue();
			}
		}
	}
}