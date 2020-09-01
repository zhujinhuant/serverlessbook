package com.serverlessbook.services.user.exception;

/*
用户名重复存在异常
*/
public class AnotherUserWithSameUsernameExistsException extends UserRegistrationException {

    private static final long serialVersionUID = 4824390458386666422L;

    public AnotherUserWithSameUsernameExistsException() {
        super("Another user with same username already exists.");
    }
}
