package net.thetowncraft.townbot.util.data;

import net.thetowncraft.townbot.util.Utils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Data {

    public static JSONObject getJSONObjectFromFile(File file) {
        String jsonString = "";
        try {
            Scanner scan = new Scanner(file);
            while(scan.hasNextLine()) jsonString += scan.nextLine();
        } catch (IOException e) {
            return new JSONObject("{}");
        }
        if(jsonString.isEmpty()) return new JSONObject("{}");

        return new JSONObject(jsonString);
    }

    public static JSONObject getJSONObjectFromFile(File dir, String child) {
        File file = new File(dir, child);
        return getJSONObjectFromFile(file);
    }

    /**
     * @return Today's log file
     */
    public static File getLogFile() {
        return getLogFile(Utils.getSimpleDate());
    }

    /**
     * @param name The date of the log file you want to retrieve
     * @return The log file from that date
     */
    public static File getLogFile(String name) {
        return new File("DetailedLogs/compiled-log/" + name + ".txt");
    }
}