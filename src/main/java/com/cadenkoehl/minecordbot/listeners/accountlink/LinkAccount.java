package com.cadenkoehl.minecordbot.listeners.accountlink;

import com.cadenkoehl.minecordbot.Bot;
import com.cadenkoehl.minecordbot.Plugin;
import com.cadenkoehl.minecordbot.listeners.util.Constants;
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

public class LinkAccount extends ListenerAdapter {
    @Override
    public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {
        Plugin plugin = Plugin.get();
        String message = event.getMessage().getContentRaw();
        File passwordDir = new File(plugin.getDataFolder().getPath() + "/account/password");
        if(passwordDir.mkdirs()) {
            System.out.println("Successfully created the account/password directory!");
        }
        File passwordFile = new File(plugin.getDataFolder().getPath() + "/account/password/" + message + ".txt");
        if(!passwordFile.exists()) {
            System.out.println("File does not exist.");
            return;
        }
        String uuidString;
        try {
            Scanner scan = new Scanner(passwordFile);
            uuidString = scan.nextLine();
            System.out.println("Successfully scanned uuid " + uuidString);
        }
        catch (IOException ex) {
            event.getJDA().getTextChannelById(Constants.MODMAIL).sendMessage("<@585334397914316820> A fatal error has occurred! `" + ex + "`\n```java\n" + Arrays.toString(ex.getStackTrace()) + "\n```");
            ex.printStackTrace();
            return;
        }
        UUID uuid = UUID.fromString(uuidString);
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        File dir = new File(plugin.getDataFolder().getPath() + "/account");
        if(dir.mkdirs()) {
            System.out.println("Successfully created the account directory!");
        }
        File file = new File(plugin.getDataFolder().getPath() + "/account/" + uuidString + ".txt");
        if(!file.exists()) {
            try {
                FileWriter write = new FileWriter(file);
                write.write(event.getAuthor().getId());
                write.close();
                if(passwordFile.delete()) {
                    System.out.println("Password file was successfully deleted!");
                }
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
}
