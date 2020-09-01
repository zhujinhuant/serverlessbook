package com.serverlessbook.services.user;

import com.serverlessbook.services.user.domain.User;
import com.serverlessbook.services.user.exception.UserRegistrationException;

public interface UserService {
	//根据toke获取用户信息
    User getUserByToken(String token) throws UserNotFoundException;

    //注册用户
    User registerNewUser(String username, String email) throws UserRegistrationException;
}
