package net.thetowncraft.townbot;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.EmbedBuilder;
import net.thetowncraft.townbot.modmail.ModMail;
import net.thetowncraft.townbot.util.Constants;
import net.thetowncraft.townbot.util.Registry;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.thetowncraft.townbot.util.data.Config;

public class Bot {

    public static JDA jda;
    public static String prefix = "?";

    /**
     * Initializes the bot, and sets its activity, status, and intents.
     */
    public static void enable() {

        JDABuilder builder = JDABuilder.createDefault(Config.get("token"));
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.playing("The Town SMP!"));

        // JDA Listeners

        Registry.registerJDAListeners(builder);
        Registry.registerDiscordCommands();
        ModMail.initActivityMessages();

        builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        builder.setChunkingFilter(ChunkingFilter.ALL);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);

        try {
            Bot.jda = builder.build();
        }
        catch (LoginException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stops & disconnects the bot. This method is invoked when the server stops.
     */
    public static void disable() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor("Server stopping!", null, Constants.THE_TOWN.getIconUrl());
        embed.setColor(Constants.RED);
        Bot.jda.getTextChannelById(Constants.MC_LOGS).sendMessage(embed.build()).queue();
        Bot.jda.getTextChannelById(Constants.MC_CHAT).sendMessage(embed.build()).complete();
        //Constants.PLAYER_COUNT_CHANNEL.getManager().setName("Server Offline").complete();
        Bot.jda.shutdownNow();
    }
}