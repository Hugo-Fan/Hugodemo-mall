package com.hugo.hugodemomall.dao;

import com.hugo.hugodemomall.dto.UserLoginRequest;
import com.hugo.hugodemomall.dto.UserRegisterRequest;
import com.hugo.hugodemomall.model.User;

public interface UserDao {

    User getUserById(Integer userId);

    User getUserByEmail(String email);
    Integer createUser(UserRegisterRequest userRegisterRequest);


}
