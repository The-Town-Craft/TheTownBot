package net.thetowncraft.townbot.util.data;

import net.thetowncraft.townbot.Plugin;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class Config {

    /**
     * @param key The key you want to get from the config.json file
     * @return The value, null if the key is not found
     */
    public static String get(String key) {
        File dataFolder = Plugin.get().getDataFolder();

        JSONObject jsonObject = Data.getJSONObjectFromFile(new File(dataFolder, "config.json"));

        try {
            return jsonObject.getString(key);
        }
        catch (JSONException ex) {
            return null;
        }
    }
}
