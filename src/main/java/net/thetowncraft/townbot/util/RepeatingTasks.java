package net.thetowncraft.townbot.util;

import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.economy.EconomyManager;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
import net.thetowncraft.townbot.listeners.minecraft.player_activity.active.ActivityManager;
import net.thetowncraft.townbot.listeners.minecraft.player_activity.afk.AFKManager;
import net.thetowncraft.townbot.util.Constants;
import net.thetowncraft.townbot.util.Utils;
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class RepeatingTasks {

    public static final long REPEATING_TICKS = 500;
    public static final long TICKS_IN_A_DAY = 1728000;

    public static void doDailyTasks() {
        String day = Utils.getNameOfDay();

        if(day.equalsIgnoreCase("Monday")) {
            rewardActivePlayers();
        }
    }

    public static void updatePlayerActivity() {
        countAFKTicks();
        countActivityPoints();
        saveActivityPoints();
    }

    /**
     * Counts and updates the player activity points for every player on the server
     */
    private static void countActivityPoints() {
        for(Player offlinePlayer : Bukkit.getOnlinePlayers()) {

            Player player = offlinePlayer.getPlayer();
            if(player != null)  {
                if(AFKManager.isAFK(player)) return;
            }

            Long points = ActivityManager.PLAYER_ACTIVITY_MAP.get(offlinePlayer.getUniqueId().toString());

            if(points == null) points = 0L;

            points++;

            ActivityManager.PLAYER_ACTIVITY_MAP.put(offlinePlayer.getUniqueId().toString(), points);
        }
    }

    private static void countAFKTicks() {

        for(Player player : Bukkit.getOnlinePlayers()) {

            int x = (int) player.getVelocity().getX();
            int y = (int) player.getVelocity().getY();
            int z = (int) player.getVelocity().getZ();

            if (x != 0 || y != 0 || z != 0) {
                AFKManager.AFK_PLAYER_TICKS.put(player, 0L);
                return;
            }

            Long afkTicks = AFKManager.AFK_PLAYER_TICKS.get(player);
            if(afkTicks == null) afkTicks = 0L;
            afkTicks++;

            AFKManager.AFK_PLAYER_TICKS.put(player, afkTicks);

            if(afkTicks > AFKManager.TIME_UNTIL_AFK) {
                AFKManager.setAFK(player, true);
            }
        }
    }

    /**
     * Saves the activity HashMap to files.
     */
    private static void saveActivityPoints() {
        try {
            File dataFolder = Plugin.get().getDataFolder();

            File dir = new File(dataFolder, "activity/");
            if(dir.mkdirs()) System.out.println(dir.getPath() + " was successfully created!");

            for(Map.Entry<String, Long> entry : ActivityManager.PLAYER_ACTIVITY_MAP.entrySet()) {
                String uuid = entry.getKey();
                File file = new File(dir,uuid + ".txt");

                FileWriter write = new FileWriter(file);
                write.write(String.valueOf(entry.getValue()));
                write.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void rewardActivePlayers() {

        for(Member member : Constants.THE_TOWN.getMembersWithRoles(Constants.MOST_ACTIVE_ROLE)) {
            Constants.THE_TOWN.removeRoleFromMember(member, Constants.MOST_ACTIVE_ROLE).queue();
        }

        Bot.jda.getTextChannelById(Constants.ANNOUNCEMENTS).sendMessage("**It's time to announce the most active players of the week!**").queue();

        Map<String, Long> mostActivePlayers = ActivityManager.get3MostActivePlayers();
        for(Map.Entry<String, Long> entry : mostActivePlayers.entrySet()) {
            Member discordMember = AccountManager.getInstance().getDiscordMember(Bukkit.getOfflinePlayer(UUID.fromString(entry.getKey())));
            discordMember.getGuild().addRoleToMember(discordMember, Constants.MOST_ACTIVE_ROLE).queue();
            Bot.jda.getTextChannelById(Constants.ANNOUNCEMENTS)
                    .sendMessage(discordMember.getAsMention() + " has been awarded the **" + Constants.MOST_ACTIVE_ROLE.getName() + "** role! :partying_face:").queue();
        }

        for(Map.Entry<String, Long> entry : ActivityManager.PLAYER_ACTIVITY_MAP.entrySet()) {
            Long bal = entry.getValue();
            if(bal == null) continue;
            if(bal >= 500) {
                OfflinePlayer player  = Bukkit.getOfflinePlayer(UUID.fromString(entry.getKey()));
                if(player.getName() == null) continue;

                Member member = AccountManager.getInstance().getDiscordMember(player);
                if(member == null) continue;

                int coins = bal.intValue() / 2;

                EconomyManager.addCoins(entry.getKey(), coins);

                try {
                    member.getUser().openPrivateChannel().queue((channel) -> channel.sendMessage("Thanks for being active this week! You have been awarded " + coins + " coins!").queue());
                }
                catch(Exception ex) {
                    //empty catch block
                }
            }
        }
        resetActivity();
    }

    public static void resetActivity() {
        File dataFolder = Plugin.get().getDataFolder();

        File dir = new File(dataFolder, "activity/");
        if(dir.mkdirs()) System.out.println(dir.getPath() + " was successfully created!");

        for(String fileName : dir.list()) {
            File file = new File(dir, fileName);
            if (file.delete()) System.out.println("Deleted file " + file.getPath());
            ActivityManager.PLAYER_ACTIVITY_MAP.clear();
        }
    }
}