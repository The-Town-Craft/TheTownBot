package net.thetowncraft.townbot.listeners.accountlink.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;

public class AccountsCommand extends DiscordCommand {

    @Override
    public void execute(CommandEvent.Discord event) {
        for(EmbedBuilder embed : AccountManager.getInstance().getAccountsEmbeds()) {
            event.getChannel().sendMessage(embed.build()).queue();
        }
    }

    @Override
    public String getName() {
        return "accounts";
    }

    @Override
    public String getDescription() {
        return "View all linked accounts.";
    }

    @Override
    public Permission getRequiredPermission() {
        return null;
    }
}
