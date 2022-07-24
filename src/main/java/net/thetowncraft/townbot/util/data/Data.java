package net.thetowncraft.townbot.util.data;

import com.sun.tools.jdeprscan.scan.Scan;
import net.thetowncraft.townbot.util.Utils;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Data {

    public static File getFile(String path) {
        File file = new File("plugins/MinecordBot/" + path);
        if(!file.exists()) {
            try {
                if(!file.createNewFile()) {
                    return null;
                }
            } catch (IOException e) {
                return null;
            }
        }
        if(!file.exists()) {
            return null;
        }
        return file;
    }

    public static List<String> getStringsFromFile(File file) {
        List<String> strings = new ArrayList<>();
        if(file == null) return strings;
        if(!file.exists()) return strings;

        Scanner scan;
        try {
            scan = new Scanner(file);
        } catch (FileNotFoundException e) {
            return strings;
        }

        while(scan.hasNextLine()) strings.add(scan.nextLine());
        return strings;
    }

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
        return new File("plugins/DetailedLogs/compiled-log/" + name + ".txt");
    }
}