package com.cadenkoehl.minecordbot;

import javax.security.auth.login.LoginException;

import com.cadenkoehl.minecordbot.listeners.discord.*;
import org.bukkit.plugin.java.JavaPlugin;

import com.cadenkoehl.minecordbot.listeners.minecraft.Advancements;
import com.cadenkoehl.minecordbot.listeners.minecraft.CommandLog;
import com.cadenkoehl.minecordbot.listeners.minecraft.Enchants;
import com.cadenkoehl.minecordbot.listeners.minecraft.MinecraftChatListener;
import com.cadenkoehl.minecordbot.listeners.minecraft.Sleep;
import com.cadenkoehl.minecordbot.listeners.minecraft.SuperVanishCompat;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

public class MinecordBot extends JavaPlugin {
	
    public static JDA jda;
    public static String prefix = "/";
    //public static DataManager data;
   
    @Override
    public void onEnable() {
    	
        JDABuilder builder = JDABuilder.createDefault(Constants.token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.playing("The Town SMP!"));
        
        // JDA Listeners

        builder.addEventListeners(new DiscordChatListener());
        builder.addEventListeners(new OnlinePlayers());
        builder.addEventListeners(new Whitelist());
        builder.addEventListeners(new WhitelistRemove());
        builder.addEventListeners(new Ban());
        builder.addEventListeners(new Unban());
        builder.addEventListeners(new ModMail());
        builder.addEventListeners(new Apply());
        builder.addEventListeners(new JoinServer());
        
        // Spigot Listeners

        getServer().getPluginManager().registerEvents(new MinecraftChatListener(), this);
        getServer().getPluginManager().registerEvents(new CommandLog(), this);
        getServer().getPluginManager().registerEvents(new SuperVanishCompat(), this);
        getServer().getPluginManager().registerEvents(new Advancements(), this);
        getServer().getPluginManager().registerEvents(new Enchants(), this);
        getServer().getPluginManager().registerEvents(new Sleep(), this);

        try {
           MinecordBot.jda = builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onDisable() {
        jda.shutdownNow();
    }
}