package net.thetowncraft.townbot.listeners.discord.commands;

import net.dv8tion.jda.api.Permission;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
import net.thetowncraft.townbot.listeners.minecraft.player_activity.active.ActivityManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class AddActivityPointsCommand extends DiscordCommand {

    @Override
    public void execute(CommandEvent.Discord event) {

        String[] args = event.getArgs();
        if(args.length < 3) {
            event.getChannel().sendMessage(":x: Incomplete command! Usage: `/" + getName() + "` `playername` `<amount>`").queue();
            return;
        }

        if(event.getMessage().getMentionedMembers().size() != 0) {
            event.getChannel().sendMessage(":x: Please use a Minecraft player's username, instead of a Discord @mention.").queue();
            return;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
        if(AccountManager.getInstance().getDiscordMember(player) == null) {
            event.getChannel().sendMessage("The player **" + args[1] + "**'s account is either not linked, or does not exist.").queue();
            return;
        }

        int amount;

        try {
            amount = Math.abs(Integer.parseInt(args[2]));
        } catch (NumberFormatException e) {
            event.getChannel().sendMessage(":x: Please provide a valid integer for argument 3.").queue();
            return;
        }

        String uuid = player.getUniqueId().toString();

        ActivityManager.PLAYER_ACTIVITY_MAP.computeIfAbsent(uuid, k -> {
            event.getChannel().sendMessage(":white_check_mark: **Success**! Added " + amount + " Activity Points to " + player.getName() + "! (new value is " + amount + ")").queue();
            return (long) amount;
        });
        Long currentPoints = ActivityManager.PLAYER_ACTIVITY_MAP.get(uuid);

        long newVal = currentPoints + amount;

        ActivityManager.PLAYER_ACTIVITY_MAP.put(uuid, newVal);
        event.getChannel().sendMessage(":white_check_mark: **Success**! Added " + amount + "Activity Points to " + player.getName() + "! (new value is " + newVal + ")").queue();
    }

    @Override
    public String getName() {
        return "addactivity";
    }

    @Override
    public String getDescription() {
        return "Add activity points to a player!";
    }

    @Override
    public Permission getRequiredPermission() {
        return Permission.MANAGE_SERVER;
    }
}
