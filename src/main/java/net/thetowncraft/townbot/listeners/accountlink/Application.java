package net.thetowncraft.townbot.listeners.accountlink;

import net.thetowncraft.townbot.util.Constants;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class Application extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;

        if(event.getChannel().getId().equalsIgnoreCase(Constants.WHITELIST_APP_CHANNEL)) {
            if(!event.getMessage().getContentRaw().equalsIgnoreCase("-apply whitelist")) {
                event.getMessage().delete().queue();
                event.getAuthor().openPrivateChannel().queue(channel -> {
                    channel.sendMessage("Please type `-apply whitelist` in <#" + Constants.WHITELIST_APP_CHANNEL + "> to begin the application!").queue();
                });
            }
        }
    }
}
