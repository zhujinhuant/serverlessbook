package com.serverlessbook.services.user;

import com.serverlessbook.services.user.domain.User;
import com.serverlessbook.services.user.exception.AnotherUserWithSameEmailExistsException;
import com.serverlessbook.services.user.exception.AnotherUserWithSameUsernameExistsException;
import com.serverlessbook.services.user.exception.InvalidMailAddressException;
import com.serverlessbook.services.user.exception.UserRegistrationException;
import com.serverlessbook.services.user.repository.UserRepository;

import javax.inject.Inject;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

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
    
    //注册用户
    @Override
    public User registerNewUser(String username, String email) throws UserRegistrationException {

        checkEmailValidity(email);  //邮箱合法性验证
        checkEmailUniqueness(email);    //邮箱重复性验证
        checkUsernameUniqueness(username);  //用户名重复性验证

        User newUser = new User()
                .setId(UUID.randomUUID().toString())
                .setUsername(username)
                .setEmail(email);
        //保存用户信息
        userRepository.saveUser(newUser);
        return newUser;
    }
    //邮箱合法性验证
    private void checkEmailValidity(String email) throws InvalidMailAddressException {
        final String emailPattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        if (!Pattern.compile(emailPattern).matcher(email).matches()) {
            throw new InvalidMailAddressException();
        }
    }
    //去数据库验证邮箱不重复
    void checkEmailUniqueness(String email) throws AnotherUserWithSameEmailExistsException {
        if (userRepository.getUserByEmail(email).isPresent()) {
            throw new AnotherUserWithSameEmailExistsException();
        }
    }
    //去数据库验证用户名不重复
    void checkUsernameUniqueness(String username) throws AnotherUserWithSameUsernameExistsException {
        if (userRepository.getUserByUsername(username).isPresent()) {
            throw new AnotherUserWithSameUsernameExistsException();
        }
    }
}
