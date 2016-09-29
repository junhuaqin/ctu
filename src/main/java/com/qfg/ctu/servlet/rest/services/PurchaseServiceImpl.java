package com.qfg.ctu.servlet.rest.services;

import com.qfg.ctu.annotations.NeedDB;
import com.qfg.ctu.dao.DaoFactory;
import com.qfg.ctu.dao.pojo.Purchase;
import com.qfg.ctu.servlet.rest.pojos.RestImportPurchase;
import com.qfg.ctu.servlet.rest.pojos.RestPurchase;
import com.qfg.ctu.servlet.rest.pojos.RestPurchaseConfirm;
import com.qfg.ctu.servlet.rest.pojos.RestPurchaseItem;
import com.qfg.ctu.util.DateTimeUtil;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by rbtq on 9/20/16.
 */
public class PurchaseServiceImpl implements PurchaseService {
    public final static String NAME = "com.qfg.ctu.servlet.rest.services.PurchaseServiceImpl";
    private final static Logger LOGGER = Logger.getLogger(PurchaseServiceImpl.class.getName());

    @Inject
    Connection conn;

    private String getUserName(Integer userId) throws Exception{
        return DaoFactory.getUserDao(conn).findById(userId).getName();
    }

    @NeedDB
    @Override
    public RestPurchase add(Integer userId, RestPurchase purchase) throws Exception {
        Purchase innerPurchase = purchase.toInner();
        innerPurchase.setCreatedAt(DateTimeUtil.nowInBeiJing());
        innerPurchase.setSale(userId);
        innerPurchase.updateTotalPrice();

        DaoFactory.getPurchaseDao(conn).save(innerPurchase);
        innerPurchase.getItems().forEach(n -> {
            try {
                DaoFactory.getPurchaseItemDao(conn).save(innerPurchase.getId(), n);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        RestPurchase savedPurchase = new RestPurchase(innerPurchase);
        savedPurchase.sale = getUserName(userId);
        savedPurchase.amount = 0;
        savedPurchase.amountConfirmed = 0;
        innerPurchase.getItems().forEach(n->savedPurchase.amount+=n.getAmount());

        return savedPurchase;
    }

    @NeedDB
    @Override
    public List<RestPurchase> getAll() throws Exception {
        List<Purchase> purchases = DaoFactory.getPurchaseDao(conn).findAll();

        return purchases.stream().map(n->{
            RestPurchase purchase = new RestPurchase(n);
            try {
                purchase.sale = getUserName(n.getSale());
                List<Purchase.PurchaseItem> items = DaoFactory.getPurchaseItemDao(conn).findByPId(n.getId());
                purchase.amount = 0;
                purchase.amountConfirmed = 0;
                items.forEach(i -> {
                    purchase.amount += i.getAmount();
                    purchase.amountConfirmed += i.getAmountConfirmed();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return purchase;
        }).collect(Collectors.toList());
    }

    @NeedDB
    @Override
    public RestPurchase getById(Integer id) throws Exception {
        Purchase innerPurchase = DaoFactory.getPurchaseDao(conn).findById(id);
        RestPurchase purchase = new RestPurchase(innerPurchase);
        purchase.sale = getUserName(innerPurchase.getSale());
        purchase.items = getAllItems(innerPurchase.getId());

        purchase.amount = 0;
        purchase.amountConfirmed = 0;
        purchase.items.forEach(n -> {
            purchase.amount += n.amount;
            purchase.amountConfirmed += n.amountConfirmed;
        });

        return purchase;
    }

    @NeedDB
    @Override
    public List<RestPurchaseItem> getAllItems(Integer id) throws Exception {
        List<Purchase.PurchaseItem> items = DaoFactory.getPurchaseItemDao(conn).findByPId(id);

        return items.stream()
                .map(RestPurchaseItem::new)
                .peek(n -> {
                    try {
                        n.confirms = getAllConfirms(n.id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .collect(Collectors.toList());
    }

    @NeedDB
    @Override
    public List<RestPurchaseConfirm> getAllConfirms(Integer id) throws Exception {
        List<Purchase.PurchaseItem.Confirm> confirms = DaoFactory.getPurchaseConfirmDao(conn).findByPId(id);
        return confirms.stream().map(n->{
            RestPurchaseConfirm confirm = new RestPurchaseConfirm(n);
            try {
                confirm.sale = getUserName(n.getSale());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return confirm;
        }).collect(Collectors.toList());
    }

    @NeedDB
    @Override
    public RestPurchaseItem addItem(Integer id, RestPurchaseItem item) throws Exception {
        Purchase.PurchaseItem innerItem = item.toInner();
        DaoFactory.getPurchaseItemDao(conn).save(id, innerItem);
        DaoFactory.getPurchaseDao(conn).addTotalPrice(id, innerItem.getUnitPrice()*innerItem.getAmount());

        return new RestPurchaseItem(innerItem);
    }

    @NeedDB
    @Override
    public RestPurchaseConfirm confirm(Integer userId, Integer id, RestPurchaseConfirm confirm) throws Exception {
        Purchase.PurchaseItem.Confirm innerConfirm = confirm.toInner();
        innerConfirm.setConfirmAt(DateTimeUtil.nowInBeiJing());
        innerConfirm.setSale(userId);

        DaoFactory.getPurchaseConfirmDao(conn).save(id, innerConfirm);
        DaoFactory.getPurchaseItemDao(conn).confirm(id, innerConfirm.getAmount());
        Purchase.PurchaseItem purchaseItem = DaoFactory.getPurchaseItemDao(conn).findById(id);
        DaoFactory.getProductDao(conn).plusStore(purchaseItem.getBarCode(), innerConfirm.getAmount());

        RestPurchaseConfirm savedConfirm = new RestPurchaseConfirm(innerConfirm);
        savedConfirm.sale = getUserName(userId);

        return savedConfirm;
    }

    @NeedDB
    @Override
    public RestPurchaseItem updateItem(Integer id, RestPurchaseItem item) throws Exception {
        Purchase.PurchaseItem oldItem = DaoFactory.getPurchaseItemDao(conn).findById(item.id);
        Purchase.PurchaseItem innerItem = item.toInner();
        DaoFactory.getPurchaseItemDao(conn).update(innerItem);
        int changedPrice = item.unitPrice*item.amount - oldItem.getUnitPrice()*oldItem.getAmount();
        DaoFactory.getPurchaseDao(conn).addTotalPrice(id, changedPrice);

        return new RestPurchaseItem(innerItem);
    }

    @NeedDB
    @Override
    public RestPurchaseConfirm updateConfirm(Integer userId, Integer id, RestPurchaseConfirm confirm) throws Exception {
        Purchase.PurchaseItem.Confirm oldConfirm = DaoFactory.getPurchaseConfirmDao(conn).findById(confirm.id);
        Purchase.PurchaseItem.Confirm innerConfirm = confirm.toInner();
        innerConfirm.setConfirmAt(DateTimeUtil.nowInBeiJing());
        innerConfirm.setSale(userId);

        DaoFactory.getPurchaseConfirmDao(conn).update(innerConfirm);
        DaoFactory.getPurchaseItemDao(conn).confirm(id, innerConfirm.getAmount()-oldConfirm.getAmount());

        RestPurchaseConfirm savedConfirm = new RestPurchaseConfirm(innerConfirm);
        savedConfirm.sale = getUserName(userId);

        return savedConfirm;
    }

    @NeedDB
    @Override
    public RestPurchase update(RestPurchase purchase) throws Exception {
        return null;
    }

    @NeedDB
    @Override
    public Boolean deletePurchase(Integer id) throws Exception {
        DaoFactory.getPurchaseDao(conn).delete(id);
        return true;
    }

    @NeedDB
    @Override
    public Boolean deleteItem(Integer id, Integer itemId) throws Exception {
        Purchase.PurchaseItem oldItem = DaoFactory.getPurchaseItemDao(conn).findById(itemId);
        DaoFactory.getPurchaseDao(conn).addTotalPrice(id, -oldItem.getAmount()*oldItem.getUnitPrice());
        DaoFactory.getPurchaseItemDao(conn).delete(itemId);
        return true;
    }

    @NeedDB
    @Override
    public Boolean deleteConfirm(Integer id, Integer confirmId) throws Exception {
        Purchase.PurchaseItem.Confirm oldConfirm = DaoFactory.getPurchaseConfirmDao(conn).findById(confirmId);
        DaoFactory.getPurchaseItemDao(conn).confirm(id, -oldConfirm.getAmount());
        DaoFactory.getPurchaseConfirmDao(conn).delete(confirmId);
        return true;
    }

    @NeedDB
    @Override
    public RestImportPurchase importTBHOrder(String document) throws Exception {
        LOGGER.log(Level.INFO, "receive:"+document);

        RestImportPurchase importPurchase = new RestImportPurchase();
        importPurchase.known = new RestPurchase();
        importPurchase.unknown = new RestPurchase();

        RestPurchaseItem item = new RestPurchaseItem();
        item.amount = 1;
        item.barCode = "12345";
        item.title = "test";
        item.unitPrice = 10000;

        RestPurchaseItem item2 = new RestPurchaseItem();
        item2.amount = 1;
        item2.barCode = "12346";
        item2.title = "test2";
        item2.unitPrice = 10000;

        importPurchase.known.purchaseOrderId="test for import";
        importPurchase.known.amount = 2;
        importPurchase.known.totalPrice = 20000;
        importPurchase.known.items = new ArrayList<>();
        importPurchase.known.items.add(item);
        importPurchase.known.items.add(item2);

        importPurchase.unknown.amount = 1;
        importPurchase.unknown.totalPrice = 10000;
        importPurchase.unknown.items = new ArrayList<>();
        importPurchase.unknown.items.add(item);

        return importPurchase;
    }
}
