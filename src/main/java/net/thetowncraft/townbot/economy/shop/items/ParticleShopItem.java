package net.thetowncraft.townbot.economy.shop.items;

import net.thetowncraft.townbot.economy.shop.ShopItem;
import org.bukkit.Particle;

public abstract class ParticleShopItem extends ShopItem {
    public abstract Particle getParticle();
    public abstract int getAmount();
}
