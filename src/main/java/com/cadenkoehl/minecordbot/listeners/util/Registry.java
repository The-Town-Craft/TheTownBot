package com.cadenkoehl.minecordbot.listeners.util;

import com.cadenkoehl.minecordbot.listeners.accountlink.Application;
import com.cadenkoehl.minecordbot.listeners.accountlink.LinkAccount;
import com.cadenkoehl.minecordbot.listeners.minecraft.commands.ActiveCommand;
import com.cadenkoehl.minecordbot.listeners.minecraft.player_activity.PlayerJoin;
import com.cadenkoehl.minecordbot.listeners.discord.DiscordChatListener;
import com.cadenkoehl.minecordbot.listeners.discord.MemberJoin;
import com.cadenkoehl.minecordbot.listeners.discord.ServerStart;
import com.cadenkoehl.minecordbot.listeners.discord.commands.*;
import com.cadenkoehl.minecordbot.listeners.discord.fun.Skin;
import com.cadenkoehl.minecordbot.listeners.minecraft.chat.*;
import com.cadenkoehl.minecordbot.listeners.minecraft.player_activity.afk.AFKListener;
import com.cadenkoehl.minecordbot.listeners.minecraft.commands.McMute;
import com.cadenkoehl.minecordbot.listeners.minecraft.commands.McUnmute;
import net.dv8tion.jda.api.JDABuilder;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Registry {
    public static void registerJDAListeners(JDABuilder builder) {
        builder.addEventListeners(new DiscordChatListener());
        builder.addEventListeners(new OnlinePlayers());
        builder.addEventListeners(new Whitelist());
        builder.addEventListeners(new Ban());
        builder.addEventListeners(new ModMail());
        builder.addEventListeners(new MemberJoin());
        builder.addEventListeners(new LinkAccount());
        builder.addEventListeners(new Skin());
        builder.addEventListeners(new Log());
        builder.addEventListeners(new DiscordMute());
        builder.addEventListeners(new DiscordUnmute());
        builder.addEventListeners(new ServerStart());
        builder.addEventListeners(new Application());
        builder.addEventListeners(new Help());
        builder.addEventListeners(new DiscordActiveCommand());
    }
    public static void registerSpigotListeners(JavaPlugin plugin) {
        registerSpigotListeners(plugin,
                new RuleReminders(),
                new WitherSpawn(),
                new PlayerCountStatus(),
                new PlayerJoin(),
                new McUnmute(),
                new McMute(),
                new Sleep(),
                new Enchants(),
                new Advancements(),
                new CommandLog(),
                new MinecraftChatListener(),
                new Raid(),
                new AFKListener(),
                new ActiveCommand(),
                new WorldChange()
        );
    }
    private static void registerSpigotListeners(JavaPlugin plugin, Listener... listeners) {
        for(Listener listener : listeners) Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
    }
}
