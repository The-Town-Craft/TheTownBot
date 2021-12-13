package net.thetowncraft.townbot.listeners.discord;

import net.thetowncraft.townbot.economy.cosmetics.CosmeticsManager;
import net.thetowncraft.townbot.util.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.thetowncraft.townbot.util.Registry;
import org.jetbrains.annotations.NotNull;


public class ServerStart extends ListenerAdapter {
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor("Server is back online!", null, "https://images-ext-2.discordapp.net/external/Q2AE1BtpdBXC7B0W4-Jb4R-GhCQ48RzBeaW_DHZs1YQ/https/cdn.discordapp.com/icons/730975912320827452/a_1440022f530b68ab784fa8bc1d536650.gif");
        embed.setColor(Constants.GREEN);
        event.getJDA().getTextChannelById(Constants.MC_LOGS).sendMessage(embed.build()).queue();
        event.getJDA().getTextChannelById(Constants.MC_CHAT).sendMessage(embed.build()).queue();
        CosmeticsManager.resetShopChannel();
        Registry.registerCosmetics();
    }
}