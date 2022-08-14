package net.thetowncraft.townbot.factions.teams.commands;

import net.dv8tion.jda.api.entities.Member;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.minecraft.MinecraftCommand;
import net.thetowncraft.townbot.factions.economy.shop.ShopManager;
import net.thetowncraft.townbot.factions.teams.Team;
import net.thetowncraft.townbot.factions.teams.Teams;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class TeamCommandMinecraft extends MinecraftCommand {

    @Override
    public void execute(CommandEvent.Minecraft event) {
        String[] args = event.getArgs();
        Player player = event.getPlayer();

        Member member = event.getDiscordMember();
        if(member == null) {
            player.sendMessage(ChatColor.RED + "Your account is not linked!");
            return;
        }
        if(args.length == 0) {
            player.sendMessage(getUsage());
            return;
        }

        switch (args[0]) {
            case "create": {

                if(!ShopManager.getPurchases(member).contains(ShopManager.TEAMS)) {
                    player.sendMessage(ChatColor.GREEN + "Type \"?buy Team Creation Ticket\" on the Discord to create a team!");
                    return;
                }

                String output = TeamCommands.create(args, player);
                if (output == null) {
                    player.sendMessage(ChatColor.GREEN + "Success! Team was created!");
                    member.getGuild().removeRoleFromMember(member, ShopManager.TEAMS.getRole()).queue();
                }
                else player.sendMessage(getErrorString(output));
                break;
            }
            case "list": {
                player.sendMessage(Teams.getMcChatView());
                break;
            }
            case "view": {
                if(args.length == 1) {
                    Team team = Teams.getTeam(player);
                    if(team == null) {
                        player.sendMessage(getUsage());
                        return;
                    }
                    player.sendMessage(team.getMcChatDisplay());
                }
                else {
                    String teamName = Arrays.stream(args).skip(1).collect(Collectors.joining(" "));
                    Team team = Teams.getByName(teamName);
                    if(team == null) {
                        player.sendMessage(ChatColor.RED + "Error! Could not find a team by the name of " + teamName);
                        return;
                    }
                    player.sendMessage(team.getMcChatDisplay());
                }
                break;
            }
            case "invite": {
                OfflinePlayer invited = getReferencedPlayer(args, event);
                if (invited == null) {
                    player.sendMessage(ChatColor.RED + args[1] + "'s account is not linked!");
                    return;
                }
                String output = TeamCommands.invite(args, invited, player);
                if (output == null) player.sendMessage(ChatColor.GREEN + "Success! " + invited.getName() + " was invited!");
                else player.sendMessage(getErrorString(output));
                break;
            }
            case "join": {
                String output = TeamCommands.join(args, player);
                if (output == null) player.sendMessage(ChatColor.GREEN + "Success! You were added to the team!");
                else player.sendMessage(getErrorString(output));
                break;
            }
            case "leave": {
                String output = TeamCommands.leave(args, player);
                if(output == null) player.sendMessage(ChatColor.GREEN + "Success! You have left the team!");
                else player.sendMessage(getErrorString(output));
                break;
            }
            case "kick": {
                OfflinePlayer kicked = getReferencedPlayer(args, event);
                if (kicked == null) return;
                String output = TeamCommands.kick(args, kicked, player);
                if (output == null) player.sendMessage(ChatColor.GREEN + "Success! " + kicked.getName() + " was kicked!");
                else player.sendMessage(getErrorString(output));
                break;
            }
            case "color": {
                String output = TeamCommands.color(args, player);
                if (output == null) player.sendMessage(ChatColor.GREEN + "Success! The team color was changed!");
                else player.sendMessage(getErrorString(output));
                break;
            }
            case "transfer": {
                OfflinePlayer to = getReferencedPlayer(args, event);
                if (to == null) return;
                String output = TeamCommands.transfer(args, player, to);
                if (output == null) player.sendMessage(ChatColor.GREEN + "Success! Team was transferred to " + to.getName() + "!");
                else player.sendMessage(getErrorString(output));
                break;
            }
            case "delete": {
                String output = TeamCommands.delete(args, player);
                if (output == null) player.sendMessage(ChatColor.GREEN + "Success! Team was deleted!");
                else player.sendMessage(getErrorString(output));
                break;
            }
            default: {
                player.sendMessage(getUsage());
                break;
            }
        }
    }

    public String getErrorString(String output) {
        return ChatColor.RED + output.replace("{usage}", getUsage()).replace("*", "");
    }

    public OfflinePlayer getReferencedPlayer(String[] args, CommandEvent.Minecraft event) {
        Player sender = event.getPlayer();
        if(args.length == 1) {
            sender.sendMessage(getUsage());
            return null;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
        if(!AccountManager.getInstance().isLinked(player)) return null;
        return player;
    }

    public String getUsage() {
        return ChatColor.RED + "" + ChatColor.BOLD + "\nIncorrect Usage" +
                ChatColor.RESET + "" + ChatColor.GRAY +
                "\n/team create <material name>" +
                "\n/team list" +
                "\n/team view <team>" +
                "\n/team invite <player>" +
                "\n/team join <team>"  +
                "\n/team leave"  +
                "\n/team kick <player>"  +
                "\n/team color <hex>"  +
                "\n/team transfer <player>"  +
                "\n/team delete";
    }

    public List<String> getSubCommands() {
        List<String> cmds = new ArrayList<>();
        cmds.add("create");
        cmds.add("list");
        cmds.add("view");
        cmds.add("invite");
        cmds.add("join");
        cmds.add("leave");
        cmds.add("kick");
        cmds.add("color");
        cmds.add("transfer");
        cmds.add("delete");
        return cmds;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> list = new ArrayList<>();

        if(args.length == 1) {
            String search = args[0];
            if(search.isEmpty()) return getSubCommands();

            for(String subcmd : getSubCommands()) {
                if(subcmd.toUpperCase().contains(search.toUpperCase())) {
                    list.add(subcmd);
                }
            }
            return list;
        }
        else if(args.length == 2) {
            String subCmd = args[0];
            String search = args[1];
            if(subCmd.equals("create")) {
                if(search.isEmpty()) {
                    for(Material material : Material.values()) {
                        list.add(material.name().toLowerCase());
                    }
                    return list;
                }
                for(Material material : Material.values()) {
                    if(material.name().toUpperCase().contains(search.toUpperCase())) list.add(material.name().toLowerCase());
                }
                return list;
            }
            else if(subCmd.equals("view")) {
                return list;
            }
            else return list;
        }
        else {
            return list;
        }
    }

    @Override
    public boolean isAdminCommand() {
        return false;
    }

    @Override
    public String getName() {
        return "team";
    }
}
