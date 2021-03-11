package net.thetowncraft.townbot;

import javax.security.auth.login.LoginException;

import net.thetowncraft.townbot.util.Constants;
import net.thetowncraft.townbot.util.Registry;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

public class Bot {

    public static JDA jda;
    public static String prefix = "/";

    public static void start() {
        JDABuilder builder = JDABuilder.createDefault(Constants.TOKEN);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.playing("The Town SMP!"));

        // JDA Listeners

        Registry.registerJDAListeners(builder);

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
}