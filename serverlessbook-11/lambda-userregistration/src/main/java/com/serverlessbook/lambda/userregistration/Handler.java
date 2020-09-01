package com.serverlessbook.lambda.userregistration;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.serverlessbook.lambda.LambdaHandler;
import com.serverlessbook.services.user.UserService;
import com.serverlessbook.services.user.domain.User;

import javax.inject.Inject;
import java.util.Objects;

public class Handler extends LambdaHandler<Handler.RegistrationInput, Handler.RegistrationOutput> {

    //内部类
    public static class RegistrationInput {
        @JsonProperty("username")
        private String username;

        @JsonProperty("email")
        private String email;

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }
    }

    //注册成功跳转地址
    public static class RegistrationOutput {

        private final String resourceUrl;

        public RegistrationOutput(User user) {
            resourceUrl = "/user/" + user.getId();
        }

        @JsonGetter("resourceUrl")
        public String getResourceUrl() {
            return resourceUrl;
        }
    }
    //注入器
    private static final Injector INJECTOR = Guice.createInjector(new DependencyInjectionModule());
    //用户service
    private UserService userService;

    @Inject
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public Handler() {
        INJECTOR.injectMembers(this);
        Objects.requireNonNull(userService);
    }

    @Override
    public RegistrationOutput handleRequest(RegistrationInput input, Context context) {
        User createdUser = userService.registerNewUser(input.username, input.email);
        //返回生成user的原始URL
        return new RegistrationOutput(createdUser);
    }
}
