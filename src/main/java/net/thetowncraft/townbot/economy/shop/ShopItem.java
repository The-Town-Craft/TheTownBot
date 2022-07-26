package net.thetowncraft.townbot.economy.shop;

public class ShopItem {

    private final String name;
    private final String desc;
    private final String image;
    private final int price;
    private final String roleId;
    private String messageId;


    public ShopItem(String name, String desc, String image, int price, String roleId) {
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

    public String getRoleId() {
        return roleId;
    }

    public String getMessageId() {
        return messageId;
    }
}
