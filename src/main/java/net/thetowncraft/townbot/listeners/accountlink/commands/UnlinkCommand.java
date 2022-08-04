package net.thetowncraft.townbot.listeners.accountlink.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.List;

public class UnlinkCommand extends DiscordCommand {

    @Override
    public void execute(CommandEvent.Discord event) {
        String[] args = event.getArgs();
        if(args.length == 1) {
            event.getChannel().sendMessage("Who's account do you want to unlink?").queue();
            return;
        }

        AccountManager manager = AccountManager.getInstance();

        List<Member> members = event.getMessage().getMentionedMembers();
        if(members.size() == 0) {
            String playerName = args[1];
            OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
            Member member = manager.getDiscordMember(player);
            if(member == null) {
                event.getChannel().sendMessage(":x: **" + playerName + "** is either not linked, or does not exist.").queue();
                return;
            }

            manager.unlink(player);
            event.getChannel().sendMessage(":white_check_mark: **Success**! The Minecraft account **" + player.getName() + "** was unlinked from the Discord account " + member.getAsMention()).queue();
            return;
        }

        for(Member member : members) {
            OfflinePlayer player = manager.getMinecraftPlayer(member);
            if(player == null) {
                event.getChannel().sendMessage(":x: **" + member.getEffectiveName() + "** is not linked to a Minecraft account!").queue();
                continue;
            }
            manager.unlink(member);
            event.getChannel().sendMessage(":white_check_mark: **Success**! The Discord account " + member.getAsMention() + " was unlinked from the Minecraft account **" + player.getName() + "**").queue();
        }
    }

    @Override
    public String getName() {
        return "unlink";
    }

    @Override
    public String getDescription() {
        return "Unlink a player's account!";
    }

    @Override
    public Permission getRequiredPermission() {
        return Permission.MANAGE_SERVER;
    }
}
