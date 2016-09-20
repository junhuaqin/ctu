package com.qfg.ctu.servlet.rest.pojos;

import com.qfg.ctu.dao.pojo.Purchase;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rbtq on 9/20/16.
 */
public class RestPurchaseItem {

    public int id;
    public String barCode;
    public String title;
    public int unitPrice;
    public int amount;
    public int amountConfirmed;
    public List<RestPurchaseConfirm> confirms;

    public RestPurchaseItem() {

    }

    public RestPurchaseItem(Purchase.PurchaseItem item) {
        id = item.getId();
        barCode = item.getBarCode();
        title = item.getTitle();
        unitPrice = item.getUnitPrice();
        amount = item.getAmount();
        amountConfirmed = item.getAmountConfirmed();
        confirms = item.getConfirms().stream().map(RestPurchaseConfirm::new).collect(Collectors.toList());
    }

    public Purchase.PurchaseItem toInner() {
        Purchase.PurchaseItem item = new Purchase.PurchaseItem();
        item.setId(id);
        item.setBarCode(barCode);
        item.setTitle(title);
        item.setUnitPrice(unitPrice);
        item.setAmount(amount);
        item.setAmountConfirmed(amountConfirmed);
        if (null != confirms) {
            item.setConfirms(confirms.stream().map(RestPurchaseConfirm::toInner).collect(Collectors.toList()));
        }

        return item;
    }
}
