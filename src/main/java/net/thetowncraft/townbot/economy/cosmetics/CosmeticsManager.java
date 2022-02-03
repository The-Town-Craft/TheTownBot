package net.thetowncraft.townbot.economy.cosmetics;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
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

    public static String SHOP_CHANNEL_ID;
    public static final Map<String, String> COSMETIC_MESSAGES = new HashMap<>();

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

    public static void resetShopChannel() {
        List<TextChannel> channels = Constants.THE_TOWN.getTextChannelsByName(BUY_EMOJI + "-shop", false);
        if(channels.size() == 0) throw new IllegalArgumentException("There is no shop channel, or it's name has been changed");
        if(channels.size() > 1) throw new IllegalArgumentException("There is more than one shop channel!");

        TextChannel shopChannel = channels.get(0);
        TextChannel channel = shopChannel.createCopy().complete();
        SHOP_CHANNEL_ID = channel.getId();
        shopChannel.delete().queue();
    }

    public static Cosmetic getCosmetic(String id) {
        return COSMETIC_REGISTRIES.get(id);
    }

    public static void register(String id, Cosmetic cosmetic) {
        if(COSMETIC_REGISTRIES.get(id) != null) throw new IllegalArgumentException("A cosmetic with id \"" + id + "\" already exists!");
        cosmetic.id = id;
        COSMETIC_REGISTRIES.put(id, cosmetic);

        File dir = new File(Plugin.get().getDataFolder(), "cosmetics/shop/message");
        dir.mkdirs();

        EmbedBuilder embed = cosmetic.getShopMessageEmbedBuilder();

        if(SHOP_CHANNEL_ID == null) return;

        Bot.jda.getTextChannelById(SHOP_CHANNEL_ID).sendMessage(embed.build()).queue(message -> {
            if(id != null) {
                COSMETIC_MESSAGES.put(message.getId(), id);
                message.addReaction("\uD83D\uDCB0").queue();
            }
        });
    }

    public static Cosmetic getCosmeticByShopMessage(String messageId) {
        String shopId = COSMETIC_MESSAGES.get(messageId);
        if(shopId == null) return null;

        return getCosmetic(shopId);
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
