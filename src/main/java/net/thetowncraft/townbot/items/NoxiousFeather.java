package net.thetowncraft.townbot.items;

import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class NoxiousFeather extends CustomItem {


    @Override
    public String getName() {
        return ChatColor.GREEN + "The Noxious Feather";
    }

    @Override
    public String getDescription() {
        return ChatColor.DARK_GREEN + "Adds an extra jump.";
    }

    @Override
    public Material getBaseItem() {
        return Material.FEATHER;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.EPIC;
    }

    @Override
    public void onInteract(PlayerInteractEvent event, int amount) {
        Player player = event.getPlayer();
        if(player.isOnGround()) return;

        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Vector vel = player.getVelocity();
            player.setVelocity(new Vector(vel.getX(), vel.getY() + 0.42, vel.getZ()));
        }
    }

    @Override
    public boolean shines() {
        return false;
    }
}
