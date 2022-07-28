package net.thetowncraft.townbot.listeners.discord;

import net.dv8tion.jda.api.entities.Member;
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
        embed.setAuthor("Server is back online!", null, Constants.THE_TOWN.getIconUrl());
        embed.setColor(Constants.GREEN);
        event.getJDA().getTextChannelById(Constants.MC_LOGS).sendMessage(embed.build()).queue();
        event.getJDA().getTextChannelById(Constants.MC_CHAT).sendMessage(embed.build()).queue();
        Registry.registerCosmetics();
    }
}