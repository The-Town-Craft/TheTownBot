package net.thetowncraft.townbot.listeners.accountlink;

import net.dv8tion.jda.api.entities.Member;
import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
import net.thetowncraft.townbot.util.Constants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        AccountManager manager = AccountManager.getInstance();
        Player player = event.getPlayer();
        if(manager.isLinked(player)) {

            Member member = manager.getDiscordMember(player);
            manager.syncAccountData(player, member);

            if(player.getWorld().getName().equals(AccountManager.UNLINKED_DIMENSION.getWorld().getName())) {
                LinkAccount.onLink(player, member.getUser());
            }

            player.addAttachment(Plugin.get(), "tradeshop.help", true);
            player.addAttachment(Plugin.get(), "tradeshop.info", true);
            player.addAttachment(Plugin.get(), "tradeshop.edit", true);
            player.addAttachment(Plugin.get(), "tradeshop.create", true);
            player.addAttachment(Plugin.get(), "tradeshop.createbi", true);
            player.addAttachment(Plugin.get(), "tradeshop.createi", true);
            return;
        }

        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tab player " + player.getName() + " tabsuffix &8 [unlinked]");
        player.setGameMode(GameMode.SPECTATOR);
        player.setInvulnerable(true);
        player.teleport(AccountManager.UNLINKED_DIMENSION);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.get(), () -> {
            Bukkit.getServer().broadcastMessage(ChatColor.RED + player.getName() + "'s account is not linked!");
            Bot.jda.getTextChannelById(Constants.MC_CHAT).sendMessage(":x: **" + player.getName() + "**'s account is not linked!").queue();
        }, 10);
    }
}