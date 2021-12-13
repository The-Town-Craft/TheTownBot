package net.thetowncraft.townbot.economy.player_shops;

import net.thetowncraft.townbot.economy.player_shops.commands.ShopChestCommand;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PlayerShopListener implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if(block.getType() != Material.CHEST) return;

        ItemMeta meta = event.getItemInHand().getItemMeta();
        if(meta == null) return;

        List<String> lore = meta.getLore();
        if(lore == null || lore.size() != 2) {
            return;
        }

        if(player.getWorld().getName().contains("end") || player.getWorld().getName().contains("nether")) {
            player.sendMessage(ChatColor.RED + "You can only place shop chests in the overworld!");
            event.setCancelled(true);
            return;
        }

        if(!PlayerShopManager.inShoppingDistrict(block.getLocation())) {
            event.getPlayer().sendMessage(ChatColor.RED + "You can only place your shop in the shopping district! (The nether portal is at -143 -126 on the nether roof)");
            event.setCancelled(true);
            return;
        };

        int price;

        try {
            price = Integer.parseInt(lore.get(0));
        } catch (NumberFormatException e) {
            return;
        }

        boolean perStack = Boolean.parseBoolean(lore.get(1));

        PlayerShopManager.addShop(player, new Vector(block.getX(), block.getY(), block.getZ()), price, perStack);

        String perStackString = perStack ? "stack" : "item";
        player.sendMessage(ChatColor.GREEN + "Your shop has been placed! No one can steal anything you put in this chest, and they will have to pay " + price + " diamonds per " + perStackString + " to take anything out of it. You are the only one that can destroy this chest.");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if(block.getType() != Material.CHEST) return;

        if(player.getWorld().getName().contains("end") || player.getWorld().getName().contains("nether")) {
            return;
        }

        PlayerShop shop = PlayerShopManager.getShop(block.getLocation().toVector());
        if(shop == null) return;

        if(player.getUniqueId().toString().equals(shop.getPlayerUUID())) {
            PlayerShopManager.removeShop(shop);
            event.setDropItems(false);
            Item item = (Item) block.getWorld().spawnEntity(block.getLocation(), EntityType.DROPPED_ITEM);
            ItemStack stack = new ItemStack(Material.CHEST);
            ItemMeta meta = stack.getItemMeta();

            List<String> lore = new ArrayList<>();
            lore.add(String.valueOf(shop.getPrice()));
            lore.add(String.valueOf(shop.isPerStack()));

            meta.setLore(lore);
            meta.setDisplayName(player.getDisplayName() + "'s Shop, " + shop.getPrice() + " diamonds per item");
            stack.setItemMeta(meta);
            item.setItemStack(stack);
            event.getPlayer().sendMessage(ChatColor.GREEN + "Your shop has been removed! To change the price of the shop, you can once again use the /shopchest command!");
            return;
        }

        OfflinePlayer shopOwner = Bukkit.getOfflinePlayer(UUID.fromString(shop.getPlayerUUID()));
        String name = shopOwner.getName();
        if(name == null) return;

        event.getPlayer().sendMessage(ChatColor.RED + "You cannot destroy " + name + "'s shop!");
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if(PlayerShopManager.inShoppingDistrict(event.getLocation())) {
            event.blockList().clear();
        }
    }

    public static final List<String> shoppingInventory = new ArrayList<>();

    @EventHandler
    public void onClickChest(PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            Block block = event.getClickedBlock();
            if(block == null) return;
            if(block.getType() != Material.CHEST) return;

            Chest chest = (Chest) block.getState();

            PlayerShop shop = PlayerShopManager.getShop(block.getLocation().toVector());
            if(shop == null) return;

            //if(player.getUniqueId().toString().equals(shop.getPlayerUUID())) return;

            HashMap<Material, Integer> itemAmounts = PlayerShopManager.getItemAmounts(chest);

            Inventory inventory = Bukkit.getServer().createInventory(null, 27, Objects.requireNonNull(chest.getCustomName()));
            for(Map.Entry<Material, Integer> entry : itemAmounts.entrySet()) {
                inventory.addItem(new ItemStack(entry.getKey(), entry.getValue()));
            }

            player.openInventory(inventory);
            shoppingInventory.add(event.getPlayer().getUniqueId().toString());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        HumanEntity player = event.getWhoClicked();
        if(shoppingInventory.contains(player.getUniqueId().toString())) {
            event.setCancelled(true);

            Inventory chest = event.getClickedInventory();
            if(chest == null) return;

            Location pos = chest.getLocation();
            if(pos == null) return;

            PlayerShop shop = PlayerShopManager.getShop(pos.toVector());
            if(shop == null) return;

            ItemStack stack = chest.getItem(event.getSlot());
            if(stack == null) return;

            int diamondsInInventory = PlayerShopManager.getDiamondsInInventory(player);

            if(shop.isPerStack()) {
                if(diamondsInInventory < shop.getPrice()) {
                    player.closeInventory();
                    player.sendMessage(ChatColor.RED + "You need " + (shop.getPrice() - diamondsInInventory) + "more diamonds to purchase this stack of items!");
                    return;
                }
                PlayerShopManager.subtractDiamonds(player, shop.getPrice());
                PlayerShopManager.addDiamondCoins(shop.getOwner(), shop.getPrice());
            }
            else {
                if(diamondsInInventory < shop.getPrice()) {
                    player.closeInventory();
                    player.sendMessage(ChatColor.RED + "This shop type is not supported yet!");
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        shoppingInventory.remove(event.getPlayer().getUniqueId().toString());
    }
}
