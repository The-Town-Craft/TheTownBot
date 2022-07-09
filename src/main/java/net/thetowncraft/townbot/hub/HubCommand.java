package net.thetowncraft.townbot.hub;

import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.minecraft.MinecraftCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HubCommand extends MinecraftCommand {

    public static final Location HUB_LOCATION = new Location(Bukkit.getWorld("world_1597802541_thetown_hub"), 0, 115, 0);

    public static final Map<String, Location> PLAYER_LOCATIONS = new HashMap<>();

    @Override
    public void execute(CommandEvent.Minecraft event) {
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        if(player.getWorld().getName().equals(HUB_LOCATION.getWorld().getName())) {
            Location location = PLAYER_LOCATIONS.get(uuid);
            if(location == null) location = Plugin.SPAWN_LOCATION;
            else {
                PLAYER_LOCATIONS.remove(uuid);
            }
            player.teleport(location);
        }
        else {
            PLAYER_LOCATIONS.put(uuid, player.getLocation());
            player.teleport(HUB_LOCATION);
        }
    }

    @Override
    public boolean isAdminCommand() {
        return true;
    }

    @Override
    public String getName() {
        return "hub";
    }
}
