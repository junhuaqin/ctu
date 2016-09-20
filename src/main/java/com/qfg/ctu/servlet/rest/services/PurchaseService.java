package com.qfg.ctu.servlet.rest.services;

import com.qfg.ctu.servlet.rest.pojos.RestPurchase;
import org.jvnet.hk2.annotations.Service;

/**
 * Created by rbtq on 9/20/16.
 */
@Service(name=PurchaseServiceImpl.NAME)
public interface PurchaseService {
    RestPurchase add(RestPurchase purchase) throws Exception;
}
