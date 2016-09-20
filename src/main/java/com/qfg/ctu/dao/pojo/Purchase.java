package com.qfg.ctu.dao.pojo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rbtq on 9/20/16.
 */
public class Purchase {
    private int id;
    private int sale;
    private LocalDateTime createdAt;
    private List<PurchaseItem> items = new ArrayList<>();
    private int totalPrice;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSale() {
        return sale;
    }

    public void setSale(int sale) {
        this.sale = sale;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<PurchaseItem> getItems() {
        return items;
    }

    public void setItems(List<PurchaseItem> items) {
        this.items = items;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public static class PurchaseItem {
        private int id;
        private String barCode;
        private String title;
        private int unitPrice;
        private int amount;
        private int amountConfirmed;
        private List<Confirm> confirms = new ArrayList<>();

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getBarCode() {
            return barCode;
        }

        public void setBarCode(String barCode) {
            this.barCode = barCode;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(int unitPrice) {
            this.unitPrice = unitPrice;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getAmountConfirmed() {
            return amountConfirmed;
        }

        public void setAmountConfirmed(int amountConfirmed) {
            this.amountConfirmed = amountConfirmed;
        }

        public List<Confirm> getConfirms() {
            return confirms;
        }

        public void setConfirms(List<Confirm> confirms) {
            this.confirms = confirms;
        }

        public static class Confirm {
            private int id;
            private int sale;
            private int amount;
            private LocalDateTime confirmAt;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getSale() {
                return sale;
            }

            public void setSale(int sale) {
                this.sale = sale;
            }

            public int getAmount() {
                return amount;
            }

            public void setAmount(int amount) {
                this.amount = amount;
            }

            public LocalDateTime getConfirmAt() {
                return confirmAt;
            }

            public void setConfirmAt(LocalDateTime confirmAt) {
                this.confirmAt = confirmAt;
            }
        }
    }
}
