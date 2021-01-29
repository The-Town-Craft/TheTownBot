package com.cadenkoehl.minecordbot.accountlink;

import com.cadenkoehl.minecordbot.Constants;
import com.cadenkoehl.minecordbot.MinecordBot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.OfflinePlayer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class AccountManager {
    public boolean isLinked(OfflinePlayer player) {
        MinecordBot plugin = MinecordBot.getPlugin(MinecordBot.class);
        String uuid = player.getUniqueId().toString();
        File dir = new File(plugin.getDataFolder() + "/account");
        if(dir.mkdirs()) {
            System.out.println("Account link directory was created!");
        }
        File file = new File(plugin.getDataFolder() + "/account/" + uuid + ".txt");
        if(!file.exists()) {
            System.out.println("File does not exist");
            return false;
        }
        String userId = null;
        try {
            Scanner scan = new Scanner(file);
            userId = scan.nextLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Guild guild = MinecordBot.jda.getGuildById(Constants.theTown);
        if(guild == null) {
            throw new NullPointerException("Cannot perform action on guild because it is null!");
        }
        if(userId == null) {
            System.out.println("User ID is null!");
            return false;
        }
        User user = MinecordBot.jda.getUserById(userId);
        AtomicBoolean memberIsNull = new AtomicBoolean(false);
        guild.retrieveMember(user).queue(member -> {
            if(member == null) {
                memberIsNull.set(true);
            }
        });
        if(memberIsNull.get()) {
            System.out.println("Member is null");
            return false;
        }
        return true;
    }
    public String generatePassword(OfflinePlayer player) {
        MinecordBot plugin = MinecordBot.getPlugin(MinecordBot.class);
        String password;
        int round = (int) Math.round(Math.random() * 10000);
        password = String.valueOf(round);
        String uuid = player.getUniqueId().toString();
        File dir = new File(plugin.getDataFolder() + "/account/password");
        if(dir.mkdirs()) {
            System.out.println("Account password directory was created!");
        }
        File file = new File(plugin.getDataFolder() + "/account/password/" + password + ".txt");
        try {
            FileWriter write = new FileWriter(file);
            write.write(uuid);
            write.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return password;
    }
}

























