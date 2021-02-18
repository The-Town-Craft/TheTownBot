package com.cadenkoehl.minecordbot;

import javax.security.auth.login.LoginException;

import com.cadenkoehl.minecordbot.listeners.accountlink.LinkAccount;
import com.cadenkoehl.minecordbot.listeners.accountlink.LinkCheck;
import com.cadenkoehl.minecordbot.listeners.discord.*;
import com.cadenkoehl.minecordbot.listeners.discord.commands.*;
import com.cadenkoehl.minecordbot.listeners.discord.fun.Skin;
import com.cadenkoehl.minecordbot.listeners.minecraft.*;
import com.cadenkoehl.minecordbot.listeners.minecraft.commands.McMute;
import com.cadenkoehl.minecordbot.listeners.minecraft.commands.McUnmute;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.bukkit.plugin.java.JavaPlugin;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

public class MinecordBot extends JavaPlugin {
	
    public static JDA jda;
    public static String prefix = "/";

    @Override
    public void onEnable() {
    	
        JDABuilder builder = JDABuilder.createDefault(Constants.TOKEN);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.playing("The Town SMP!"));
        
        // JDA Listeners

        builder.addEventListeners(new DiscordChatListener());
        builder.addEventListeners(new OnlinePlayers());
        builder.addEventListeners(new Whitelist());
        builder.addEventListeners(new Ban());
        builder.addEventListeners(new ModMail());
        builder.addEventListeners(new LinkAccount());
        builder.addEventListeners(new Skin());
        builder.addEventListeners(new Log());
        builder.addEventListeners(new DiscordMute());
        builder.addEventListeners(new DiscordUnmute());
        builder.addEventListeners(new ServerStart());
        builder.addEventListeners(new Help());
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        builder.setChunkingFilter(ChunkingFilter.ALL);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);

        // Spigot Listeners

        getServer().getPluginManager().registerEvents(new Raid(), this);
        getServer().getPluginManager().registerEvents(new MinecraftChatListener(), this);
        getServer().getPluginManager().registerEvents(new CommandLog(), this);
        getServer().getPluginManager().registerEvents(new Advancements(), this);
        getServer().getPluginManager().registerEvents(new Enchants(), this);
        getServer().getPluginManager().registerEvents(new Sleep(), this);
        getServer().getPluginManager().registerEvents(new McMute(), this);
        getServer().getPluginManager().registerEvents(new McUnmute(), this);
        getServer().getPluginManager().registerEvents(new LinkCheck(), this);
        getServer().getPluginManager().registerEvents(new WitherSpawn(), this);

        try {
           MinecordBot.jda = builder.build();
        }
        catch (LoginException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onDisable() {
        jda.shutdownNow();
    }
}