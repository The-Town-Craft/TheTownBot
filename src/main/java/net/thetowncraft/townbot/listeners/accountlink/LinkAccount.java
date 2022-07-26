package net.thetowncraft.townbot.listeners.accountlink;

import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.util.Constants;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
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
            AccountManager.getMinecraftAccounts().put(player.getUniqueId().toString(), event.getAuthor().getId());
            AccountManager.getInstance().syncAccountData(player, Constants.THE_TOWN.getMember(event.getAuthor()));
            event.getJDA().getTextChannelById(Constants.MODMAIL).sendMessage(":white_check_mark: The Discord account **" + event.getAuthor().getAsTag() + "** was successfully linked to the Minecraft account **" + player.getName() + "**!").queue();
            event.getChannel().sendMessage(":white_check_mark: **Success**! Your Discord account was linked to the Minecraft account **" + player.getName() + "**!").queue();
            return;
        } catch (IOException ex) {
            event.getJDA().getTextChannelById(Constants.MODMAIL).sendMessage("<@585334397914316820> A fatal error has occurred! `" + ex + "`\n```java\n" + Arrays.toString(ex.getStackTrace()) + "\n```");
            ex.printStackTrace();
        }
        event.getChannel().sendMessage(":x: Your account is already linked to the Minecraft account **" + player.getName() + "**!").queue();
    }
}
