package com.cadenkoehl.minecordbot.listeners.discord;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class JoinServer extends ListenerAdapter {
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        event.getMember().getUser().openPrivateChannel().queue((channel -> {
            channel.sendMessage("Thanks for joining **The Town**! Below is a link to our whitelist application! \nhttps://forms.gle/TgNNRj52g5tfwAPs5\n If your application is accepted, you will gain access to all the " +
                    "channels in the Discord server! If not, you will be kicked. No hard feelings :)").queue();
        }));
    }
}