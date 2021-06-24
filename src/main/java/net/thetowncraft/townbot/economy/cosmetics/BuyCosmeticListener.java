package net.thetowncraft.townbot.economy.cosmetics;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.thetowncraft.townbot.economy.EconomyManager;
import net.thetowncraft.townbot.listeners.accountlink.AccountManager;
import net.thetowncraft.townbot.util.Constants;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class BuyCosmeticListener extends ListenerAdapter {

    private static final List<String> buyCooldown = new ArrayList<>();
    private static Map<String, String> buyers = new HashMap<>();

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
        Member member = event.getMember();
        if(member.getUser().isBot()) return;

        TextChannel channel = Constants.SHOP_CHANNEL;
        assert channel != null;
        if(event.getChannel().getId().equals(channel.getId())) {
            MessageReaction.ReactionEmote emote = event.getReaction().getReactionEmote();
            if(emote.isEmoji() && emote.getEmoji().equals(CosmeticsManager.BUY_EMOJI)) {

                event.getReaction().removeReaction(member.getUser()).queue();
                if(buyers.containsKey(member.getId())) return;
                if(buyCooldown.contains(member.getId())) {
                    return;
                }

                buyCooldown.add(member.getId());

                OfflinePlayer player = AccountManager.getInstance().getMinecraftPlayer(member);
                if(player == null) {
                    error(":x: " + member.getAsMention() + " your account must be linked for you to purchase cosmetics!", event, 20);
                    return;
                }

                Cosmetic cosmetic = CosmeticsManager.getCosmeticByShopMessage(event.getMessageId());
                if(cosmetic == null) {
                    error(":x: " + member.getAsMention() + " something went wrong while trying to purchase that cosmetic! Please contact ModMail about this issue!", event, 5);
                    return;
                }

                int bal = EconomyManager.getCoinBalance(player);

                if(cosmetic.getPrice() > bal) {
                    error(":x: " + member.getAsMention() + " you need " + (cosmetic.getPrice() - bal) + " more coins to purchase the **" + cosmetic.getName() + "** cosmetic!", event);
                    return;
                }

                if(CosmeticsManager.getCosmetics(player).contains(cosmetic)) {
                    error(":x: " + member.getAsMention() + " you have already purchased the **" + cosmetic.getName() + "** cosmetic!", event);
                    return;
                }

                channel.sendMessage(member.getAsMention() + " are you sure you want to purchase the **" + cosmetic.getName() + "** cosmetic for " + cosmetic.getPrice() + " coins?").queue(message -> {
                    message.addReaction(Constants.CHECK_MARK).queue();

                    buyers.put(member.getId(), cosmetic.id);

                    message.delete().queueAfter(10, TimeUnit.SECONDS);

                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            buyCooldown.remove(event.getMember().getId());
                            buyers.remove(member.getId());
                        }
                    }, 10000L);
                });
            }
            if(emote.getEmoji().equals(Constants.CHECK_MARK)) {
                String cosmeticId = buyers.get(member.getId());
                if(cosmeticId == null) {
                    event.getReaction().removeReaction(member.getUser()).queue();
                    return;
                }

                OfflinePlayer player = AccountManager.getInstance().getMinecraftPlayer(member);
                if(player == null) {
                    event.getReaction().removeReaction(member.getUser()).queue();
                    return;
                }

                Cosmetic cosmetic = CosmeticsManager.getCosmetic(cosmeticId);

                CosmeticsManager.purchaseCosmetic(player, cosmetic);
                Constants.SHOP_CHANNEL.sendMessage(":white_check_mark: " + member.getAsMention() + " you have successfully purchased the **" + cosmetic.getName() + "** cosmetic!").queue(message -> {
                    message.delete().queueAfter(6, TimeUnit.SECONDS);
                });
            }
        }
    }

    private void error(String errMessage, GuildMessageReactionAddEvent event, int seconds) {
        Constants.SHOP_CHANNEL.sendMessage(errMessage).queue(message -> {
            message.delete().queueAfter(8, TimeUnit.SECONDS);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    buyCooldown.remove(event.getMember().getId());
                }
            }, seconds * 1000L);
        });
    }

    private void error(String errMessage, GuildMessageReactionAddEvent event) {
        error(errMessage, event, 8);
    }
}
