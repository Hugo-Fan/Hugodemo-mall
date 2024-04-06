package com.hugo.hugodemomall.service;

import com.hugo.hugodemomall.dto.UserLoginRequest;
import com.hugo.hugodemomall.dto.UserRegisterRequest;
import com.hugo.hugodemomall.model.User;

public interface UserService {
    User getUserById(Integer userId);
    Integer register(UserRegisterRequest userRegisterRequest);

    User login(UserLoginRequest userLoginRequest);
}
