package net.thetowncraft.townbot.economy.cosmetics;

import net.dv8tion.jda.api.Permission;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
import org.bukkit.OfflinePlayer;

import java.util.List;

public class CosmeticListCommand extends DiscordCommand {

    @Override
    public void execute(CommandEvent.Discord event) {
        OfflinePlayer player = AccountManager.getInstance().getMinecraftPlayer(event.getMember());
        if(player == null) {
            event.getChannel().sendMessage(":x: You have not purchased any cosmetics yet!").queue();
            return;
        }

        List<Cosmetic> cosmetics = CosmeticsManager.getCosmetics(player);
        if(cosmetics.size() == 0) {
            event.getChannel().sendMessage(":x: You have not purchased any cosmetics yet!").queue();
            return;
        }
    }

    @Override
    public String getName() {
        return "cosmetics";
    }

    @Override
    public String getDescription() {
        return "See a list of all your cosmetics!";
    }

    @Override
    public Permission getRequiredPermission() {
        return null;
    }
}
