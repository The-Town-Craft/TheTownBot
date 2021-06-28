package net.thetowncraft.townbot.economy.cosmetics;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.economy.EconomyManager;
import net.thetowncraft.townbot.util.Constants;
import org.bukkit.OfflinePlayer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class CosmeticsManager {

    public static final String BUY_EMOJI = "\uD83D\uDCB0";

    private static final Map<String, Cosmetic> COSMETIC_REGISTRIES = new HashMap<>();
    private static final Map<String, List<Cosmetic>> PLAYER_COSMETICS = new HashMap<>();

    public static Map<String, Cosmetic> getCosmetics() {
        return COSMETIC_REGISTRIES;
    }

    public static List<Cosmetic> getCosmetics(OfflinePlayer player) {
        List<Cosmetic> cosmetics = PLAYER_COSMETICS.get(player.getUniqueId().toString());
        if(cosmetics == null) cosmetics = new ArrayList<>();

        return cosmetics;
    }

    public static Cosmetic getCosmetic(String id) {
        return COSMETIC_REGISTRIES.get(id);
    }

    public static void register(String id, Cosmetic cosmetic) {
        if(COSMETIC_REGISTRIES.get(id) != null) throw new IllegalArgumentException("A cosmetic with id \"" + id + "\" already exists!");
        cosmetic.id = id;
        COSMETIC_REGISTRIES.put(id, cosmetic);

        if(messageExists(cosmetic.id)) {
            return;
        }

        File dir = new File(Plugin.get().getDataFolder(), "cosmetics/shop/message");
        dir.mkdirs();

        EmbedBuilder embed = cosmetic.getShopMessageEmbedBuilder();

        Bot.jda.getTextChannelById("854843843387064341").sendMessage(embed.build()).queue(message -> {

            File file = new File(dir, message.getId() + ".txt");

            try {
                FileWriter write = new FileWriter(file);
                write.write(id);
                write.close();
            }
            catch(IOException e) {
                return;
            }
            message.addReaction("\uD83D\uDCB0").queue();
        });
    }

    private static boolean messageExists(String cosmeticId) {
        File dir = new File(Plugin.get().getDataFolder(), "cosmetics/shop/message");
        dir.mkdirs();

        for(String fileName : Objects.requireNonNull(dir.list())) {
            if(!fileName.contains(".txt")) continue;

            try {
                Scanner scan = new Scanner(new File(dir, fileName));
                System.out.println("test");
                if(cosmeticId.equals(scan.nextLine())) {
                    System.out.println("test1");
                    return Bot.jda.getTextChannelById(fileName.replace(".txt", "")) != null;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static Cosmetic getCosmeticByShopMessage(String messageId) {
        File dir = new File(Plugin.get().getDataFolder(), "cosmetics/shop/message");
        dir.mkdirs();

        File file = new File(dir, messageId + ".txt");
        if(!file.exists()) return null;

        try {
            Scanner scan = new Scanner(file);
            return getCosmetic(scan.nextLine());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void addCosmetic(OfflinePlayer player, Cosmetic cosmetic) {
        String uuid = player.getUniqueId().toString();
        List<Cosmetic> cosmetics = PLAYER_COSMETICS.get(uuid);
        if(cosmetics == null) {
            List<Cosmetic> newCosmetics = new ArrayList<>();
            newCosmetics.add(cosmetic);
            PLAYER_COSMETICS.put(uuid, new ArrayList<>(newCosmetics));
            return;
        }

        cosmetics.add(cosmetic);
    }

    public static void purchaseCosmetic(OfflinePlayer player, Cosmetic cosmetic) {
        addCosmetic(player, cosmetic);
        EconomyManager.subtractCoins(player.getUniqueId().toString(), cosmetic.getPrice());
    }
}
