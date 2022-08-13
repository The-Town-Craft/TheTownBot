package net.thetowncraft.townbot.api.command_handler.minecraft;

import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class MinecraftCommand implements TabExecutor {

    public static final List<MinecraftCommand> COMMANDS = new ArrayList<>();

    public static void registerCommands(MinecraftCommand... commands) {
        COMMANDS.addAll(Arrays.asList(commands));
        for(MinecraftCommand command : COMMANDS) {
            PluginCommand pluginCommand = Plugin.get().getCommand(command.getName());
            if(pluginCommand == null) continue;
            pluginCommand.setTabCompleter(command);
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }

    /**
     * This method is called whenever the command is executed
     */
    public abstract void execute(CommandEvent.Minecraft event);

    /**
     * Should this command only be used by admins?
     * @return true if the command should only be used by admins
     */
    public abstract boolean isAdminCommand();

    public abstract String getName();
}
