package net.thetowncraft.townbot.economy.cosmetics;

import net.thetowncraft.townbot.util.Rarity;

public class TestCosmetic extends Cosmetic {

    @Override
    public String getName() {
        return "Test Cosmetic";
    }

    @Override
    public String getDescription() {
        return "A test cosmetic! Don't buy this its not worth it and only for testing";
    }

    @Override
    public int getPrice() {
        return 10;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.COMMON;
    }

    @Override
    public String getEmote() {
        return ":rofl:";
    }

    @Override
    public String getImagePath() {
        return "https://www.minecraft.net/content/dam/games/minecraft/screenshots/1-17-1-pre-release-1-header.jpg";
    }
}
