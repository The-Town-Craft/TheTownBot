package net.thetowncraft.townbot.util;

import net.thetowncraft.townbot.api.command_handler.CommandHandler;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.api.command_handler.discord.TestCommand;
import net.thetowncraft.townbot.api.command_handler.minecraft.MinecraftCommand;
import net.thetowncraft.townbot.listeners.accountlink.LinkAccount;
import net.thetowncraft.townbot.listeners.discord.commands.*;
import net.thetowncraft.townbot.listeners.minecraft.chat.*;
import net.thetowncraft.townbot.listeners.minecraft.commands.ActiveCommand;
import net.thetowncraft.townbot.listeners.minecraft.player_activity.PlayerJoin;
import net.thetowncraft.townbot.listeners.discord.DiscordChatListener;
import net.thetowncraft.townbot.listeners.discord.MemberJoin;
import net.thetowncraft.townbot.listeners.discord.ServerStart;
import net.thetowncraft.townbot.listeners.discord.fun.Skin;
import net.thetowncraft.townbot.listeners.minecraft.player_activity.afk.AFKListener;
import net.thetowncraft.townbot.listeners.minecraft.commands.McMute;
import net.thetowncraft.townbot.listeners.minecraft.commands.McUnmute;
import net.dv8tion.jda.api.JDABuilder;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A class to help with the registry of commands and listeners
 */
public class Registry {

    public static void registerDiscordCommands() {
        DiscordCommand.registerCommands(

                new TestCommand()

        );
    }

    public static void registerMinecraftCommands() {
        MinecraftCommand.registerCommands(


        );
    }

    public static void registerJDAListeners(JDABuilder builder) {
        builder.addEventListeners(new CommandHandler.Discord());
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
        builder.addEventListeners(new Help());
        builder.addEventListeners(new DiscordActiveCommand());
    }
    public static void registerSpigotListeners(JavaPlugin plugin) {
        registerSpigotListeners(plugin,
                new CommandHandler.Minecraft(),
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
