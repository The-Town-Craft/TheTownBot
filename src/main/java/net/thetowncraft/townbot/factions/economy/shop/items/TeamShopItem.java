package net.thetowncraft.townbot.factions.economy.shop.items;

import net.thetowncraft.townbot.factions.economy.shop.ShopItem;
import net.thetowncraft.townbot.util.Constants;

import java.awt.*;

public class TeamShopItem extends ShopItem {

    @Override
    public String getName() {
        return "Team Creation Ticket";
    }

    @Override
    public String getDesc() {
        return ":white_check_mark: Create your own highly customizable team/group on the server, where you can invite up to five people! \n" +
                ":crossed_swords: Fight battles, and wage war between teams! (If you want to of course!)\n " +
                ":moneybag: Many more features are coming to teams very soon. Once it is fully finished, the price will be doubled, so purchase it fast!\n" +
                "*You will have to purchase a new ticket for each team you create.*\n" +
                "*You can only be in one team at a time.*\n";
    }

    @Override
    public String getImage() {
        return "https://cdn.discordapp.com/attachments/819063742335680512/1006779430288506900/unknown.png";
    }

    @Override
    public Color getColor() {
        return Color.GREEN;
    }

    @Override
    public int getPrice() {
        return 1000;
    }

    @Override
    public String getRoleId() {
        return "1006779707804614696";
    }
}
