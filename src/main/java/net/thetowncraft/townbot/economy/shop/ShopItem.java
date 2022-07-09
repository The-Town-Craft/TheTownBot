package net.thetowncraft.townbot.economy.shop;

public class ShopItem {

    private final String name;
    private final String desc;
    private final String image;
    private final int price;
    private final int roleId;
    private String messageId;


    public ShopItem(String name, String desc, String image, int price, int roleId) {
        this.name = name;
        this.desc = desc;
        this.image = image;
        this.price = price;
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getImage() {
        return image;
    }

    public int getPrice() {
        return price;
    }

    public int getRoleId() {
        return roleId;
    }

    public String getMessageId() {
        return messageId;
    }
}
