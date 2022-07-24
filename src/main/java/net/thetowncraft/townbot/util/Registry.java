package net.thetowncraft.townbot.util;

import net.thetowncraft.townbot.api.command_handler.CommandHandler;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.api.command_handler.discord.ExampleCommand;
import net.thetowncraft.townbot.api.command_handler.minecraft.MinecraftCommand;
import net.thetowncraft.townbot.custom_bosses.bosses.*;
import net.thetowncraft.townbot.dimension.DimensionEventListener;
import net.thetowncraft.townbot.economy.commands.*;
import net.thetowncraft.townbot.economy.player_shops.PlayerShopListener;
import net.thetowncraft.townbot.economy.player_shops.commands.DepositDiamonds;
import net.thetowncraft.townbot.economy.player_shops.commands.ShopChestCommand;
import net.thetowncraft.townbot.economy.player_shops.commands.WithdrawDiamonds;
import net.thetowncraft.townbot.economy.shop.ShopManager;
import net.thetowncraft.townbot.hub.HubCommand;
import net.thetowncraft.townbot.custom_items.ItemDropListener;
import net.thetowncraft.townbot.listeners.accountlink.LinkAccount;
import net.thetowncraft.townbot.listeners.discord.commands.AddActivityPointsCommand;
import net.thetowncraft.townbot.listeners.discord.commands.*;
import net.thetowncraft.townbot.listeners.minecraft.commands.*;
import net.thetowncraft.townbot.listeners.minecraft.chat.*;
import net.thetowncraft.townbot.custom_items.CustomItemListener;
import net.thetowncraft.townbot.listeners.minecraft.player_activity.PlayerJoin;
import net.thetowncraft.townbot.listeners.discord.DiscordChatListener;
import net.thetowncraft.townbot.listeners.discord.MemberJoin;
import net.thetowncraft.townbot.listeners.discord.ServerStart;
import net.thetowncraft.townbot.listeners.discord.fun.Skin;
import net.thetowncraft.townbot.listeners.minecraft.player_activity.afk.AFKListener;
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

                //General Commands
                new ExampleCommand(),
                new Help(),
                new DiscordActiveCommand(),
                new OnlinePlayers(),
                new Skin(),

                //Staff Commands
                new ModMail(),
                new Ban(),
                new Unban(),
                new BanList(),
                new DiscordMute(),
                new DiscordUnmute(),
                new Log(),
                new Whitelist(),
                new RunMCCommand(),
                new ChangelogCommand(),

                //Activity Points
                new AddActivityPointsCommand(),
                new SubtractActivityPointsCommand(),

                //Economy
                new AddCoins(),
                new Bal(),
                new BalTop(),
                new SetCoins(),
                new SubtractCoins(),
                new Pay.Discord(),
                new TodoCommand()
        );
    }

    public static void registerMinecraftCommands() {
        MinecraftCommand.registerCommands(
                new Pay.Minecraft(),
                new Bal.MC(),
                new BalTop.MC(),
                new ShopChestCommand(),
                new DepositDiamonds(),
                new WithdrawDiamonds(),
                new GiveCustomItem(),
                new MaintenanceCommand(),
                new HubCommand(),
                new DonateCommand()
        );
    }

    public static void registerCosmetics() {
        ShopManager.initShop();
    }

    public static void registerJDAListeners(JDABuilder builder) {
        builder.addEventListeners(new CommandHandler.Discord());
        builder.addEventListeners(new DiscordChatListener());
        builder.addEventListeners(new ModMailListener());
        builder.addEventListeners(new MemberJoin());
        builder.addEventListeners(new LinkAccount());
        builder.addEventListeners(new ServerStart());
    }
    public static void registerSpigotListeners(JavaPlugin plugin) {
        registerSpigotListeners(plugin,
                new RuleReminders(),
                new WitherSpawn(),
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
                new WorldChange(),
                new PlayerShopListener(),
                new CustomItemListener(),
                new ItemDropListener(),
                new DimensionEventListener(),
                new MaintenanceListener(),

                //Bosses
                new WickedHunterBoss(),
                new BlazingWitherBoss(),
                new NoxiousChickenBoss(),
                new IllusionerBoss(),
                new IceologerBoss()
        );
    }
    private static void registerSpigotListeners(JavaPlugin plugin, Listener... listeners) {
        for(Listener listener : listeners) Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
    }
}
