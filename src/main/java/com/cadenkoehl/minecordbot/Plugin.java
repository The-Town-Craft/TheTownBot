package com.cadenkoehl.minecordbot;

import com.cadenkoehl.minecordbot.listeners.minecraft.player_activity.RepeatingTasks;
import com.cadenkoehl.minecordbot.listeners.minecraft.player_activity.active.ActivityManager;
import com.cadenkoehl.minecordbot.listeners.util.Registry;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {
    @Override
    public void onEnable() {

        Bot.start();

        Registry.registerSpigotListeners(this);

        ActivityManager.loadActivityPoints();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, RepeatingTasks::updatePlayerActivity, 0, RepeatingTasks.REPEATING_TICKS);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, RepeatingTasks::doDailyTasks, RepeatingTasks.TICKS_IN_A_DAY - 10000, RepeatingTasks.TICKS_IN_A_DAY);
    }

    @Override
    public void onDisable() {
        Bot.jda.shutdownNow();
    }

    public static Plugin get() {
        return getPlugin(Plugin.class);
    }

}
