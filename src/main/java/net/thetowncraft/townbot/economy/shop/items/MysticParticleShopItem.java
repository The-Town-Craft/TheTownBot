package net.thetowncraft.townbot.economy.shop.items;

import net.thetowncraft.townbot.economy.shop.ShopItem;
import org.bukkit.Particle;

import java.awt.*;

public class MysticParticleShopItem extends ParticleShopItem {

    @Override
    public String getName() {
        return "Mystic Aura";
    }

    @Override
    public Particle getParticle() {
        return Particle.PORTAL;
    }

    @Override
    public int getAmount() {
        return 70;
    }

    @Override
    public String getDesc() {
        return "Emit magical purple particles from your character!";
    }

    @Override
    public String getImage() {
        return "https://cdn.discordapp.com/attachments/819063742335680512/1001421316148174930/mystic_aura.png";
    }

    @Override
    public Color getColor() {
        return new Color(0x8000FF);
    }

    @Override
    public int getPrice() {
        return 400;
    }

    @Override
    public String getRoleId() {
        return "1001355803313315921";
    }
}
