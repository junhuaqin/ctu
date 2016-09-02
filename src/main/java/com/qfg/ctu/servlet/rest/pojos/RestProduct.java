package com.qfg.ctu.servlet.rest.pojos;

import com.qfg.ctu.dao.pojo.Product;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by rbtq on 7/26/16.
 */

@XmlRootElement
public class RestProduct {
    public String barCode;
    public String title;
    public int unitPrice;
    public int left;

    public RestProduct() {

    }

    public RestProduct(Product product) {
        this.barCode = product.getBarCode();
        this.title = product.getTitle();
        this.unitPrice = product.getUnitPrice();
        this.left = product.getStoreNum();
    }

    public Product toInner() {
        Product product = new Product();
        product.setBarCode(this.barCode);
        product.setTitle(this.title);
        product.setUnitPrice(this.unitPrice);
        product.setStoreNum(this.left);

        return product;
    }
}
