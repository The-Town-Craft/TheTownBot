package net.thetowncraft.townbot.factions.economy.shop.items;

import org.bukkit.Particle;

import java.awt.*;

public class BlazingSpiritShopItem extends ParticleShopItem {

    @Override
    public String getName() {
        return "Blazing Spirit";
    }

    @Override
    public String getDesc() {
        return "Emit blazing hot particles from your character!";
    }

    @Override
    public String getImage() {
        return "https://cdn.discordapp.com/attachments/819063742335680512/1001421024870547486/blazing_spirit.png";
    }

    @Override
    public Color getColor() {
        return Color.ORANGE;
    }

    @Override
    public int getPrice() {
        return 600;
    }

    @Override
    public String getRoleId() {
        return "933970760056315904";
    }

    @Override
    public Particle getParticle() {
        return Particle.LAVA;
    }

    @Override
    public int getAmount() {
        return 10;
    }
}
