package com.example.jbw.go4fresh;

public class ProductInfo {
    private String id;
    private String itemName;
    private String itemType;
    private String itemQuantity;
    private String itemQuality;
    private String minPrice;
    private String userId;
    public ProductInfo() {}
    public ProductInfo(String id, String itemName, String itemType, String itemQuantity, String itemQuality, String minPrice, String userId) {
        this.id = id;
        this.itemName = itemName;
        this.itemType = itemType;
        this.itemQuantity = itemQuantity;
        this.itemQuality = itemQuality;
        this.minPrice = minPrice;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public String getItemQuality() {
        return itemQuality;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public String getUserId() {
        return userId;
    }
}
