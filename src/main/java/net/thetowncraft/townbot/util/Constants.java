package net.thetowncraft.townbot.util;

import net.thetowncraft.townbot.Bot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

public class Constants {

	public static final Role MOST_ACTIVE_ROLE = Bot.jda.getRoleById("817927959155179540");
	public static final Role TOWN_MEMBER_ROLE = Bot.jda.getRoleById("730982990959869982");
	public static final Role PUBLIC_WORKS_ROLE = Bot.jda.getRoleById("730987270626344971");
	public static final Role LAWYER_ROLE = Bot.jda.getRoleById("780950276597088288");
	public static final Role VICE_MAYOR_ROLE = Bot.jda.getRoleById("730983417952337950");
	public static final Role MAYOR_ROLE = Bot.jda.getRoleById("781585659697037323");
	public static final Role PINGS_CATEGORY_ROLE = Bot.jda.getRoleById("767576207792406568");
	public static final Role OTHER_CATEGORY_ROLE = Bot.jda.getRoleById("767579733331935252");
	public static final Role UNLINKED_ROLE = Bot.jda.getRoleById("815691410165858324");
	public static final Role DEV_ROLE = Bot.jda.getRoleById("819051808650559519");
	public static final TextChannel MOD_CHAT = Bot.jda.getTextChannelById("759179669320237076");
	public static final TextChannel DEV_CHAT = Bot.jda.getTextChannelById("819063742335680512");
	public static final TextChannel ACTIVE_PLAYER_CHAT = Bot.jda.getTextChannelById("817928132652433468");
	public static final String MC_CHAT = "790082482363957278";
	public static final String MC_LOGS = "789927053985972265";
	public static final String ANNOUNCEMENTS = "730976451725099034";
	public static final String MODMAIL = "781421376086999040";
	public static final String TOWN_DISCORD_ID = "730975912320827452";
	public static final Guild THE_TOWN = Bot.jda.getGuildById("730975912320827452");
	public static final String WHITELIST_APP_CHANNEL = "815691992129601537";
	public static final int GREEN = 0x50bb5f;
	public static final int RED = 0xb83838;
	public static final int PURPLE = 0x01d1fa;

}