package com.hugo.hugodemomall.service.impl;

import com.hugo.hugodemomall.dao.UserDao;
import com.hugo.hugodemomall.dto.UserRegisterRequest;
import com.hugo.hugodemomall.model.User;
import com.hugo.hugodemomall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        return userDao.createUser(userRegisterRequest);
    }
}
