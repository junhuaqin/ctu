package com.qfg.ctu.servlet.rest.services;

import com.qfg.ctu.servlet.rest.pojos.RestUser;
import org.jvnet.hk2.annotations.Service;

/**
 * Created by rbtq on 7/30/16.
 */

@Service
public class UserService {
    public RestUser getById(int id) throws Exception {
        RestUser restUser = new RestUser();
        restUser.id = id;
        restUser.name = "sq";

        return restUser;
    }
}
