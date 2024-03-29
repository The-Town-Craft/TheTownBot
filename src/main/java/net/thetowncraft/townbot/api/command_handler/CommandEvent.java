package net.thetowncraft.townbot.api.command_handler;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.api.command_handler.minecraft.MinecraftCommand;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandEvent {

    public static class Minecraft {

        private final String[] args;
        private final Player player;
        private final MinecraftCommand command;

        public Minecraft(Player player, String[] args, MinecraftCommand command) {
            this.args = args;
            this.player = player;
            this.command = command;
        }

        public String[] getArgs() {
            return args;
        }

        public Player getPlayer() {
            return player;
        }

        public Member getDiscordMember() {
            return AccountManager.getInstance().getDiscordMember(player);
        }

        public MinecraftCommand getCommand() {
            return command;
        }
    }
    public static class Discord {

        private final String[] args;
        private final GuildMessageReceivedEvent event;
        private final Guild guild;
        private final Member member;
        private final Message message;
        private final TextChannel channel;
        private final User author;
        private final JDA jda;
        private final String messageId;
        private final long messageIdLong;
        private final boolean isWebhookMessage;
        private final DiscordCommand command;

        public Discord(GuildMessageReceivedEvent event, DiscordCommand command) {
            this.event = event;
            this.args = event.getMessage().getContentRaw().split("\\s+");
            this.guild = event.getGuild();
            this.message = event.getMessage();
            this.channel = event.getChannel();
            this.member = event.getMember();
            this.author = event.getAuthor();
            this.jda = event.getJDA();
            this.messageId = event.getMessageId();
            this.messageIdLong = event.getMessageIdLong();
            this.isWebhookMessage = event.isWebhookMessage();
            this.command = command;
        }

        public Member getSelfMember() {
            return event.getGuild().getSelfMember();
        }

        public User getSelfUser() {
            return event.getJDA().getSelfUser();
        }

        public String[] getArgs() {
            return args;
        }

        public JavaPlugin getPlugin() {
            return Plugin.get();
        }

        public GuildMessageReceivedEvent getEvent() {
            return event;
        }

        public Guild getGuild() {
            return guild;
        }

        public Member getMember() {
            return member;
        }

        public Message getMessage() {
            return message;
        }

        public TextChannel getChannel() {
            return channel;
        }

        public User getAuthor() {
            return author;
        }

        public JDA getJDA() {
            return jda;
        }

        public String getMessageId() {
            return messageId;
        }

        public long getMessageIdLong() {
            return messageIdLong;
        }

        public boolean isWebhookMessage() {
            return isWebhookMessage;
        }

        public DiscordCommand getCommand() {
            return command;
        }
    }
}