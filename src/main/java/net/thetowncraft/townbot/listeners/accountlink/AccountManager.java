package net.thetowncraft.townbot.listeners.accountlink;

import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.util.Constants;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Manages Discord & Minecraft Account linking!
 */
public class AccountManager {

    private AccountManager() {}

    /**
     * @param member A member
     * @return Their rank on Discord, (for example public works manager, vice mayor, etc.)
     */
    public Role getDiscordRank(Member member) {
        if(member.getRoles().contains(Constants.MAYOR_ROLE)) return Constants.MAYOR_ROLE;

        if(member.getRoles().contains(Constants.VICE_MAYOR_ROLE)) return Constants.VICE_MAYOR_ROLE;

        if(member.getRoles().contains(Constants.LAWYER_ROLE)) return Constants.LAWYER_ROLE;

        if(member.getRoles().contains(Constants.PUBLIC_WORKS_ROLE)) return Constants.PUBLIC_WORKS_ROLE;

        return Constants.TOWN_MEMBER_ROLE;
    }

    /**
     * @param member A member
     * @return Takes the member's Discord rank and translates it's color to a Minecraft chat color.
     */
    public ChatColor getMinecraftChatColor(Member member) {
        Role discordRank = getDiscordRank(member);

        if(discordRank == Constants.MAYOR_ROLE) return ChatColor.DARK_PURPLE;
        if(discordRank == Constants.VICE_MAYOR_ROLE) return ChatColor.GOLD;
        if(discordRank == Constants.LAWYER_ROLE) return ChatColor.YELLOW;
        if(discordRank == Constants.PUBLIC_WORKS_ROLE) return ChatColor.AQUA;
        return ChatColor.GREEN;

    }

    /**
     * @param player A player
     * @return True if the player's account is linked, false it it is not
     */
    public boolean isLinked(OfflinePlayer player) {
        System.out.println("Checking if " + player.getName() + "'s account is linked...");
        Plugin plugin = Plugin.get();
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
        Member member = Bot.jda.getGuildById(Constants.TOWN_DISCORD_ID).getMemberById(userId);
        if(member == null) {
            System.out.println("Member is null, meaning it is either not cached or the member does not exist.");
            return false;
        }

        linkPlayer(player, member);
        return true;
    }

    public void linkPlayer(OfflinePlayer player, Member member) {
        try {
            member.modifyNickname(player.getName()).queue();
            System.out.println("Successfully modified " + player.getName() + "'s nickname!");
        }
        catch (HierarchyException ex) {
            System.out.println("I was unable to modify " + member.getEffectiveName() + "'s nickname because I lack permission!");
        }

        member.getGuild().removeRoleFromMember(member, Constants.UNLINKED_ROLE).complete();
        member.getGuild().addRoleToMember(member, Constants.TOWN_MEMBER_ROLE).complete();

        char color = getMinecraftChatColor(member).getChar();

        //Assigns the player their correct tab ranks
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),  "tab player " + player.getName() + " tagprefix &" + color);

        if(member.getRoles().contains(Constants.MAYOR_ROLE)) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tab player " + player.getName() + " tabsuffix &5 [mayor]");
            return;
        }

        if(member.getRoles().contains(Constants.VICE_MAYOR_ROLE)) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tab player " + player.getName() + " tabsuffix &6 [vice mayor]");
            return;
        }

        if(member.getRoles().contains(Constants.LAWYER_ROLE)) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tab player " + player.getName() + " tabsuffix &e [lawyer]");
            return;
        }

        if(member.getRoles().contains(Constants.PUBLIC_WORKS_ROLE)) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tab player " + player.getName() + " tabsuffix &b [public works manager]");
            return;
        }

        if(member.getRoles().contains(Constants.TOWN_MEMBER_ROLE)) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tab player " + player.getName() + " tabsuffix &2 [town member]");
        }
    }
    public String generatePassword(OfflinePlayer player) {
        Plugin plugin = Plugin.get();
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
        Plugin plugin = Plugin.get();
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
        return Bot.jda.getGuildById(Constants.TOWN_DISCORD_ID).getMemberById(userId);
    }

    public static AccountManager getInstance() {
        return new AccountManager();
    }
}