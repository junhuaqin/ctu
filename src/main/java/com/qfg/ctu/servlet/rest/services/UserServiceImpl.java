package com.qfg.ctu.servlet.rest.services;

import com.qfg.ctu.annotations.NeedDB;
import com.qfg.ctu.dao.DaoFactory;
import com.qfg.ctu.servlet.rest.pojos.RestUser;

import javax.inject.Inject;
import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rbtq on 8/3/16.
 */
public class UserServiceImpl implements UserService {
    public final static String NAME = "com.qfg.ctu.servlet.rest.services.UserServiceImpl";

    @Inject
    Connection conn;

    @NeedDB
    @Override
    public RestUser getById(Integer id) throws Exception {
        return new RestUser(DaoFactory.getUserDao(conn).findById(id));
    }

    @NeedDB
    @Override
    public List<RestUser> getAll() throws Exception {
        return  DaoFactory.getUserDao(conn).findAll().stream()
                .map(RestUser::new)
                .collect(Collectors.toList());
    }
}
