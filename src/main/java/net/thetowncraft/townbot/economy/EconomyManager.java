package net.thetowncraft.townbot.economy;

import net.dv8tion.jda.api.entities.Member;
import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
import net.thetowncraft.townbot.modmail.ModMail;
import net.thetowncraft.townbot.util.data.Data;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EconomyManager {

    private static final Map<String, Integer> COIN_MAP = new HashMap<>();

    private static File DIR;

    public static Map<String, Integer> getCoinMap() {
        return COIN_MAP;
    }

    public static int getCoinBalance(String playerUUID) {
        Integer balance = COIN_MAP.get(playerUUID);
        if(balance == null) return 0;
        return balance;
    }

    public static int getCoinBalance(Member member) {
        OfflinePlayer player = AccountManager.getInstance().getMinecraftPlayer(member);
        if(player == null) return 0;
        return getCoinBalance(player);
    }

    public static int getCoinBalance(OfflinePlayer player) {
        Integer balance = COIN_MAP.get(player.getUniqueId().toString());
        if(balance == null) return 0;
        return balance;
    }

    public static void setCoinBalance(String playerUUID, int balance) {
        COIN_MAP.put(playerUUID, balance);
    }

    public static void addCoins(String playerUUID, int amount, String reason) {
        Integer coins = COIN_MAP.get(playerUUID);
        if(coins == null) coins = 0;

        COIN_MAP.put(playerUUID, coins + amount);

        if(reason == null) return;

        OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(playerUUID));
        Member member = AccountManager.getInstance().getDiscordMember(player);
        if(member == null) return;

        ModMail.sendModMail(member.getUser(), member.getGuild(), ":coin: **" + amount + " Coins** have been added to your account!\n:pencil: **Reason**: " + reason, new ArrayList<>());
    }

    public static void subtractCoins(String playerUUID, int amount) {
        Integer coins = COIN_MAP.get(playerUUID);
        if(coins == null) coins = 0;

        COIN_MAP.put(playerUUID, coins - amount);
    }

    public static void saveEconomy() {
        JSONObject jsonObject = new JSONObject();

        for(Map.Entry<String, Integer> entry : COIN_MAP.entrySet()) {
            if(entry.getValue() == null) continue;
            jsonObject.put(entry.getKey(), entry.getValue());
        }

        File file = new File(DIR, "economy.json");
        try {
            FileWriter write = new FileWriter(file);
            write.write(jsonObject.toString());
            write.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadEconomy() {
        DIR = new File(Plugin.get().getDataFolder(),  "economy");
        DIR.mkdirs();

        File file = new File(DIR, "economy.json");
        if(!file.exists()) return;

        JSONObject jsonObject = Data.getJSONObjectFromFile(file);

        for(String playerUUID : jsonObject.keySet()) {
            setCoinBalance(playerUUID, jsonObject.getInt(playerUUID));
        }
    }
}
