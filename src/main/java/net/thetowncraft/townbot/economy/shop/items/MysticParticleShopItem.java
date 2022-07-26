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
    public String getDesc() {
        return "Emit magical purple particles from your character!";
    }

    @Override
    public String getImage() {
        return "https://cdn.discordapp.com/attachments/997986798254948392/997988512534110269/unknown.png";
    }

    @Override
    public Color getColor() {
        return new Color(0x8000FF);
    }

    @Override
    public int getPrice() {
        return 500;
    }

    @Override
    public String getRoleId() {
        return "1001355803313315921";
    }
}
