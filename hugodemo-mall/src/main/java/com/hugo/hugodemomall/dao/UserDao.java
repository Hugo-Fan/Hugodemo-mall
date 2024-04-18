package com.hugo.hugodemomall.dao;

import com.hugo.hugodemomall.dto.UserLoginRequest;
import com.hugo.hugodemomall.dto.UserRegisterRequest;
import com.hugo.hugodemomall.model.User;
import com.hugo.hugodemomall.model.UserRoles;

public interface UserDao {

    User getUserById(Integer userId);

    User getUserByEmail(String email);
    Integer createUser(UserRegisterRequest userRegisterRequest);

    UserRoles getUserByRole(Integer userId);
}
