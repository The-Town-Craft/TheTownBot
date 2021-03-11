package net.thetowncraft.townbot.listeners.minecraft.anticheat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class XrayDetection implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        AntiCheatManager.INSTANCE.blockBroke(event.getPlayer(), event.getBlock());
    }
}