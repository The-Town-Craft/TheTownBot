package net.thetowncraft.townbot.listeners.discord.commands;

import net.dv8tion.jda.api.Permission;
import net.thetowncraft.townbot.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.util.Constants;
import net.thetowncraft.townbot.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * This command does NOT need updating when new commands are added! It automatically gets commands from the List of DiscordCommands and displays them in /help!
 */
public class Help extends DiscordCommand {
    @Override
    public void execute(CommandEvent.Discord event) {
        String[] args = event.getArgs();

        if(args.length == 1) {

            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Constants.GREEN);

            embed.setTitle("List of Commands");

            for(DiscordCommand cmd : DiscordCommand.COMMANDS) {
                embed.appendDescription("`" + Bot.prefix + cmd.getName() + "` - " + cmd.getDescription() + "\n");
            }

            embed.appendDescription("\n**Type** `" + Bot.prefix + "help` `[command name]` **for more info about a command!**");

            event.getChannel().sendMessage(embed.build()).queue();
        }
        else {

            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Constants.GREEN);

            DiscordCommand cmd = DiscordCommand.getByName(args[1]);
            if(cmd == null) {
                event.getChannel().sendMessage(":x: Could not find a command by the name of \"" + args[1] + "\"").queue();
                return;
            }
            embed.setAuthor(Utils.capFirstLetter(cmd.getName()));
            embed.addField("Description", cmd.getDescription(), false);
            embed.addField("Aliases", Arrays.toString(cmd.getAliases()), false);
            Permission perm = cmd.getRequiredPermission();
            if(perm != null) embed.addField("Required Permission", perm.getName(), false);

            event.getChannel().sendMessage(embed.build()).queue();
        }
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "The command you just used!";
    }

    @Override
    public Permission getRequiredPermission() {
        return null;
    }
}
