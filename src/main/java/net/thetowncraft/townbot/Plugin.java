package net.thetowncraft.townbot;

import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.minecraft.MinecraftCommand;
import net.thetowncraft.townbot.custom_items.mystic_realm.glacial_items.GlacialArmor;
import net.thetowncraft.townbot.dimension.MysticRealmListener;
import net.thetowncraft.townbot.factions.economy.EconomyManager;
import net.thetowncraft.townbot.factions.economy.shop.ShopItemListener;
import net.thetowncraft.townbot.factions.economy.shop.ShopManager;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
import net.thetowncraft.townbot.listeners.accountlink.LinkAccount;
import net.thetowncraft.townbot.listeners.minecraft.commands.MaintenanceCommand;
import net.thetowncraft.townbot.listeners.minecraft.player_activity.PlayerCountStatus;
import net.thetowncraft.townbot.util.Constants;
import net.thetowncraft.townbot.util.RepeatingTasks;
import net.thetowncraft.townbot.listeners.minecraft.player_activity.active.ActivityManager;
import net.thetowncraft.townbot.util.Registry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class Plugin extends JavaPlugin {
    
    public static boolean serverUnderMaintenance;

    public static Location SPAWN_LOCATION;

    public static final String OVERWORLD_NAME = "world_1597802541";
    public static final String VOID_DIMENSION_NAME = "world_1597802541_thetown_the_void";

    /**
     * This method runs whenever the server starts or restarts
     */
    @Override
    public void onEnable() {

        Bot.enable();

        Registry.registerSpigotListeners(this);
        Registry.registerMinecraftCommands();

        ActivityManager.loadActivityPoints();

        AccountManager.loadAccounts();
        EconomyManager.loadEconomy();

        ShopManager.initShop();

        MaintenanceCommand.loadData();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, RepeatingTasks::updatePlayerActivity, 0, RepeatingTasks.REPEATING_TICKS);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, RepeatingTasks::doDailyTasks, RepeatingTasks.TICKS_IN_A_DAY, RepeatingTasks.TICKS_IN_A_DAY);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, GlacialArmor::bossDefeatCheck, 80, 80);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, ShopItemListener::updateItems, 10, 10);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, MysticRealmListener::checkBiomeEffects, 60, 60);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, LinkAccount::checkLink, 20, 20);
        SPAWN_LOCATION = new Location(Bukkit.getWorld(OVERWORLD_NAME),-161, 64, 230);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        try {
            for(MinecraftCommand cmd : MinecraftCommand.COMMANDS) {
                if(command.getName().equals(cmd.getName())) {
                    if(!(sender instanceof Player)) {
                        sender.sendMessage(ChatColor.RED + "This command cannot be run from the console!");
                        return true;
                    }
                    Player player = (Player) sender;
                    if(!player.isOp() && cmd.isAdminCommand()) {
                        player.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
                        return true;
                    }
                    cmd.execute(new CommandEvent.Minecraft(player, args, cmd));
                    return true;
                }
            }
        }
        catch (Exception ex) {
            Bukkit.getServer().broadcastMessage("[ERROR]: " + ChatColor.RED + ex);
            Constants.DEV_CHAT.sendMessage(":x: [**" + Constants.DEV_ROLE.getAsMention() + "**] **An exception has occurred with a Minecraft command!** `" + ex + "`").queue();
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * This method runs whenever the server stops or restarts
     */
    @Override
    public void onDisable() {
        EconomyManager.saveEconomy();
        Bot.disable();
        MaintenanceCommand.saveData();
    }

    /**
     * @return An instance of this plugin
     */
    public static Plugin get() {
        return getPlugin(Plugin.class);
    }
}