package net.thetowncraft.townbot.custom_items;

import net.thetowncraft.townbot.util.Rarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

public class ShapedGlass extends CustomItem {

    @Override
    public String getName() {
        return "Shaped Glass";
    }

    @Override
    public String getDescription() {
        return ChatColor.AQUA + "Double your damage... " + ChatColor.RED + "BUT halve your health.";
    }

    @Override
    public int getCustomModelData() {
        return 1;
    }

    @Override
    public Material getBaseItem() {
        return Material.BLUE_STAINED_GLASS_PANE;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.RARE;
    }

    @Override
    public boolean shines() {
        return true;
    }

    @Override
    public void updateStats(Player player, int amount) {
        AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if(attribute == null) {
            System.out.println(player.getName() + "'s GENERIC_MAX_HEALTH attribute is null.");
            return;
        }

        if(amount == 0) {
            attribute.setBaseValue(20);
            return;
        }
        attribute.setBaseValue(20.0 / (amount + 1));
    }
}
