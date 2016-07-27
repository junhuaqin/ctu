package com.qfg.ctu.dao.pojo;

/**
 * Created by rbtq on 7/26/16.
 */
public class Product {
    private Integer barCode;
    private String title;
    private Integer unitPrice; // all is real price * 100
    private Integer storeNum;

    public Integer getBarCode() {
        return barCode;
    }

    public void setBarCode(Integer barCode) {
        this.barCode = barCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getStoreNum() {
        return storeNum;
    }

    public void setStoreNum(Integer storeNum) {
        this.storeNum = storeNum;
    }
}
