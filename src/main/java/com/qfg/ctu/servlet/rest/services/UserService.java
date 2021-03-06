package com.qfg.ctu.servlet.rest.services;

import com.qfg.ctu.servlet.rest.pojos.RestChangePassword;
import com.qfg.ctu.servlet.rest.pojos.RestUser;
import org.jvnet.hk2.annotations.Service;

import java.util.List;

/**
 * Created by rbtq on 7/30/16.
 */

@Service(name=UserServiceImpl.NAME)
public interface UserService {
    RestUser getById(Integer id) throws Exception;
    List<RestUser> getAll() throws Exception;
    RestUser login(String userName, String password) throws Exception;
    RestUser add(RestUser user) throws Exception;
    RestUser changePassword(RestChangePassword restChangePassword) throws Exception;
}
