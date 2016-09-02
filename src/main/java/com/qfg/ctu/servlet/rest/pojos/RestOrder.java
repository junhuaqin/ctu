package com.qfg.ctu.servlet.rest.pojos;

import java.util.List;

/**
 * Created by rbtq on 7/30/16.
 */
public class RestOrder {
    public int id;
    public String sale;
    public long createdAt;
    public List<RestOrderItem> items;
    public int totalPrice;

    public static class RestOrderItem {
        public String barCode;
        public String title;
        public int unitPrice;
        public int count;
    }
}
