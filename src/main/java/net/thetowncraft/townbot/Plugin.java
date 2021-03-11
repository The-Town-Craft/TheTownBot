package net.thetowncraft.townbot;

import net.thetowncraft.townbot.listeners.minecraft.player_activity.RepeatingTasks;
import net.thetowncraft.townbot.listeners.minecraft.player_activity.active.ActivityManager;
import net.thetowncraft.townbot.util.Registry;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {

    /**
     * Initializing this class will throw a {@code java.lang.IllegalArgumentException}, so use {@code get()} instead
     */
    private Plugin() {}

    /**
     * This method runs whenever the server starts or restarts
     */
    @Override
    public void onEnable() {

        Bot.start();

        Registry.registerSpigotListeners(this);
        Registry.registerMinecraftCommands();

        ActivityManager.loadActivityPoints();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, RepeatingTasks::updatePlayerActivity, 0, RepeatingTasks.REPEATING_TICKS);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, RepeatingTasks::doDailyTasks, RepeatingTasks.TICKS_IN_A_DAY - 10000, RepeatingTasks.TICKS_IN_A_DAY);
    }

    /**
     * This method runs whenever the server stops or restarts
     */
    @Override
    public void onDisable() {
        Bot.jda.shutdownNow();
    }

    /**
     * @return An instance of this plugin
     */
    public static Plugin get() {
        return getPlugin(Plugin.class);
    }
}