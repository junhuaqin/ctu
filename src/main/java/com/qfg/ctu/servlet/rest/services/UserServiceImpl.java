package com.qfg.ctu.servlet.rest.services;

import com.qfg.ctu.servlet.rest.pojos.RestUser;

import javax.inject.Inject;
import java.sql.Connection;
import java.util.List;

/**
 * Created by rbtq on 8/3/16.
 */
public class UserServiceImpl implements UserService {
    public final static String NAME = "com.qfg.ctu.servlet.rest.services.UserServiceImpl";

    @Inject
    Connection conn;

    @Override
    public RestUser getById(Integer id) throws Exception {
        RestUser user = new RestUser();
        user.id = id;
        user.name = "test";
        return user;
    }

    @Override
    public List<RestUser> getAll() throws Exception {
        return null;
    }
}
