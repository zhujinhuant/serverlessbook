package com.serverlessbook.services.user;

import com.serverlessbook.services.user.domain.User;

public interface UserService {
	//根据toke获取用户信息
    User getUserByToken(String token) throws UserNotFoundException;
}
