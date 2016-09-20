package com.qfg.ctu.servlet.rest.pojos;

import com.qfg.ctu.dao.pojo.Purchase;
import com.qfg.ctu.util.DateTimeUtil;

/**
 * Created by rbtq on 9/20/16.
 */
public class RestPurchaseConfirm {
    public int id;
    public String sale;
    public int amount;
    public long confirmAt;

    public RestPurchaseConfirm() {

    }

    public RestPurchaseConfirm(Purchase.PurchaseItem.Confirm confirm) {
        id = confirm.getId();
        amount = confirm.getAmount();
        confirmAt = DateTimeUtil.getMilli(confirm.getConfirmAt());
    }

    public Purchase.PurchaseItem.Confirm toInner() {
        Purchase.PurchaseItem.Confirm confirm = new Purchase.PurchaseItem.Confirm();
        confirm.setId(id);
        confirm.setAmount(amount);

        return confirm;
    }
}
