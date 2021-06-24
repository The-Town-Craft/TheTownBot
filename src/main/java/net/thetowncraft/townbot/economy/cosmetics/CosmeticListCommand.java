package net.thetowncraft.townbot.economy.cosmetics;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
import net.thetowncraft.townbot.util.SkinRender;
import org.bukkit.OfflinePlayer;

import java.util.List;

public class CosmeticListCommand extends DiscordCommand {

    private static String getCosmeticsAsString(OfflinePlayer player) {
        String s = "";

        for(Cosmetic cosmetic : CosmeticsManager.getCosmetics(player)) {
            s += "\n" + cosmetic.getName();
        }

        return s;
    }

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

        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor(player.getName() + "'s Cosmetics", null, SkinRender.renderFace(player));
        embed.setDescription(getCosmeticsAsString(player));
        embed.setColor(event.getMember().getColor());
        event.getChannel().sendMessage(embed.build()).queue();
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
