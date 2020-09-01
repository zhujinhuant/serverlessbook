package com.serverlessbook.services.user.repository;

import com.serverlessbook.services.user.domain.User;

import com.serverlessbook.services.user.repository.UserRepository;
import javax.inject.Inject;
import java.util.Optional;

public class UserRepositoryDynamoDB implements UserRepository {
	//暂不去数据库取
    public Optional<User> getUserByToken(String token) {
        return Optional.empty();
    }

}
