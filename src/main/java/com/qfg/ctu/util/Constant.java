package com.qfg.ctu.util;

import java.time.ZoneOffset;

/**
 * Created by rbtq on 7/30/16.
 */
public class Constant {
    public final static String SENSITIVE = "******";
    public final static ZoneOffset BEIJING_ZONE = ZoneOffset.UTC; //ZoneOffset.of("+08:00");
    public final static String DB_CTU = "ctu";
    public final static String TBL_PRODUCTS = "products";
    public final static String TBL_ACCOUNTS = "accounts";
    public final static String TBL_ROLES    = "roles";
    public final static String TBL_ORDERS   = "orders";
    public final static String TBL_ORDERITEMS = "orderItems";
    public final static String TBL_PURCHASES   = "purchases";
    public final static String TBL_PURCHASE_ITEMS   = "purchaseItems";
    public final static String TBL_PURCHASE_CONFIRMS   = "purchaseConfirm";
    public final static String AUTH = "Authorization";
}
