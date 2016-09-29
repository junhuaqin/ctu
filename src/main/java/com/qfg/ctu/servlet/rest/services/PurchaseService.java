package com.qfg.ctu.servlet.rest.services;

import com.qfg.ctu.servlet.rest.pojos.RestImportPurchase;
import com.qfg.ctu.servlet.rest.pojos.RestPurchase;
import com.qfg.ctu.servlet.rest.pojos.RestPurchaseConfirm;
import com.qfg.ctu.servlet.rest.pojos.RestPurchaseItem;
import org.jvnet.hk2.annotations.Service;

import java.io.InputStream;
import java.util.List;

/**
 * Created by rbtq on 9/20/16.
 */
@Service(name=PurchaseServiceImpl.NAME)
public interface PurchaseService {
    RestPurchase add(Integer userId, RestPurchase purchase) throws Exception;
    List<RestPurchase> getAll() throws Exception;
    RestPurchase getById(Integer id) throws Exception;
    List<RestPurchaseItem> getAllItems(Integer id) throws Exception;
    List<RestPurchaseConfirm> getAllConfirms(Integer id) throws Exception;

    RestPurchaseItem addItem(Integer id, RestPurchaseItem item) throws Exception;
    RestPurchaseConfirm confirm(Integer userId, Integer id, RestPurchaseConfirm confirm) throws Exception;

    RestPurchaseItem updateItem(Integer id, RestPurchaseItem item) throws Exception;
    RestPurchaseConfirm updateConfirm(Integer userId, Integer id, RestPurchaseConfirm confirm) throws Exception;
    RestPurchase update(RestPurchase purchase) throws Exception;

    Boolean deletePurchase(Integer id) throws Exception;
    Boolean deleteItem(Integer id, Integer itemId) throws Exception;
    Boolean deleteConfirm(Integer id, Integer confirmId) throws Exception;

    RestImportPurchase importTBHOrder(InputStream inputStream) throws Exception;
}
