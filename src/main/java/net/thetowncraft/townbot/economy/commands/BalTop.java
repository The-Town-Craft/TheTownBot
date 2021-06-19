package net.thetowncraft.townbot.economy.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.api.command_handler.minecraft.MinecraftCommand;
import net.thetowncraft.townbot.economy.EconomyManager;
import net.thetowncraft.townbot.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BalTop extends DiscordCommand {

    private EmbedBuilder embed;

    @Override
    public void execute(CommandEvent.Discord event) {

        List<MessageEmbed> embeds = new ArrayList<>();

        embed = new EmbedBuilder();
        embed.setColor(event.getMember().getColor());
        embed.setAuthor("Economy leaderboard!", null, event.getGuild().getIconUrl());

        AtomicInteger i = new AtomicInteger();
        AtomicInteger amountOnEmbed = new AtomicInteger();
        Utils.sortByIntValue(EconomyManager.getCoinMap()).forEach((key, value) -> {
            if(amountOnEmbed.get() > 20) {
                amountOnEmbed.set(0);
                embeds.add(embed.build());
                embed = new EmbedBuilder();
                embed.setColor(event.getMember().getColor());
            }
            i.getAndIncrement();
            amountOnEmbed.getAndIncrement();
            OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(key));
            String name = player.getName();
            if(name == null) {
                name = "[UNKNOWN_PLAYER]";
            }
            embed.appendDescription("\n" + i.get() + ". " + name + " :coin: " + value);
        });

        if(embeds.size() == 0) {
            event.getChannel().sendMessage(embed.build()).queue();
            return;
        }

        for(MessageEmbed embed : embeds) {
            event.getChannel().sendMessage(embed).queue();
        }
        event.getChannel().sendMessage(embed.build()).queue();
    }

    @Override
    public String getName() {
        return "baltop";
    }

    @Override
    public String getDescription() {
        return "View the economy leaderboard!";
    }

    @Override
    public Permission getRequiredPermission() {
        return null;
    }

    public static class MC extends MinecraftCommand {

        @Override
        public void execute(CommandEvent.Minecraft event) {
            HashMap<String, Integer> coinMap = Utils.sortByIntValue(EconomyManager.getCoinMap());

            String listOfPlayers = "";

            int i = 0;
            for(Map.Entry<String, Integer> entry : coinMap.entrySet()) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(entry.getKey()));
                String name = player.getName();
                if(name == null) continue;

                Integer value = entry.getValue();
                if(value == null) continue;

                i++;

                listOfPlayers += ChatColor.RESET + "\n" + i + ". " + ChatColor.YELLOW + name + " " + ChatColor.BOLD + value;
            }

            event.getPlayer().sendMessage(ChatColor.BOLD + "" + ChatColor.GOLD + "Economy Leaderboard\n--------" + listOfPlayers);
        }

        @Override
        public boolean isAdminCommand() {
            return false;
        }

        @Override
        public String getName() {
            return "baltop";
        }
    }
}