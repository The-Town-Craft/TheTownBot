package net.thetowncraft.townbot.util.data;

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
}