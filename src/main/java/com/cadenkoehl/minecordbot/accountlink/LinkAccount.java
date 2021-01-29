package com.cadenkoehl.minecordbot.accountlink;

import com.cadenkoehl.minecordbot.Constants;
import com.cadenkoehl.minecordbot.MinecordBot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class LinkAccount extends ListenerAdapter {
    @Override
    public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {
        MinecordBot plugin = MinecordBot.getPlugin(MinecordBot.class);
        String password = event.getMessage().getContentRaw();
        File passwordFile = new File(plugin.getDataFolder() + "/account/password/" + password + ".txt");
        if(!passwordFile.exists()) {
            return;
        }
        String uuidString = null;
        try {
            Scanner scan = new Scanner(passwordFile);
            uuidString = scan.nextLine();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        UUID uuid = UUID.fromString(uuidString);
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        String name = player.getName();
        try {
            File dir = new File(plugin.getDataFolder() + "/account");
            if(dir.mkdirs()) {
                System.out.println("Account directory was successfully created!");
            }
            File accountFile = new File(plugin.getDataFolder() + "/account/" + uuidString + ".txt");
            FileWriter write = new FileWriter(accountFile);
            write.write(event.getAuthor().getId());
            write.close();
        }
        catch (IOException e) {
            event.getChannel().sendMessage(":x: Something went wrong! Please contact a staff member immediately! (you can dm this bot, your message gets sent straight to the staff)").queue();
            e.printStackTrace();
        }
        try {
            event.getJDA().getGuildById(Constants.theTown).retrieveMember(event.getAuthor()).queue(member -> {
                member.modifyNickname(name).queue();
            });
        }
        catch (HierarchyException ex) {
            event.getChannel().sendMessage(":x: I was unable to modify your nickname due to lack of permission, please report this to a staff member!").queue();
        }
        event.getChannel().sendMessage(":white_check_mark: **Success**! Your Discord account was linked to the Minecraft account **" + name + "**!").queueAfter(1, TimeUnit.SECONDS);
    }
}