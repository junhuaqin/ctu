package com.qfg.ctu.servlet.rest.pojos;

import com.qfg.ctu.dao.pojo.User;
import com.qfg.ctu.util.Constant;
import com.qfg.ctu.util.DateTimeUtil;

/**
 * Created by rbtq on 7/30/16.
 */
public class RestUser {
    public int id;
    public String name;
    public String userName;
    public String password;
    public boolean active;
    public long createAt;
    public long lastLoginAt;
    public int roleId;

    public RestUser() {

    }

    public RestUser(User user) {
        id = user.getId();
        name = user.getName();
        userName = user.getUserName();
        active = user.isActive();
        createAt = DateTimeUtil.getMilli(user.getCreatedAt());
        lastLoginAt = DateTimeUtil.getMilli(user.getLastLoginAt());
        password = Constant.SENSITIVE;
        roleId = user.getRole();
    }

    public User toInner() {
        User user = new User();
        user.setName(name);
        user.setActive(active);
        user.setUserName(userName);
        user.setPassword(password);
        user.setCreatedAt(DateTimeUtil.getLocalDateTime(createAt));
        user.setLastLoginAt(user.getCreatedAt());
        user.setRole(roleId);

        return user;
    }
}
