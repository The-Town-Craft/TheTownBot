package net.thetowncraft.townbot.factions.bounty;

import net.dv8tion.jda.api.entities.Member;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
import net.thetowncraft.townbot.util.data.Data;
import org.bukkit.OfflinePlayer;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public class BountyManager {

    private static final HashMap<String, Integer> BOUNTIES = new HashMap<>();

    public void setBounty(OfflinePlayer player, int amount) {
        BOUNTIES.put(player.getUniqueId().toString(), amount);
    }

    public void setBounty(Member member, int amount) {
        OfflinePlayer player = AccountManager.getInstance().getMinecraftPlayer(member);
        if(player == null) return;
        setBounty(player, amount);
    }

    public int getBounty(OfflinePlayer player) {
        Integer bounty = BOUNTIES.get(player.getUniqueId().toString());
        if(bounty == null) return 0;
        return bounty;
    }

    public int getBounty(Member member) {
        OfflinePlayer player = AccountManager.getInstance().getMinecraftPlayer(member);
        if(player == null) return 0;
        return getBounty(player);
    }

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
