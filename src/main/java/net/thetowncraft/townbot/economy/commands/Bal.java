package net.thetowncraft.townbot.economy.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.api.command_handler.minecraft.MinecraftCommand;
import net.thetowncraft.townbot.economy.EconomyManager;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
import net.thetowncraft.townbot.listeners.minecraft.player_activity.active.ActivityManager;
import net.thetowncraft.townbot.util.SkinRender;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import java.util.List;

public class Bal extends DiscordCommand {

    @Override
    public void execute(CommandEvent.Discord event) {

        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
        Member member;
        boolean askedForSelfBal = false;

        if(mentionedMembers.size() != 0) {
            member = mentionedMembers.get(0);
        }
        else {
            member = event.getMember();
            askedForSelfBal = true;
        }

        OfflinePlayer player = AccountManager.getInstance().getMinecraftPlayer(member);
        if(player == null) {
            if(askedForSelfBal) {
                event.getChannel().sendMessage(":x: Your account must be linked for you to earn coins!").queue();
            }
            else {
                event.getChannel().sendMessage(":x: " + member.getEffectiveName() + "'s account must be linked for them earn coins!").queue();
            }
            return;
        }

        int bal = EconomyManager.getCoinBalance(player.getUniqueId().toString());

        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor(player.getName() + "'s Account", null, SkinRender.renderFace(player));
        embed.addField(":coin: Coins", String.valueOf(bal), true);
        embed.addField(":sparkles: Activity Points", String.valueOf(ActivityManager.getActivityPoints(player)), true);
        embed.setColor(member.getColor());
        event.getChannel().sendMessage(embed.build()).queue();
    }

    @Override
    public String getName() {
        return "bal";
    }

    @Override
    public String getDescription() {
        return "View how many coins you have!";
    }

    @Override
    public Permission getRequiredPermission() {
        return null;
    }

    public static class MC extends MinecraftCommand {

        @Override
        public void execute(CommandEvent.Minecraft event) {
            String[] args = event.getArgs();
            boolean askedForSelfBal = args.length == 0;

            OfflinePlayer player;

            if(askedForSelfBal) {
                player = event.getPlayer();
            }
            else {
                player = Bukkit.getOfflinePlayer(args[0]);
                if(!AccountManager.getInstance().isLinked(player)) {
                    event.getPlayer().sendMessage(ChatColor.RED + args[0] + "'s account is either not linked, or the player does not exist");
                    return;
                }
            }
            event.getPlayer().sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + player.getName() + "'s Account" +
                    "\n--------" +
                    ChatColor.RESET + "\n" + ChatColor.YELLOW + "Coins: " + EconomyManager.getCoinBalance(player) +
                    ChatColor.RESET + "" + ChatColor.AQUA + "\nActivity Points: " + ActivityManager.getActivityPoints(player)
            );
        }

        @Override
        public boolean isAdminCommand() {
            return false;
        }

        @Override
        public String getName() {
            return "bal";
        }
    }
}
