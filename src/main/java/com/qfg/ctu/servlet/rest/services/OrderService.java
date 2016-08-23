package com.qfg.ctu.servlet.rest.services;

import com.qfg.ctu.servlet.rest.pojos.RestOrder;
import com.qfg.ctu.servlet.rest.pojos.RestStatics;
import org.jvnet.hk2.annotations.Service;

import java.util.List;

/**
 * Created by rbtq on 7/30/16.
 */
@Service(name = OrderServiceImpl.NAME)
public interface OrderService {

    List<RestOrder> getAll(Long from, Long to) throws Exception;

    RestStatics getStatics() throws Exception;

    RestOrder add(Integer userId, RestOrder restOrder) throws Exception;
}
