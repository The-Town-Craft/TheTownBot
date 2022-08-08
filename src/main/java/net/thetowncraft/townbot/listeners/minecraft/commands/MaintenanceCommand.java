package net.thetowncraft.townbot.listeners.minecraft.commands;

import net.dv8tion.jda.api.entities.Member;
import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.minecraft.MinecraftCommand;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
import net.thetowncraft.townbot.listeners.minecraft.player_activity.PlayerCountStatus;
import net.thetowncraft.townbot.util.Constants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class MaintenanceCommand extends MinecraftCommand {

    public static final String kickMessage = "Server is currently under maintenance.";

    @Override
    public void execute(CommandEvent.Minecraft event) {
        Plugin.serverUnderMaintenance = !Plugin.serverUnderMaintenance;

        if(Plugin.serverUnderMaintenance) {
            event.getPlayer().sendMessage(ChatColor.GREEN + "Server is now under maintenance. Only members with the developer role will be able to join.");
            kickNonDevs();
        }
        else {
            event.getPlayer().sendMessage(ChatColor.GREEN + "Server has been re-opened. Members without the developer role will once again be able to join.");
        }
        PlayerCountStatus.update();
    }

    public static void kickNonDevs() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            Member member = AccountManager.getInstance().getDiscordMember(player);
            if(member == null || !member.getRoles().contains(Constants.DEV_ROLE)) {
                player.kickPlayer(kickMessage);
            }
        }
    }

    public static void loadData() {
        File file = new File(Plugin.get().getDataFolder(), "maintenance.txt");
        Plugin.serverUnderMaintenance = file.exists();

        System.out.println("serverUnderMaintenance is now " + Plugin.serverUnderMaintenance);
    }

    public static void saveData() {
        File file = new File(Plugin.get().getDataFolder(), "maintenance.txt");

        if(Plugin.serverUnderMaintenance) {
            if(!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            if(file.exists()) {
                file.delete();
            }
        }
    }

    @Override
    public boolean isAdminCommand() {
        return true;
    }

    @Override
    public String getName() {
        return "maintenance";
    }
}
