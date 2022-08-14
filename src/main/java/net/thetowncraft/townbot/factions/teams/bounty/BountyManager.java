package net.thetowncraft.townbot.factions.teams.bounty;

import net.thetowncraft.townbot.util.data.Data;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

public class BountyManager {

    public static final HashMap<String, Integer> BOUNTIES = new HashMap<>();

    public static void save() {
        JSONObject bounties = new JSONObject();
        BOUNTIES.forEach(bounties::put);
        Data.writeToFile(getFile(), bounties.toString());
    }

    public static void load() {
        JSONObject bounties = Data.getJSONObjectFromFile(getFile());
        for(String key : bounties.keySet()) {
            try {
                BOUNTIES.put(key, (Integer) bounties.get(key));
            }
            catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static File getFile() {
        return Data.getFile("factions/bounty.json");
    }
}
