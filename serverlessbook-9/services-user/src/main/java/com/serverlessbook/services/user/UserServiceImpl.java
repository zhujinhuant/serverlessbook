package com.serverlessbook.services.user;

import com.serverlessbook.services.user.domain.User;
import com.serverlessbook.services.user.repository.UserRepository;
import javax.inject.Inject;
import java.util.Objects;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    
    //构造函数，传入用户存储服务
    @Inject
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        Objects.requireNonNull(userRepository);
    }

    @Override
    public User getUserByToken(String token) throws UserNotFoundException {
    	 return userRepository.getUserByToken(token).orElseThrow(UserNotFoundException::new);
    }
 
}
