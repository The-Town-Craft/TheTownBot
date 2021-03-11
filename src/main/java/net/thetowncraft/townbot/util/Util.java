package net.thetowncraft.townbot.util;

import java.text.DateFormatSymbols;
import java.util.*;

public class Util {
    public static HashMap<String, Long> sortByValue(Map<String, Long> unsortedMap) {
        List<Map.Entry<String, Long>> list =
                new LinkedList<>(unsortedMap.entrySet());

        list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        HashMap<String, Long> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Long> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    public static String getNameOfDay() {
        DateFormatSymbols dfs = new DateFormatSymbols(Locale.ENGLISH);
        String[] weekdays = dfs.getWeekdays();

        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);

        return weekdays[day];
    }
}