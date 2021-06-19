package net.thetowncraft.townbot.util;

import net.thetowncraft.townbot.Plugin;

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

    public static String capFirstLetter(String string) {
        return string.substring(0,1).toUpperCase() + string.substring(1).toLowerCase();
    }
}