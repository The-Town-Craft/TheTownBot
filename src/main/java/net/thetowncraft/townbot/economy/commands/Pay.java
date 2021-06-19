package net.thetowncraft.townbot.economy.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.api.command_handler.minecraft.MinecraftCommand;
import net.thetowncraft.townbot.economy.EconomyManager;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

public class Pay {

    /**
     * @param giver Whoever is paying coins
     * @param receiver Whoever is receiving the payment
     * @return The conformation message to the giver
     */
    private static String pay(OfflinePlayer giver, OfflinePlayer receiver, int amount, boolean mc) {

        if(giver.getUniqueId().toString().equals(receiver.getUniqueId().toString())) return mc ? ChatColor.RED + "You can't pay yourself!" : ":x: You can't pay yourself!";

        if(!AccountManager.getInstance().isLinked(receiver)) {
            return mc ? ChatColor.RED + receiver.getName() + "'s account is either not linked, or the player does not exist." : ":x:" + receiver.getName() + "'s account is either not linked, or the player does not exist.";
        }

        if(amount < 1) {
            return mc ? ChatColor.RED + "The number must be above 0" : ":x: The number must be above 0";
        }

        int currentBal = EconomyManager.getCoinBalance(giver);

        if(currentBal < 1) {
            return mc ? ChatColor.RED + "You don't have any coins in your account to transfer!" : ":x: You don't have any coins in your account to transfer!";
        }

        if(amount > currentBal) {
            return mc ? ChatColor.RED + "You cannot pay " + receiver.getName() + " " + amount + " coins because you only have " + currentBal : ":x: You cannot pay " + receiver.getName() + " " + amount + " coins because you only have " + currentBal;
        }

        EconomyManager.subtractCoins(giver.getUniqueId().toString(), amount);
        EconomyManager.addCoins(receiver.getUniqueId().toString(), amount);

        if(receiver.isOnline()) {
            ((Player) receiver).sendMessage(ChatColor.GREEN + giver.getName() + " just payed you " + amount + " coins!");
        }
        else {
            Member member = AccountManager.getInstance().getDiscordMember(receiver);
            if(member == null) return "A FATAL ERROR HAS OCCURRED";

            try {
                member.getUser().openPrivateChannel().queue((channel) -> {
                    channel.sendMessage("**" + giver.getName() + "** just payed you :coin: **" + amount + "** coins!").queue();
                });
            }
            catch(Exception ex) {
                //empty catch block
            }
        }

        return mc ? ChatColor.GREEN + "Successfully paid " + receiver.getName() + " " + amount + " coins!" : ":white_check_mark: Successfully paid **" + receiver.getName() + "** " + amount + " coins! :coin:";
    }

    public static class Minecraft extends MinecraftCommand {

        @Override
        public void execute(CommandEvent.Minecraft event) {

            String[] args = event.getArgs();
            Player player = event.getPlayer();
            String usage = Objects.requireNonNull(Plugin.get().getCommand("pay")).getUsage();

            if(args.length != 2) {
                player.sendMessage(ChatColor.RED + "Incomplete command! Usage: " + usage);
                return;
            }

            OfflinePlayer receiver = Bukkit.getOfflinePlayer(args[0]);
            if(!AccountManager.getInstance().isLinked(receiver)) {
                player.sendMessage(ChatColor.RED + args[0] + "'s account is either not linked, or does not exist");
                return;
            }

            int amount;

            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "Please provide a valid, positive integer for argument 2");
                return;
            }

            player.sendMessage(pay(player, receiver, amount, true));
        }

        @Override
        public boolean isAdminCommand() {
            return false;
        }

        @Override
        public String getName() {
            return "pay";
        }
    }

    public static class Discord extends DiscordCommand {

        @Override
        public void execute(CommandEvent.Discord event) {

            String[] args = event.getArgs();
            List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
            if(args.length != 3 || mentionedMembers.size() == 0) {
                event.getChannel().sendMessage(":x: Incomplete command! Correct usage: `/pay` `<@member>` `amount`").queue();
                return;
            }

            Member member = event.getMember();
            OfflinePlayer player = AccountManager.getInstance().getMinecraftPlayer(member);
            if(player == null) {
                event.getChannel().sendMessage(":x: Your account is either not linked, or it does not exist").queue();
                return;
            }

            Member discordReceiver = mentionedMembers.get(0);
            OfflinePlayer receiver = AccountManager.getInstance().getMinecraftPlayer(discordReceiver);
            if(receiver == null) {
                event.getChannel().sendMessage(":x: " + discordReceiver.getEffectiveName() + "'s account is either not linked, or it does not exist.").queue();
                return;
            }

            int amount;

            try {
                amount = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                event.getChannel().sendMessage(":x: Please provide a valid, positive integer for argument 3").queue();
                return;
            }

            event.getChannel().sendMessage(pay(player, receiver, amount, false)).queue();
        }

        @Override
        public String getName() {
            return "pay";
        }

        @Override
        public String getDescription() {
            return "Pay a player a certain amount of coins!";
        }

        @Override
        public Permission getRequiredPermission() {
            return null;
        }
    }
}
