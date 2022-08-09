package net.thetowncraft.townbot.factions.teams;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.thetowncraft.townbot.Bot;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
import net.thetowncraft.townbot.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Team {

    private Material material;
    private final String roleId;
    private final Role role;
    private final String textChannelId;
    private final TextChannel channel;
    private UUID leaderUUID;
    private final List<String> invites;

    Team(Material material, String roleId, String textChannelId, UUID leaderUUID) {
        this.material = material;
        this.roleId = roleId;
        this.role = Bot.jda.getRoleById(roleId);
        this.textChannelId = textChannelId;
        this.channel = Bot.jda.getTextChannelById(textChannelId);
        this.leaderUUID = leaderUUID;
        this.invites = new ArrayList<>();
    }

    public void add(Member member) {
        member.getGuild().addRoleToMember(member, role).queue();
        OfflinePlayer player = AccountManager.getInstance().getMinecraftPlayer(member);
        if(player != null) invites.remove(player.getUniqueId().toString());
        channel.sendMessage(":partying_face: " + member.getAsMention() + " joined the team!").queue();
    }

    public void add(OfflinePlayer player) {
        Member member = AccountManager.getInstance().getDiscordMember(player);
        if(member == null) return;
        add(member);
    }

    public void remove(OfflinePlayer player) {
        Member member = AccountManager.getInstance().getDiscordMember(player);
        if(member == null) return;
        remove(member);
    }

    public void remove(Member member) {
        member.getGuild().removeRoleFromMember(member, role).queue();
        channel.sendMessage(":sob: " + member.getAsMention() + " left the team. Come back soon!").queue();
    }

    public List<Member> getMembers() {
        return role.getGuild().getMembersWithRoles(role);
    }

    public void setLeaderUUID(UUID leaderUUID) {
        this.leaderUUID = leaderUUID;
    }

    public UUID getLeaderUUID() {
        return leaderUUID;
    }

    public void setLeader(OfflinePlayer player) {
        setLeaderUUID(player.getUniqueId());
    }

    public OfflinePlayer getLeader() {
        return Bukkit.getOfflinePlayer(leaderUUID);
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public String getName() {
        return Teams.getName(material);
    }

    public Material getMaterial() {
        return material;
    }

    public Role getRole() {
        return role;
    }

    public String getRoleId() {
        return roleId;
    }

    public String getChannelId() {
        return textChannelId;
    }

    public TextChannel getChannel() {
        return channel;
    }

    public List<String> getInvites() {
        return invites;
    }

    public void createInvite(OfflinePlayer player) {
        invites.add(player.getUniqueId().toString());
    }
}
