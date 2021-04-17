package com.example.jbw.go4fresh;
public class CostInfo {

    private String bFirstName;
    private String bLastName;
    private String bCity;
    private String bPhone;
    private String sMinPrice;
    private String bPrice;
    private String pName;
    private String sQuantity;
    private String bQuantity;
    private String productId;
    private String costId;
    private String userName;
    private String quality;
    private String userId;
    private String date;
    public String getUserName() {
        return userName;
    }
    public CostInfo() {}

    public CostInfo(String userName, String pName, String sMinPrice, String bPrice, String quality, String sQuantity, String date){

        this.sMinPrice = sMinPrice;
        this.pName=pName;
        this.bPrice = bPrice;
        this.sQuantity = sQuantity;
        this.userName=userName;
        this.quality=quality;
        this.date=date;
    }

    public CostInfo(String userName, String bPrice) {
        this.userName = userName;
        this.bPrice = bPrice;
    }

    public CostInfo(String bFirstName, String bLastName, String bCity, String bPhone, String sMinPrice, String bPrice,
                    String pName, String sQuantity, String productId, String costId, String userName,String quality,String userId) {
        this.bFirstName = bFirstName;
        this.bLastName = bLastName;
        this.bCity = bCity;
        this.bPhone = bPhone;
        this.sMinPrice = sMinPrice;
        this.bPrice = bPrice;
        this.pName = pName;
        this.sQuantity = sQuantity;
        this.productId = productId;
        this.costId = costId;
        this.userName=userName;
        this.quality=quality;
        this.userId=userId;
    }
    public String getUserId() {
        return userId;
    }
    public String getDate() {
        return date;
    }
    public String getQuality() {
        return quality;
    }
    public String getsUserId() {
        return userId;
    }
    public String getCostId() {
        return costId;
    }
    public String getbFirstName() {
        return bFirstName;
    }
    public String getProductId() {
        return productId;
    }
    public String getbLastName() {
        return bLastName;
    }
    public String getbQuantity() {
        return bQuantity;
    }
    public String getbCity() {
        return bCity;
    }
    public String getbPhone() {
        return bPhone;
    }
    public String getsMinPrice() {
        return sMinPrice;
    }
    public String getbPrice() {
        return bPrice;
    }
    public String getpName() {
        return pName;
    }
    public String getsQuantity() {
        return sQuantity;
    }
}
