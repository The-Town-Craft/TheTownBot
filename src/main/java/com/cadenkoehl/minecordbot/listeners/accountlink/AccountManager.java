package com.cadenkoehl.minecordbot.listeners.accountlink;

import com.cadenkoehl.minecordbot.Constants;
import com.cadenkoehl.minecordbot.MinecordBot;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class AccountManager {

    private AccountManager() {}

    public boolean isLinked(OfflinePlayer player) {
        System.out.println("Checking if " + player.getName() + "'s account is linked...");
        MinecordBot plugin = MinecordBot.getPlugin(MinecordBot.class);
        String uuid = player.getUniqueId().toString();
        File dir = new File(plugin.getDataFolder() + "/account");
        if(dir.mkdirs()) {
            System.out.println("Successfully created the account directory!");
        }
        File file = new File(plugin.getDataFolder() + "/account/" + uuid + ".txt");
        if(!file.exists()) {
            System.out.println("File does not exist, meaning " + player.getName() + "'s account is not linked");
            return false;
        }
        String userId;
        try {
            Scanner scan = new Scanner(file);
            userId = scan.nextLine();
            System.out.println("Successfully scanned user id " + userId);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        Member member = MinecordBot.jda.getGuildById(Constants.TOWN_DISCORD).getMemberById(userId);
        if(member == null) {
            System.out.println("Member is null, meaning it is either not cached or the member does not exist.");
            return false;
        }
        try {
            member.modifyNickname(player.getName()).queue();
            System.out.println("Successfully modified " + player.getName() + "'s nickname!");
        }
        catch (HierarchyException ex) {
            System.out.println("I was unable to modify " + member.getEffectiveName() + "'s nickname because I lack permission!");
        }
        member.getGuild().removeRoleFromMember(member, Constants.UNLINKED_ROLE).queue();
        member.getGuild().addRoleToMember(member, Constants.TOWN_MEMBER_ROLE).queue();

        if(member.getRoles().contains(Constants.MAYOR_ROLE)) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tab player " + player.getName() + " tabsuffix &5 [mayor]");
            return true;
        }

        if(member.getRoles().contains(Constants.VICE_MAYOR_ROLE)) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tab player " + player.getName() + " tabsuffix &6 [vice mayor]");
            return true;
        }

        if(member.getRoles().contains(Constants.LAWYER_ROLE)) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tab player " + player.getName() + " tabsuffix &e [lawyer]");
            return true;
        }

        if(member.getRoles().contains(Constants.PUBLIC_WORKS_ROLE)) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tab player " + player.getName() + " tabsuffix &b [public works manager]");
            return true;
        }

        if(member.getRoles().contains(Constants.TOWN_MEMBER_ROLE)) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tab player " + player.getName() + " tabsuffix &2 [town member]");
            return true;
        }

        return true;
    }
    public String generatePassword(OfflinePlayer player) {
        MinecordBot plugin = MinecordBot.getPlugin(MinecordBot.class);
        int password = (int) Math.round(Math.random() * 100000);
        File dir = new File(plugin.getDataFolder().getPath() + "/account/password");
        if(dir.mkdirs()) {
            System.out.println("Successfully created the account/password directory!");
        }
        File file = new File(plugin.getDataFolder().getPath() + "/account/password/" + password + ".txt");
        try {
            FileWriter write = new FileWriter(file);
            write.write(player.getUniqueId().toString());
            write.close();
            System.out.println("Successfully wrote password to file!");
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("All of the checks passed, returning a password string for the generatePassword() method!");
        return String.valueOf(password);
    }
    public Member getDiscordMember(OfflinePlayer player) {
        MinecordBot plugin = MinecordBot.getPlugin(MinecordBot.class);
        if(!isLinked(player)) {
            System.out.println(player.getName() + "'s account is not linked! Returning null!");
            return null;
        }
        String uuid = player.getUniqueId().toString();
        File file = new File(plugin.getDataFolder().getPath() + "/account/" + uuid + ".txt");
        String userId;
        try {
            Scanner scan = new Scanner(file);
            userId = scan.nextLine();
        } catch (FileNotFoundException e) {
            System.err.println("Caught a FileNotFoundException: Returning null for getDiscordMember()");
            return null;
        }
        return MinecordBot.jda.getGuildById(Constants.TOWN_DISCORD).getMemberById(userId);
    }

    public static AccountManager getInstance() {
        return new AccountManager();
    }
}