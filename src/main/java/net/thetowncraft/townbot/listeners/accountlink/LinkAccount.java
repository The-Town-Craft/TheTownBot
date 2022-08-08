package net.thetowncraft.townbot.listeners.accountlink;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.modmail.ModMail;
import net.thetowncraft.townbot.util.Constants;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.thetowncraft.townbot.util.Utils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.UUID;

/**
 * Listens for members putting in their password
 */
public class LinkAccount extends ListenerAdapter {
    @Override
    public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {
        Plugin plugin = Plugin.get();
        String message = event.getMessage().getContentRaw();
        String uuidString = AccountManager.getPasswords().get(message);
        if(uuidString == null) {
            System.out.println(message);
            return;
        }

        UUID uuid = UUID.fromString(uuidString);
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        File dir = new File(plugin.getDataFolder().getPath() + "/account");
        if(dir.mkdirs()) {
            System.out.println("Successfully created the account directory!");
        }
        File file = new File(plugin.getDataFolder().getPath() + "/account/" + uuidString + ".txt");
        try {
            FileWriter write = new FileWriter(file);
            write.write(event.getAuthor().getId());
            write.close();
            AccountManager.getPasswords().remove(message);
            AccountManager.getMinecraftAccounts().put(offlinePlayer.getUniqueId().toString(), event.getAuthor().getId());
            AccountManager.getInstance().syncAccountData(offlinePlayer, Constants.THE_TOWN.getMember(event.getAuthor()));
            String confirmationMessage =
                    ":white_check_mark: The Discord account **" + event.getAuthor().getAsTag() + "** was successfully linked to the Minecraft account **" + offlinePlayer.getName() + "**!";
            event.getJDA().getTextChannelById(Constants.MC_CHAT).sendMessage(confirmationMessage).queue();
            ModMail.sendModMail(event.getAuthor(), Constants.THE_TOWN,
                    ":white_check_mark: **Success**! Your Discord account was linked to the Minecraft account **" + offlinePlayer.getName() + "**!", new ArrayList<>());

            Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.get(), () -> {
                Player player = offlinePlayer.getPlayer();
                if(player != null) onLink(player, event.getAuthor());
            }, 20);
            return;
        } catch (IOException ex) {
            event.getJDA().getTextChannelById(Constants.MODMAIL).sendMessage("<@585334397914316820> A fatal error has occurred! `" + ex + "`\n```java\n" + Arrays.toString(ex.getStackTrace()) + "\n```");
            ex.printStackTrace();
        }
        event.getChannel().sendMessage(":x: Your account is already linked to the Minecraft account **" + offlinePlayer.getName() + "**!").queue();
    }

    public static void onLink(Player player, User user) {
        player.setGameMode(GameMode.SURVIVAL);
        player.setInvulnerable(false);
        player.teleport(Plugin.SPAWN_LOCATION);
        player.sendMessage(ChatColor.GREEN + "Your Minecraft account was successfully linked to the Discord account " + user.getName() + "#" + user.getDiscriminator());
        Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.get(), () -> {
            ModMail.sendModMail(user, Constants.THE_TOWN, "Hey there! Thanks for joining us! If you wanna check out the new dimension, you can find all the details in <#934510539239985213>! Have fun! :smile:", new ArrayList<>());
        }, 2000);
    }

    public static void checkLink() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(AccountManager.getInstance().isLinked(player)) continue;
            player.sendMessage(ChatColor.GREEN + "Welcome, " + player.getName() + "! Here's how to play on the server:\n" + ChatColor.RESET +
                    "Step 1: Join our Discord (" + ChatColor.AQUA + "https://discord.gg/b7gVsDmpkk" + ChatColor.RESET + ")\n" +
                    "Step 2: DM TheTown bot with the code " + ChatColor.GREEN + AccountManager.getPassword(player) + ChatColor.RESET + "\n" +
                    "Step 3: Have fun! :)");
            Location dim = AccountManager.UNLINKED_DIMENSION;
            if(player.getWorld().equals(dim.getWorld())) {
                player.teleport(new Location(dim.getWorld(), dim.getX(), dim.getY(), dim.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
            }
        }
    }
}
