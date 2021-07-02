package net.thetowncraft.townbot.economy.player_shops;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Biome;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;

public class PlayerShopManager {

    private static final List<PlayerShop> SHOPS = new ArrayList<>();
    private static final Map<String, Integer> DIAMOND_COINS = new HashMap<>();

    public static int getDiamondsInInventory(Player player) {
        int amount = 0;

        for(ItemStack stack : player.getInventory().getContents()) {
            if(stack.getType() == Material.DIAMOND) {
                amount += stack.getAmount();
            }
        }

        return amount;
    }

    public static int getDiamondCoins(OfflinePlayer player) {
        Integer coins = DIAMOND_COINS.get(player.getUniqueId().toString());
        if(coins == null) return 0;

        return coins;
    }

    public static void depositDiamonds(Player player, int amount) {

        String uuid = player.getUniqueId().toString();
        int diamonds = getDiamondsInInventory(player);

        if(diamonds < amount) {
            player.sendMessage(ChatColor.RED + "You can't deposit " + amount + " diamonds if you only have " + diamonds);
            return;
        }

        if(DIAMOND_COINS.containsKey(uuid)) {
            DIAMOND_COINS.replace(uuid, amount + DIAMOND_COINS.get(uuid));
        }
        else {
            DIAMOND_COINS.put(uuid, amount);
        }
        addDiamondCoins(player, amount);
        subtractDiamonds(player, amount);
        player.sendMessage(ChatColor.GREEN + "Deposited " + amount + " diamond coins into your account!");
    }

    public static void withdrawDiamonds(Player player, int amount) {
        subtractDiamonds(player, amount);
        subtractDiamondCoins(player, amount);
        player.sendMessage(ChatColor.GREEN + "Withdrew " + amount + " diamonds from your account!");
    }

    public static void addDiamondCoins(Player player, int amount) {

        String uuid = player.getUniqueId().toString();

        if(DIAMOND_COINS.containsKey(uuid)) {
            DIAMOND_COINS.replace(uuid, amount + DIAMOND_COINS.get(uuid));
        }
        else {
            DIAMOND_COINS.put(uuid, amount);
        }
    }

    public static void addDiamonds(Player player, int amount) {
        Item item = (Item) player.getWorld().spawnEntity(player.getLocation(), EntityType.DROPPED_ITEM);
        item.setItemStack(new ItemStack(Material.DIAMOND, amount));
    }

    public static void subtractDiamonds(Player player, int amount) {
        for(ItemStack stack : player.getInventory().getContents()) {
            if(stack.getType() == Material.DIAMOND) {
                int newAmount = stack.getAmount() - amount;
                if(newAmount < 0) newAmount = 0;
                stack.setAmount(newAmount);
                return;
            }
        }
    }

    public static void subtractDiamondCoins(Player player, int amount) {

        String uuid = player.getUniqueId().toString();

        if(DIAMOND_COINS.containsKey(uuid)) {
            int newAmount = DIAMOND_COINS.get(uuid) - amount;
            if(newAmount < 0) newAmount = 0;
            DIAMOND_COINS.replace(uuid, newAmount);
        }
        else {
            DIAMOND_COINS.put(uuid, amount);
        }
    }

    public static void addShop(OfflinePlayer player, Vector pos, int price, boolean perStack) {
        addShop(new PlayerShop(player.getUniqueId().toString(), pos, price, perStack));
    }

    public static PlayerShop getShop(Vector pos) {
         for(PlayerShop shop : SHOPS) {
             if(shop.getPos().getX() == pos.getX() && shop.getPos().getY() == pos.getY() && shop.getPos().getZ() == pos.getZ()) {
                 return shop;
             }
         }
         return null;
    }

    public static HashMap<Material, Integer> getItemAmounts(Chest chest) {
        HashMap<Material, Integer> itemAmounts = new HashMap<>();

        for(ItemStack stack : chest.getInventory().getContents()) {
            if(stack == null) continue;
            Material item = stack.getType();
            if(!itemAmounts.containsKey(item)) {
                itemAmounts.put(item, stack.getAmount());
            }
            else {
                Integer amount = itemAmounts.get(item);
                itemAmounts.replace(item, amount + stack.getAmount());
            }
        }
        return itemAmounts;
    }

    public static boolean inShoppingDistrict(Location pos) {
        return pos.getX() > -1245 && pos.getX() < -913 && pos.getZ() < -800 && pos.getZ() > -1123 && (pos.getWorld().getBiome(pos.getBlockX(), pos.getBlockZ(), pos.getBlockZ()) == Biome.MUSHROOM_FIELD_SHORE || pos.getWorld().getBiome(pos.getBlockX(), pos.getBlockZ(), pos.getBlockZ()) == Biome.MUSHROOM_FIELDS);
    }

    public static void addShop(PlayerShop shop) {
        SHOPS.add(shop);
    }

    public static void removeShop(PlayerShop shop) {
        SHOPS.remove(shop);
    }

    public static List<PlayerShop> getShops() {
        return SHOPS;
    }
}
