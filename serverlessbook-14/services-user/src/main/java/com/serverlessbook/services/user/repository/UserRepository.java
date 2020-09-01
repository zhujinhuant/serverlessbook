package com.serverlessbook.services.user.repository;

import com.serverlessbook.services.user.domain.User;

import java.util.Optional;
/*
用户业务逻辑
*/
public interface UserRepository {
	//根据token查询用户
    Optional<User> getUserByToken(String token);
    //更具email查询用户
    Optional<User> getUserByEmail(String email);
    //根据username查询用户
    Optional<User> getUserByUsername(String username);
    //保存用户
    void saveUser(User user);

}
