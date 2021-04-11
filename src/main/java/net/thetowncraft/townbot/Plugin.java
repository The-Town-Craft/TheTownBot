package net.thetowncraft.townbot;

import net.thetowncraft.townbot.listeners.minecraft.player_activity.PlayerCountStatus;
import net.thetowncraft.townbot.util.RepeatingTasks;
import net.thetowncraft.townbot.listeners.minecraft.player_activity.active.ActivityManager;
import net.thetowncraft.townbot.util.Registry;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {

    /**
     * This method runs whenever the server starts or restarts
     */
    @Override
    public void onEnable() {

        Bot.enable();

        Registry.registerSpigotListeners(this);
        Registry.registerMinecraftCommands();

        ActivityManager.loadActivityPoints();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, RepeatingTasks::updatePlayerActivity, 0, RepeatingTasks.REPEATING_TICKS);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, RepeatingTasks::doDailyTasks, 5000, RepeatingTasks.TICKS_IN_A_DAY);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, PlayerCountStatus::update, 5000, RepeatingTasks.REPEATING_TICKS);
    }

    /**
     * This method runs whenever the server stops or restarts
     */
    @Override
    public void onDisable() {
        Bot.disable();
    }

    /**
     * @return An instance of this plugin
     */
    public static Plugin get() {
        return getPlugin(Plugin.class);
    }
}