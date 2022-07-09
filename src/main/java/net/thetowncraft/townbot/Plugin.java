package net.thetowncraft.townbot;

import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.minecraft.MinecraftCommand;
import net.thetowncraft.townbot.dimension.DimensionEventListener;
import net.thetowncraft.townbot.economy.EconomyManager;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
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

import java.util.Locale;

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

        MaintenanceCommand.loadData();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, RepeatingTasks::updatePlayerActivity, 0, RepeatingTasks.REPEATING_TICKS);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, RepeatingTasks::doDailyTasks, RepeatingTasks.TICKS_IN_A_DAY, RepeatingTasks.TICKS_IN_A_DAY);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, PlayerCountStatus::update, 5000, RepeatingTasks.REPEATING_TICKS);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, DimensionEventListener::checkBiomeEffects, 60, 60);
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