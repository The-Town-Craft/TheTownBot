
package net.thetowncraft.townbot.util;

import com.sun.imageio.plugins.jpeg.JPEGStreamMetadataFormat;
import net.dv8tion.jda.api.entities.*;
import net.thetowncraft.townbot.Bot;
import org.bukkit.ChatColor;

public class Constants {

	public static final User CADEN = Bot.jda.getUserById("585334397914316820");

	public static final Role DONATOR_ROLE = Bot.jda.getRoleById("803502592297664533");
	public static final Role ACTIVE_PLAYER_ROLE = Bot.jda.getRoleById("817927959155179540");
	public static final Role INACTIVE_PLAYER_ROLE = Bot.jda.getRoleById("781282435701669900");
	public static final Role BUSY_ROLE = Bot.jda.getRoleById("1005718223666282547");
	public static final Role TOWN_MEMBER_ROLE = Bot.jda.getRoleById("730982990959869982");
	public static final Role MEMBER_ROLE = Bot.jda.getRoleById("779463332645830686");
	public static final Role PUBLIC_WORKS_ROLE = Bot.jda.getRoleById("730987270626344971");
	public static final Role LAWYER_ROLE = Bot.jda.getRoleById("780950276597088288");
	public static final Role VICE_MAYOR_ROLE = Bot.jda.getRoleById("730983417952337950");
	public static final Role MAYOR_ROLE = Bot.jda.getRoleById("781585659697037323");
	public static final Role PINGS_CATEGORY_ROLE = Bot.jda.getRoleById("767576207792406568");
	public static final Role PURCHASES_CATEGORY_ROLE = Bot.jda.getRoleById("1001355363293073518");
	public static final Role OTHER_CATEGORY_ROLE = Bot.jda.getRoleById("767579733331935252");
	public static final Role BOSSES_CATEGORY_ROLE = Bot.jda.getRoleById("995754478609375363");
	public static final Role UNLINKED_ROLE = Bot.jda.getRoleById("815691410165858324");
	public static final Role DEV_ROLE = Bot.jda.getRoleById("819051808650559519");
	public static final Role STAFF_ROLE = Bot.jda.getRoleById("793254607526428673");
	public static final Role ADMIN_ROLE = Bot.jda.getRoleById("819611983396601856");
	public static final TextChannel MOD_CHAT = Bot.jda.getTextChannelById("759179669320237076");
	public static final TextChannel DEV_CHAT = Bot.jda.getTextChannelById("819063742335680512");
	public static final TextChannel CHANGELOGS = Bot.jda.getTextChannelById("995567151345836052");
	public static final TextChannel COINS_CHANNEL = Bot.jda.getTextChannelById("994044489654075453");
	public static final Role CHANGELOG_PING = Bot.jda.getRoleById("774074418942050324");
	public static final TextChannel ACTIVE_PLAYER_CHAT = Bot.jda.getTextChannelById("817928132652433468");
	public static final VoiceChannel PLAYER_COUNT_CHANNEL = Bot.jda.getVoiceChannelById("1005960975352070196");
	public static final Category COMMUNITY_CATEGORY = Bot.jda.getCategoryById("767522736820977674");
	public static final String THETOWN_CHAT = "755499624810283119";
	public static final String MC_CHAT = "790082482363957278";
	public static final String MC_LOGS = "789927053985972265";
	public static final String ANNOUNCEMENTS = "821378395139997757";
	public static final String MODMAIL = "781421376086999040";
	public static final String TOWN_DISCORD_ID = "730975912320827452";
	public static final String SHOP_ID = "994044489654075453";
	public static final Guild THE_TOWN = Bot.jda.getGuildById("730975912320827452");
	public static final String WHITELIST_APP_CHANNEL = "815691992129601537";
	public static final String CHECK_MARK = "✅";
	public static final String X = "❌";
	public static final int GREEN = 0x50bb5f;
	public static final int RED = 0xb83838;
	public static final int PURPLE = 0x01d1fa;

}