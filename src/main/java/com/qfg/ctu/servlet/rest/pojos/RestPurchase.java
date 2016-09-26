package com.qfg.ctu.servlet.rest.pojos;

import com.qfg.ctu.dao.pojo.Purchase;
import com.qfg.ctu.util.DateTimeUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rbtq on 9/20/16.
 */
public class RestPurchase {
    public int id;
    public String purchaseOrderId;
    public String sale;
    public long createdAt;
    public List<RestPurchaseItem> items;
    public int totalPrice;
    public int amount;
    public int amountConfirmed;

    public RestPurchase() {

    }

    public RestPurchase(Purchase purchase) {
        id = purchase.getId();
        purchaseOrderId = purchase.getPurchaseOrderId();
        createdAt = DateTimeUtil.getMilli(purchase.getCreatedAt());
        totalPrice = purchase.getTotalPrice();
        items = purchase.getItems().stream().map(RestPurchaseItem::new).collect(Collectors.toList());
    }

    public Purchase toInner() {
        Purchase purchase = new Purchase();
        purchase.setId(id);
        purchase.setPurchaseOrderId(purchaseOrderId);
        purchase.setTotalPrice(totalPrice);

        if (null != items) {
            purchase.setItems(items.stream().map(RestPurchaseItem::toInner).collect(Collectors.toList()));
        }

        return purchase;
    }
}
