package com.hugo.hugodemomall.dao;

import com.hugo.hugodemomall.dto.UserRegisterRequest;
import com.hugo.hugodemomall.model.User;

public interface UserDao {

    User getUserById(Integer userId);
    Integer createUser(UserRegisterRequest userRegisterRequest);
}
