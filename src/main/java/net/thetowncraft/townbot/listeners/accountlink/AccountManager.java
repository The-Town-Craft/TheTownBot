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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Manages Discord & Minecraft Account linking!
 */
public class AccountManager {

    private AccountManager() {}

    private static final Map<String, String> DISCORD_ACCOUNTS = new HashMap<>();
    private static final Map<String, String> MINECRAFT_ACCOUNTS = new HashMap<>();
    private static final Map<String, String> PASSWORDS = new HashMap<>();

    public static Map<String, String> getPasswords() {
        return PASSWORDS;
    }

    public static void loadAccounts() {
        File dir = new File(Plugin.get().getDataFolder(), "account");
        dir.mkdirs();

        for(String fileName : Objects.requireNonNull(dir.list())) {
            if(!fileName.contains(".txt")) continue;
            File file = new File(dir, fileName);
            fileName = fileName.replace(".txt", "");
            String discordID;
            try {
                Scanner scan = new Scanner(file);
                discordID = scan.nextLine();
            } catch (FileNotFoundException e) {
                continue;
            }
            DISCORD_ACCOUNTS.put(discordID, fileName);
            MINECRAFT_ACCOUNTS.put(fileName, discordID);
        }
        System.out.println("Successfully loaded " + DISCORD_ACCOUNTS.size() + " accounts");
    }

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
        String userId = MINECRAFT_ACCOUNTS.get(player.getUniqueId().toString());
        if(userId == null) return false;

        Member member = Bot.jda.getGuildById(Constants.TOWN_DISCORD_ID).getMemberById(userId);
        if(member == null) {
            return false;
        }

        syncAccountData(player, member);
        return true;
    }

    public void syncAccountData(OfflinePlayer player, Member member) {

        DISCORD_ACCOUNTS.put(member.getId(), player.getUniqueId().toString());

        try {
            member.modifyNickname(player.getName()).queue();
        }
        catch (HierarchyException ex) {
            System.out.println("I was unable to modify " + member.getEffectiveName() + "'s nickname because I lack permission!");
        }

        member.getGuild().removeRoleFromMember(member, Constants.UNLINKED_ROLE).complete();
        member.getGuild().addRoleToMember(member, Constants.TOWN_MEMBER_ROLE).complete();

        char color = getMinecraftChatColor(member).getChar();

        //Assigns the player their correct tab ranks

        Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.get(), () -> {
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
        });
    }
    public String generatePassword(OfflinePlayer player) {
        int password = (int) Math.round(Math.random() * 100000);
        PASSWORDS.put(String.valueOf(password), player.getUniqueId().toString());
        return String.valueOf(password);
    }
    public Member getDiscordMember(OfflinePlayer player) {
        if(!isLinked(player)) {
            return null;
        }

        String userId = MINECRAFT_ACCOUNTS.get(player.getUniqueId().toString());
        if (userId == null) return null;
        return Bot.jda.getGuildById(Constants.TOWN_DISCORD_ID).getMemberById(userId);
    }
    public OfflinePlayer getMinecraftPlayer(Member member) {
        String uuid = DISCORD_ACCOUNTS.get(member.getId());
        if(uuid == null) return null;
        return Bukkit.getOfflinePlayer(UUID.fromString(uuid));
    }

    public static AccountManager getInstance() {
        return new AccountManager();
    }
}