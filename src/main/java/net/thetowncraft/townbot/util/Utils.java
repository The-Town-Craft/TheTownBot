package net.thetowncraft.townbot.util;

import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.listeners.patches.Vanish;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

import java.io.File;
import java.text.DateFormatSymbols;
import java.util.*;

public class Utils {

    /**
     * This method sorts a string/long HashMap by it's value
     * @param unsortedMap An unsorted map
     * @return The sorted map
     */
    public static HashMap<String, Long> sortByLongValue(Map<String, Long> unsortedMap) {
        List<Map.Entry<String, Long>> list =
                new LinkedList<>(unsortedMap.entrySet());

        list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        HashMap<String, Long> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Long> entry : list) {
            if(entry.getValue() != 0) sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    public static HashMap<String, Integer> sortByIntValue(Map<String, Integer> unsortedMap) {
        List<Map.Entry<String, Integer>> list =
                new LinkedList<>(unsortedMap.entrySet());

        list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        HashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            if(entry.getValue() != 0) sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    /**
     * @return The name of the current weekday in english.
     */
    public static String getNameOfDay() {
        DateFormatSymbols dfs = new DateFormatSymbols(Locale.ENGLISH);
        String[] weekdays = dfs.getWeekdays();

        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);

        return weekdays[day];
    }

    /**
     * @return A simple & easy to read representation of the current date
     */
    public static String getSimpleDate() {
        String dateRaw = new Date().toString();
        String[] dateSplit = dateRaw.split("\\s+");
        return dateSplit[1] + "-" + dateSplit[2] + "-" + dateSplit[5];
    }

    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }

    public static String capFirstLetter(String string) {
        return string.substring(0,1).toUpperCase() + string.substring(1).toLowerCase();
    }

    //Gets all players that are not vanished
    public static List<Player> getEffectiveOnlinePlayers() {
        List<Player> effectivePlayers = new ArrayList<>();
        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        for(Player player : onlinePlayers) {
            if(!Vanish.isVanished(player)) effectivePlayers.add(player);
        }
        return effectivePlayers;
    }
}