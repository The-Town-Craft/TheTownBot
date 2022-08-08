package net.thetowncraft.townbot.listeners.accountlink;

import jdk.internal.foreign.abi.Binding;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.util.Constants;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

/**
 * Manages Discord & Minecraft Account linking!
 */
public class AccountManager {

    public static final Location UNLINKED_DIMENSION = new Location(Bukkit.getWorld(Plugin.OVERWORLD_NAME + "_thetown_unlinked"), 0, 120, 0);

    private AccountManager() {}

    private static final Map<String, String> DISCORD_ACCOUNTS = new HashMap<>();
    private static final Map<String, String> MINECRAFT_ACCOUNTS = new HashMap<>();
    private static final Map<String, String> PASSWORDS = new HashMap<>();

    public static Map<String, String> getDiscordAccounts() {
        return DISCORD_ACCOUNTS;
    }

    public static Map<String, String> getMinecraftAccounts() {
        return MINECRAFT_ACCOUNTS;
    }

    public static Map<String, String> getPasswords() {
        return PASSWORDS;
    }

    public static String getPassword(OfflinePlayer player) {
        for(String password : getPasswords().keySet()) {
            String uuidString = getPasswords().get(password);
            if(uuidString == null) continue;

            if(player.getUniqueId().toString().equals(uuidString)) {
                return password;
            }
        }
        return getInstance().generatePassword(player);
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

        if(member.getRoles().contains(Constants.DONATOR_ROLE)) return Constants.DONATOR_ROLE;

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
        if(discordRank == Constants.DONATOR_ROLE) return ChatColor.RED;
        if(discordRank == Constants.PUBLIC_WORKS_ROLE) return ChatColor.DARK_AQUA;
        if(discordRank == Constants.ACTIVE_PLAYER_ROLE) return ChatColor.AQUA;
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
        return true;
    }

    public void syncAccountData(OfflinePlayer player, Member member) {

        DISCORD_ACCOUNTS.put(member.getId(), player.getUniqueId().toString());
        MINECRAFT_ACCOUNTS.put(player.getUniqueId().toString(), member.getId());

        try {
            member.modifyNickname(player.getName()).queue();
        }
        catch (HierarchyException ex) {
            System.out.println("I was unable to modify " + member.getEffectiveName() + "'s nickname because I lack permission!");
        }

        member.getGuild().removeRoleFromMember(member, Constants.UNLINKED_ROLE).complete();
        member.getGuild().removeRoleFromMember(member, Constants.INACTIVE_PLAYER_ROLE).queue();
        member.getGuild().removeRoleFromMember(member, Constants.BUSY_ROLE).queue();
        member.getGuild().removeRoleFromMember(member, Constants.MEMBER_ROLE).queue();

        member.getGuild().addRoleToMember(member, Constants.TOWN_MEMBER_ROLE).complete();

        char color = getMinecraftChatColor(member).getChar();

        //Assigns the player their correct tab ranks

        Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.get(), () -> {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),  "tab player " + player.getName() + " tagprefix &" + color);

            if(member.getRoles().contains(Constants.UNLINKED_ROLE)) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tab player " + player.getName() + " tabsuffix &8 [unlinked]");
                return;
            }

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

            if(member.getRoles().contains(Constants.DONATOR_ROLE)) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tab player " + player.getName() + " tabsuffix &4 [donator]");
                return;
            }

            if(member.getRoles().contains(Constants.ACTIVE_PLAYER_ROLE)) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tab player " + player.getName() + " tabsuffix &b [active player]");
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

    public List<EmbedBuilder> getAccountsEmbeds() {
        List<EmbedBuilder> embeds = new ArrayList<>();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor("Linked Accounts", null, Constants.THE_TOWN.getIconUrl());
        embed.setColor(Constants.GREEN);
        int i = 1;
        for(Map.Entry<String, String> entry : DISCORD_ACCOUNTS.entrySet()) {
            Member member = Constants.THE_TOWN.getMemberById(entry.getKey());
            if(member == null) continue;

            OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(entry.getValue()));
            embed.appendDescription("\n" + player.getName() + " <----> " + member.getAsMention());
            i++;
            if(i > 20) {
                embeds.add(embed);
                embed = new EmbedBuilder();
                embed.setColor(Constants.GREEN);
                i = 1;
            }
        }
        embeds.add(embed);
        return embeds;
    }

    public void unlink(OfflinePlayer player) {
        Member member = getDiscordMember(player);
        if(member == null) return;

        DISCORD_ACCOUNTS.remove(member.getId());
        MINECRAFT_ACCOUNTS.remove(player.getUniqueId().toString());
    }

    public void unlink(Member member) {
        OfflinePlayer player = getMinecraftPlayer(member);
        if(player == null) return;

        DISCORD_ACCOUNTS.remove(member.getId());
        MINECRAFT_ACCOUNTS.remove(player.getUniqueId().toString());
    }

    public String generatePassword(OfflinePlayer player) {
        int password = (int) Math.round(Math.random() * 100000);
        PASSWORDS.put(String.valueOf(password), player.getUniqueId().toString());
        return String.valueOf(password);
    }
    public Member getDiscordMember(OfflinePlayer player) {
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