package net.thetowncraft.townbot.listeners.minecraft.commands;

import net.dv8tion.jda.api.entities.Member;
import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
import net.thetowncraft.townbot.util.Constants;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class MaintenanceListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(Plugin.serverUnderMaintenance) {
            Player player = event.getPlayer();
            Member discord = AccountManager.getInstance().getDiscordMember(player);
            if(!discord.getRoles().contains(Constants.DEV_ROLE)) {
                player.kickPlayer(MaintenanceCommand.kickMessage);
                discord.getUser().openPrivateChannel().queue(channel ->
                        channel.sendMessage("The Town is currently under server maintenance. If you have any questions, please ask in this DM!").queue());
            }
        }
    }
}
