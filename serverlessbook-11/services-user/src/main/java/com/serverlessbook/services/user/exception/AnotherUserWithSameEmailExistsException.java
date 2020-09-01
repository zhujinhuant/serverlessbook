package com.serverlessbook.services.user.exception;

/*
邮箱重复存在异常
*/
public class AnotherUserWithSameEmailExistsException extends UserRegistrationException {

    private static final long serialVersionUID = -7048567407775970663L;

    public AnotherUserWithSameEmailExistsException() {
        super("Another user with same E-Mail address already exists.");
    }
}
