package net.thetowncraft.townbot.listeners.minecraft.chat;

import net.dv8tion.jda.api.entities.Member;
import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
import net.thetowncraft.townbot.listeners.chatmute.MuteManager;
import net.thetowncraft.townbot.util.SkinRender;
import net.thetowncraft.townbot.util.Constants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;


public class MinecraftChatListener implements Listener {

	AccountManager manager = AccountManager.getInstance();

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		event.setCancelled(true);
		String message = event.getMessage();
		String name = event.getPlayer().getName();
		Player player = event.getPlayer();

		if(message.contains("@everyone") || message.contains("@here")) {
			return;
		}
		MuteManager mute = new MuteManager();
		if(mute.isMuted(player)) {
			player.sendMessage(ChatColor.RED + "You are muted and cannot send messages!");
			return;
		}

		AccountManager manager = AccountManager.getInstance();
		Member member = manager.getDiscordMember(player);
		Bukkit.getServer().broadcastMessage("<" + manager.getMinecraftChatColor(member) + name + ChatColor.RESET + "> " + message);

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
		sendPlayerJoinMessage(event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		sendPlayerLeaveMessage(event.getPlayer());
	}

	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		sendPlayerKickMessage(event.getPlayer(), event.getReason());
	}

	public static void sendPlayerJoinMessage(Player player) {
		EmbedBuilder embed = getJoinLeaveEmbed(player, "joined the game", Constants.GREEN);
		sendMcChatEmbed(embed);
	}

	public static void sendPlayerKickMessage(Player player, String reason) {
		if(!AccountManager.getInstance().isLinked(player)) {
			return;
		}

		EmbedBuilder embed = getJoinLeaveEmbed(player, "was kicked", Constants.RED);

		embed.setDescription("Reason: " + reason);
		sendMcChatEmbed(embed);
	}

	public static void sendPlayerLeaveMessage(Player player) {
		EmbedBuilder embed = getJoinLeaveEmbed(player, "left the game", Constants.RED);
		sendMcChatEmbed(embed);
	}

	public static EmbedBuilder getJoinLeaveEmbed(Player player, String action, int color) {
		String playerName = player.getName();
		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(color);
		embed.setAuthor(playerName + " " + action, null, SkinRender.renderHead(player));
		return embed;
	}

	public static void sendMcChatEmbed(EmbedBuilder embed) {
		Bot.jda.getTextChannelById(Constants.MC_CHAT).sendMessage(embed.build()).queue();
		Bot.jda.getTextChannelById(Constants.MC_LOGS).sendMessage(embed.build()).queue();
	}

	@EventHandler
	public void onBan(PlayerQuitEvent event) {
		if(event.getPlayer().isBanned()) {
			String player = event.getPlayer().getName();
			EmbedBuilder embed = new EmbedBuilder();
			embed.setAuthor(player + " was banned", null, SkinRender.renderHead(event.getPlayer()));
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
			
				Bot.jda.getTextChannelById(Constants.MC_CHAT).sendMessage(">>> <:Totem:995628428281794650> **" + entity + "** lost their totem!").queue();
				Bot.jda.getTextChannelById(Constants.MC_LOGS).sendMessage(log.build()).queue();
			}
		}
	}
}