package com.qfg.ctu.servlet.rest.services;

import com.qfg.ctu.annotations.NeedDB;
import com.qfg.ctu.dao.DaoFactory;
import com.qfg.ctu.dao.pojo.User;
import com.qfg.ctu.servlet.rest.exception.InvalidRequestException;
import com.qfg.ctu.servlet.rest.pojos.RestUser;
import com.qfg.ctu.util.DateTimeUtil;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
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

    @NeedDB
    @Override
    public RestUser login(String userName, String password) throws Exception {
        User user = DaoFactory.getUserDao(conn).findByUserName(userName);
        if (!user.checkPassword(password)) {
            throw new InvalidRequestException(Response.Status.UNAUTHORIZED, "Not allowed to access");
        }

        user.setLastLoginAt(DateTimeUtil.nowInBeiJing());
        DaoFactory.getUserDao(conn).setLastLogin(user.getId(), user.getLastLoginAt());

        return new RestUser(user);
    }

    @NeedDB
    @Override
    public RestUser add(RestUser user) throws Exception {
        User innerUser = user.toInner();
        innerUser.setCreatedAt(DateTimeUtil.nowInBeiJing());
        innerUser.setLastLoginAt(innerUser.getCreatedAt());
        DaoFactory.getUserDao(conn).save(innerUser);

        return new RestUser(innerUser);
    }
}
